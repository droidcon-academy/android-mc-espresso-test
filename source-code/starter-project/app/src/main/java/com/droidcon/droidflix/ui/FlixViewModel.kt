package com.droidcon.droidflix.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.droidflix.data.OMDBClient
import com.droidcon.droidflix.data.model.Flix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlixViewModel: ViewModel() {

    private val _flixList = MutableStateFlow<List<Flix>>(emptyList())
    val flixList: StateFlow<List<Flix>> = _flixList
    private val _flix = MutableStateFlow<Flix?>(null)
    val flix: StateFlow<Flix?> = _flix
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading
    var currentPage = 1
    private var debounceJob: Job? = null

    fun getFlix(input: String = "", page: Int = 1) {
        currentPage = page
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            delay(700)
            val response = OMDBClient.searchFlix(input, page)
            _error.emit(response.error)
            when {
                response.error?.isNotBlank() == true -> _flixList.emit(emptyList())
                page == 1 -> _flixList.emit(response.search)
                else -> _flixList.emit(_flixList.value + response.search)
            }
            _loading.emit(false)
        }
    }

    fun getFlix(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            val response = OMDBClient.searchFlix(id)
            _flix.emit(response)
            _loading.emit(false)
        }
    }
}