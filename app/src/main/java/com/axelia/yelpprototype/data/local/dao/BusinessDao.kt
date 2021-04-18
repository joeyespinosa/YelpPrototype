package com.axelia.yelpprototype.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.axelia.yelpprototype.model.Business
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<Business>)

    @Query("DELETE FROM ${Business.TABLE_NAME}")
    fun deleteAllItems()

    @Query("SELECT * FROM ${Business.TABLE_NAME} WHERE ID = :businessId")
    fun getItemById(businessId: String): Flow<Business>

    @Query("SELECT * FROM ${Business.TABLE_NAME}")
    fun getAllItems(): Flow<List<Business>>

    @Query("SELECT * FROM ${Business.TABLE_NAME} WHERE isFavorite = 1")
    fun getAllItemFavorites(): Flow<List<Business>>

    @Query("UPDATE ${Business.TABLE_NAME} SET isFavorite = 1 WHERE id = :itemId")
    suspend fun favoriteItem(itemId: String): Int

    @Query("UPDATE ${Business.TABLE_NAME} SET isFavorite = 0 WHERE id = :itemId")
    suspend fun removeAsFavoriteItem(itemId: String): Int
}