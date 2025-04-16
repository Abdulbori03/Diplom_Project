package com.example.myproject.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.Group
import com.example.myproject.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _insertResult = MutableStateFlow<Result<Unit>?>(null)
    val insertResult: StateFlow<Result<Unit>?> = _insertResult.asStateFlow()

    fun insertGroup(group: Group) {
        viewModelScope.launch {
            try {
                groupRepository.insertGroup(group)
                _insertResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _insertResult.value = Result.failure(e)
            }
        }
    }
} 