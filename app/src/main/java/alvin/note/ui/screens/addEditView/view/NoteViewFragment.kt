package alvin.note.ui.screens.addEditView.view

import alvin.note.R
import alvin.note.databinding.FragmentNoteViewBinding
import alvin.note.ui.screens.base.BaseFragment
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteViewFragment : BaseFragment<FragmentNoteViewBinding>() {
    override val viewModel: NoteViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_note_view
    private val args: NoteViewFragmentArgs by navArgs()
    private var selectedColorId: Int = R.color.white

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNoteById(args.noteId)
    }

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.btnEdit?.setOnClickListener {
            viewModel.note.value?.id?.let { noteId ->
                val action = NoteViewFragmentDirections.actionNoteViewFragmentToEditFragment(noteId)
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                // Show/hide loading indicator based on loading state
                binding?.loadingOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.note.collect { note ->
                if (note != null) {
                    binding?.tvTitle?.text = note.title
                    binding?.tvDesc?.text = note.desc
                    selectedColorId = getColorId(note.color)
                    applyColor(selectedColorId)
                }
            }
        }
    }

    private fun getColorId(color: Int): Int {
        return when (color) {
            ContextCompat.getColor(requireContext(), R.color.red) -> R.color.red
            ContextCompat.getColor(requireContext(), R.color.yellow) -> R.color.yellow
            ContextCompat.getColor(requireContext(), R.color.green) -> R.color.green
            ContextCompat.getColor(requireContext(), R.color.cyan) -> R.color.cyan
            ContextCompat.getColor(requireContext(), R.color.purple) -> R.color.purple
            else -> R.color.white
        }
    }

    private fun applyColor(colorId: Int) {
        binding?.main?.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
    }
}