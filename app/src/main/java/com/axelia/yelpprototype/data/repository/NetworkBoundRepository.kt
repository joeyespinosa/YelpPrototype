package com.axelia.yelpprototype.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.axelia.yelpprototype.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    fun asFlow() = flow<State<RESULT>> {

        // Emit Loading State
        emit(State.loading())

        try {
            // Emit Database content first
            emit(State.success(fetchFromLocal().first()))

            // Fetch latest from remote
            val apiResponse = fetchFromRemote()

            // Parse body
            val remoteItems = apiResponse.body()

            // Check for response validation
            if (apiResponse.isSuccessful && remoteItems != null) {
                // Delete existing items
                deleteCurrentData()

                // Save into the persistence storage
                saveRemoteData(remoteItems)
            } else {
                // Something went wrong! Emit Error state.
                emit(State.error(apiResponse.message()))
            }
        } catch (e: Exception) {
            // Exception occurred! Emit error
            emit(State.error("Network error! Can't get items."))
            e.printStackTrace()
        }

        // Retrieve from persistence storage and emit
        emitAll(fetchFromLocal().map {
            State.success<RESULT>(it)
        })
    }

    /**
     * Saves retrieved from remote into the persistence storage.
     */
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Delete current items of the persistence storage.
     */
    @WorkerThread
    protected abstract suspend fun deleteCurrentData()

    /**
     * Retrieves all data from persistence storage.
     */
    @MainThread
    protected abstract fun fetchFromLocal(): Flow<RESULT>

    /**
     * Fetches [Response] from the remote end point.
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}