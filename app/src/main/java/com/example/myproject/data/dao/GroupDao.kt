package com.example.myproject.data.dao

import androidx.room.*
import com.example.myproject.data.model.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups")
    fun getAllGroups(): Flow<List<Group>>

    @Query("SELECT * FROM groups WHERE id = :id")
    fun getGroupById(id: Long): Flow<Group>

    @Query("SELECT COUNT(*) FROM products WHERE group_id = :groupId")
    fun getProductCountInGroup(groupId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)
} 