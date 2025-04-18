package com.example.myproject.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.Group
import com.example.myproject.data.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    private val _insertResult = MutableStateFlow<Result<Unit>?>(null)
    val insertResult: StateFlow<Result<Unit>?> = _insertResult.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            repository.getAllGroups().collect { groups ->
                _groups.value = groups
            }
        }
    }

    fun getGroupById(id: Long): Flow<Group> = repository.getGroupById(id)

    fun getProductCountInGroup(groupId: Long): Flow<Int> = repository.getProductCountInGroup(groupId)

    fun insertGroup(group: Group) {
        viewModelScope.launch {
            try {
                repository.insertGroup(group)
                _insertResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _insertResult.value = Result.failure(e)
            }
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

    class Factory(
        private val repository: GroupRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GroupViewModel::class.java)) {
                return GroupViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 