package com.masorone.encryptionalgorithms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class EncryptFragmentOne : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageTv: EditText
    private lateinit var keyTv: EditText
    private lateinit var sendButton: Button

    private val caesar by lazy {
        EncryptionAlgorithm.Caesar.RuLowerCase(
            VerifiedData.StringMessageAndKey(
                ProvideResources.Base(requireContext().applicationContext.resources)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_encript, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        messageTv = view.findViewById(R.id.edit_text_message)
        keyTv = view.findViewById(R.id.edit_text_key)
        sendButton = view.findViewById(R.id.button_send)

        sendButton.setOnClickListener {
            val result = caesar.encrypt(
                messageTv.text.toString(),
                keyTv.text.toString()
            )
        }
    }

    companion object {

        fun newInstance(message: Message) = EncryptFragmentOne().apply {
            arguments = Bundle().apply {
                putParcelable(MESSAGE_KEY, message)
            }
        }

        fun newInstance() = EncryptFragmentOne()

        private const val MESSAGE_KEY = "message"
    }
}

interface UiState
interface UiEvent
interface UiEffect

class EncryptFragmentOneContract {

    sealed class Event : UiEvent {
        object Back : Event()
        object MessageEntering : Event()
        class SendButtonClick(val message: String, val key: String) : Event()
        class DecryptButtonClick(val message: String, val key: String) : Event()
    }

    sealed class State : UiState {
        object Initial : State()
        object TextEntered : State()
        class BothError() : State()
        class OneError() : State()
        class UnknownSignError() : State()
        class NegativeKeyError() : State()
        class Success() : State()
    }

    sealed class Effect : UiEffect {
        class Navigate(private val message: Message) : Effect()
        class ShowToast(private val message: String) : Effect()
        object CloseApp : Effect()
    }
}