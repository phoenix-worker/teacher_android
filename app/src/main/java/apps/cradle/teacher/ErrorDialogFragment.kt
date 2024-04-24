package apps.cradle.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import apps.cradle.teacher.databinding.DialogErrorBinding

class ErrorDialogFragment : DialogFragment() {

    private lateinit var binding: DialogErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogErrorBinding.inflate(inflater)
        val correctAnswerString = arguments?.getString(EXTRA_MESSAGE)
        binding.message.text = correctAnswerString
        binding.button.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    companion object {

        const val EXTRA_MESSAGE = "extra_message"
    }
}