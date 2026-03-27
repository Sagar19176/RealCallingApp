package com.oceanentp.realcalling.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceanentp.realcalling.data.model.CallLogEntry
import com.oceanentp.realcalling.data.model.Contact
import com.oceanentp.realcalling.data.repository.CallLogRepository
import com.oceanentp.realcalling.data.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Represents the UI state for any data operation.
 *
 * - [Loading]: An operation is in progress; the UI should show a loading indicator.
 * - [Success]: Data was loaded successfully; [data] holds the result.
 * - [Error]: An error occurred; [message] holds a human-readable description.
 */
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

/**
 * MainViewModel integrates [ContactsRepository] and [CallLogRepository] to provide
 * observable UI state for both contacts and call logs.
 *
 * This ViewModel follows MVVM best practices:
 * - All data operations run inside [viewModelScope] to tie lifecycle to the ViewModel.
 * - State is exposed as read-only [StateFlow]s so the Compose UI can collect them safely.
 * - Mutable backing fields (_contactsState, _callLogsState) are private to prevent
 *   external mutation.
 *
 * @param contactsRepository Repository responsible for fetching device contacts.
 * @param callLogRepository  Repository responsible for fetching call log entries.
 */
class MainViewModel(
    private val contactsRepository: ContactsRepository,
    private val callLogRepository: CallLogRepository
) : ViewModel() {

    // ---- Contacts state -------------------------------------------------------

    /** Backing mutable state for contacts; initialised as Loading. */
    private val _contactsState = MutableStateFlow<UiState<List<Contact>>>(UiState.Loading)

    /**
     * Read-only StateFlow of the contacts UI state.
     * Compose screens should collect this to render the contacts list.
     */
    val contactsState: StateFlow<UiState<List<Contact>>> = _contactsState.asStateFlow()

    // ---- Call logs state ------------------------------------------------------

    /** Backing mutable state for call logs; initialised as Loading. */
    private val _callLogsState = MutableStateFlow<UiState<List<CallLogEntry>>>(UiState.Loading)

    /**
     * Read-only StateFlow of the call logs UI state.
     * Compose screens should collect this to render the call log list.
     */
    val callLogsState: StateFlow<UiState<List<CallLogEntry>>> = _callLogsState.asStateFlow()

    // ---- Initialisation -------------------------------------------------------

    init {
        // Load both data sets as soon as the ViewModel is created.
        loadContacts()
        loadCallLogs()
    }

    // ---- Data loading ---------------------------------------------------------

    /**
     * Loads (or reloads) the contacts list from [ContactsRepository].
     *
     * Sets [contactsState] to [UiState.Loading] before fetching, then transitions
     * to [UiState.Success] on completion or [UiState.Error] if an exception occurs.
     */
    fun loadContacts() {
        viewModelScope.launch {
            _contactsState.value = UiState.Loading
            try {
                // first() collects only the first emission and then cancels the flow,
                // making this a safe single-shot read that won't accumulate collectors
                // when called multiple times (e.g. on permission grant or refresh).
                val contacts = contactsRepository.getContacts().first()
                _contactsState.value = UiState.Success(contacts)
            } catch (e: Exception) {
                _contactsState.value = UiState.Error(
                    e.message ?: "Failed to load contacts"
                )
            }
        }
    }

    /**
     * Loads (or reloads) the call log entries from [CallLogRepository].
     *
     * Sets [callLogsState] to [UiState.Loading] before fetching, then transitions
     * to [UiState.Success] on completion or [UiState.Error] if an exception occurs.
     */
    fun loadCallLogs() {
        viewModelScope.launch {
            _callLogsState.value = UiState.Loading
            try {
                // first() collects only the first emission and then cancels the flow,
                // making this a safe single-shot read that won't accumulate collectors
                // when called multiple times (e.g. on permission grant or refresh).
                val callLogs = callLogRepository.getCallLogs().first()
                _callLogsState.value = UiState.Success(callLogs)
            } catch (e: Exception) {
                _callLogsState.value = UiState.Error(
                    e.message ?: "Failed to load call logs"
                )
            }
        }
    }

    // ---- Refresh helpers ------------------------------------------------------

    /**
     * Convenience method to refresh the contacts list.
     * Delegates to [loadContacts]; can be wired to a pull-to-refresh gesture.
     */
    fun refreshContacts() = loadContacts()

    /**
     * Convenience method to refresh the call logs list.
     * Delegates to [loadCallLogs]; can be wired to a pull-to-refresh gesture.
     */
    fun refreshCallLogs() = loadCallLogs()

    /**
     * Refreshes both contacts and call logs simultaneously.
     * Useful after permissions are granted or when the user performs a global refresh.
     */
    fun refreshAll() {
        refreshContacts()
        refreshCallLogs()
    }
}
