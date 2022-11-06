package com.masorone.encryptionalgorithms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EncryptFragmentOneViewModel(
    private val repository: MessageRepository,
    private val algorithm: EncryptionAlgorithm.Caesar
) : ViewModel() {

    private val _uiState: MutableStateFlow<EncryptFragmentOneContract.State> =
        MutableStateFlow(EncryptFragmentOneContract.State.Initial)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<EncryptFragmentOneContract.Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<EncryptFragmentOneContract.Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    fun setEvent(event: EncryptFragmentOneContract.Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    private fun setState(state: EncryptFragmentOneContract.State) {
        _uiState.value = state
    }

    private fun setEffect(effect: EncryptFragmentOneContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: EncryptFragmentOneContract.Event) {
        when (event) {
            is EncryptFragmentOneContract.Event.Back -> {
                setEffect(EncryptFragmentOneContract.Effect.CloseApp)
            }
            is EncryptFragmentOneContract.Event.MessageEntering -> {
                setState(EncryptFragmentOneContract.State.TextEntered)
            }
            is EncryptFragmentOneContract.Event.SendButtonClick -> {
                send(event.message, event.key)
            }
            is EncryptFragmentOneContract.Event.DecryptButtonClick -> {

            }
        }
    }

    private fun send(message: String, key: String) {
        when (val result = algorithm.encrypt(message, key)) {
            is EncryptionAlgorithm.Result.Success -> {
                setEffect(
                    EncryptFragmentOneContract.Effect.Navigate(
                        Message(result.map(), key)
                    )
                )
            }
            is EncryptionAlgorithm.Result.EmptyMessage -> {

            }
            is EncryptionAlgorithm.Result.UnknownSign -> {}
            is EncryptionAlgorithm.Result.NegativeKey -> {}
            is EncryptionAlgorithm.Result.Verified -> {}
        }
    }
}