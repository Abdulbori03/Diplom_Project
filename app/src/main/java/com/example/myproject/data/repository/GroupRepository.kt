package com.example.myproject.data.repository

import com.example.myproject.data.dao.GroupDao
import com.example.myproject.data.model.Group
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao
) {
    fun getAllGroups(): Flow<List<Group>> = groupDao.getAllGroups()
    
    fun getGroupById(id: Long): Flow<Group> = groupDao.getGroupById(id)
    
    fun getProductCountInGroup(groupId: Long): Flow<Int> = groupDao.getProductCountInGroup(groupId)
    
    suspend fun insertGroup(group: Group) = groupDao.insertGroup(group)
    
    suspend fun updateGroup(group: Group) = groupDao.updateGroup(group)
    
    suspend fun deleteGroup(group: Group) = groupDao.deleteGroup(group)
} 