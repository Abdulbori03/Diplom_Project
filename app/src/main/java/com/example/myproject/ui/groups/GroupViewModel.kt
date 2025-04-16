package com.example.myproject.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.Group
import com.example.myproject.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {

    val groups: Flow<List<Group>> = repository.getAllGroups()

    fun getGroupById(id: Long): Flow<Group> = repository.getGroupById(id)

    fun getProductCountInGroup(groupId: Long): Flow<Int> = repository.getProductCountInGroup(groupId)

    fun insertGroup(group: Group) {
        viewModelScope.launch {
            repository.insertGroup(group)
        }
    }

    fun updateGroup(group: Group) {
        viewModelScope.launch {
            repository.updateGroup(group)
        }
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            repository.deleteGroup(group)
        }
    }
} 