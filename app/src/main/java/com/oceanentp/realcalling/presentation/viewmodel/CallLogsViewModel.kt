package com.oceanentp.realcalling.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oceanentp.realcalling.data.model.CallLogEntry
import com.oceanentp.realcalling.data.repository.CallLogRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CallLogsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CallLogRepository(application)

    val callLogs: StateFlow<List<CallLogEntry>> = repository.getCallLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
