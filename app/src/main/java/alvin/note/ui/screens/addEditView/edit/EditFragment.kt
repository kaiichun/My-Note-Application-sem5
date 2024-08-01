package alvin.note.ui.screens.addEditView.edit

import alvin.note.R
import alvin.note.ui.screens.addEditView.base.BaseManageAddEditFragment
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : BaseManageAddEditFragment() {
    override val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNoteById(args.noteId)
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.finish.collect {
                NavHostFragment.findNavController(this@EditFragment).navigate(
                    EditFragmentDirections.actionEditToHome()
                )
            }
        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.btnSubmit?.text = getString(R.string.button_update)

        lifecycleScope.launch {
            viewModel.note.collect { note ->
                if (note != null) {
                    binding?.etTitle?.setText(note.title)
                    binding?.etDescription?.setText(note.desc)
                    binding?.root?.setBackgroundColor(note.color)
                    selectedColorId = getColorId(note.color)
                    applyColor(selectedColorId)
                }
            }
        }

        binding?.viewRed?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewYellow?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewGreen?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewCyan?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewPurple?.setOnClickListener { onClickColorSelect(it) }

        binding?.btnSubmit?.setOnClickListener {
            val title = binding?.etTitle?.text.toString()
            val description = binding?.etDescription?.text.toString()
            val color = ContextCompat.getColor(requireContext(), selectedColorId)
            viewModel.submitNote(title, description, color)
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Note is updated", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.darkGreen)
                ).show()
            }
        }
    }


}