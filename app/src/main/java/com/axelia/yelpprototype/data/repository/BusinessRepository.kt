package com.axelia.yelpprototype.data.repository

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.MainThread
import com.axelia.yelpprototype.data.local.dao.BusinessDao
import com.axelia.yelpprototype.data.remote.api.YelpService
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.model.Businesses
import com.axelia.yelpprototype.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class BusinessRepository @Inject constructor(
    private val itemsDao: BusinessDao,
    private val apiService: YelpService
) : Parcelable {

    constructor(parcel: Parcel) : this(
        TODO("itemsDao"),
        TODO("apiService")
    ) {
    }

    fun getAllItems(
        term: String?,
        location: String
    ): Flow<State<List<Business>>> {
        return object : NetworkBoundRepository<List<Business>, Businesses>() {

            override suspend fun saveRemoteData(response: Businesses) = itemsDao.insertItems(response.businesses)

            override fun fetchFromLocal(): Flow<List<Business>> = itemsDao.getAllItems()

            override suspend fun fetchFromRemote(): Response<Businesses> = apiService.getBusinesses(
                term = term,
                location = location
            )

            override suspend fun deleteCurrentData() = itemsDao.deleteAllItems()

        }.asFlow().flowOn(Dispatchers.IO)
    }

    fun getAllItemsByCurrentLocation(
        term: String?,
        latitude: Double,
        longitude: Double
    ): Flow<State<List<Business>>> {
        return object : NetworkBoundRepository<List<Business>, Businesses>() {

            override suspend fun saveRemoteData(response: Businesses) = itemsDao.insertItems(response.businesses)

            override fun fetchFromLocal(): Flow<List<Business>> = itemsDao.getAllItems()

            override suspend fun fetchFromRemote(): Response<Businesses> = apiService.getBusinessesByCurrentLocation(
                term = term,
                latiude = latitude,
                longitude = longitude
            )

            override suspend fun deleteCurrentData() = itemsDao.deleteAllItems()

        }.asFlow().flowOn(Dispatchers.IO)
    }

    @MainThread
    fun getItemById(itemId: String): Flow<Business> = itemsDao.getItemById(itemId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BusinessRepository> {
        override fun createFromParcel(parcel: Parcel): BusinessRepository {
            return BusinessRepository(parcel)
        }

        override fun newArray(size: Int): Array<BusinessRepository?> {
            return arrayOfNulls(size)
        }
    }
}
