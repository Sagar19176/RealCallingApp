package com.oceanentp.realcalling.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oceanentp.realcalling.data.repository.CallLogRepository
import com.oceanentp.realcalling.data.repository.ContactsRepository

/**
 * Factory for creating [MainViewModel] instances with constructor-injected repositories.
 *
 * Because [MainViewModel] takes [ContactsRepository] and [CallLogRepository] as constructor
 * parameters (rather than using a DI framework), a custom [ViewModelProvider.Factory] is
 * required so that the Android framework can create the ViewModel correctly.
 *
 * Usage in a Composable or Activity:
 * ```kotlin
 * val viewModel: MainViewModel = viewModel(
 *     factory = MainViewModelFactory(contactsRepository, callLogRepository)
 * )
 * ```
 *
 * @param contactsRepository Repository for reading device contacts.
 * @param callLogRepository  Repository for reading call log entries.
 */
class MainViewModelFactory(
    private val contactsRepository: ContactsRepository,
    private val callLogRepository: CallLogRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(contactsRepository, callLogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
