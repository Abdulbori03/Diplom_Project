package com.example.myproject.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    
    val allGroups: Flow<List<Group>> = repository.getAllGroups()

    fun getGroupById(id: Long): Flow<Group> = repository.getGroupById(id)
    
    fun getProductCountInGroup(groupId: Long): Flow<Int> = repository.getProductCountInGroup(groupId)

    sealed class InsertResult {
        object Success : InsertResult()
        data class Error(val message: String) : InsertResult()
    }

    private val _insertResult = MutableLiveData<InsertResult>()
    val insertResult: LiveData<InsertResult> = _insertResult

    fun insertGroup(group: Group) {
        viewModelScope.launch {
            try {
                repository.insertGroup(group)
                _insertResult.value = InsertResult.Success
            } catch (e: Exception) {
                _insertResult.value = InsertResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateGroup(group: Group) = viewModelScope.launch {
        repository.updateGroup(group)
    }

    fun deleteGroup(group: Group) = viewModelScope.launch {
        repository.deleteGroup(group)
    }
} 