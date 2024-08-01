package alvin.note.ui.screens.home

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import alvin.note.R
import alvin.note.data.model.Note
import alvin.note.databinding.AlertDeleteNoteViewBinding
import alvin.note.databinding.FragmentHomeBinding
import alvin.note.ui.adapter.NoteAdapter
import alvin.note.ui.screens.BottomSheetFragment
import alvin.note.ui.screens.base.BaseFragment
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: NoteAdapter
    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_home

    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        binding?.fabAdd?.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToAdd()
            )
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                adapter.setNotes(notes)
                binding?.tvNoContent?.isInvisible = adapter.itemCount != 0
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding?.loadingOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Note deleted successfully", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.darkGreen))
                    .show()
            }
        }
    }

    private fun setupAdapter() {
        adapter = NoteAdapter(emptyList())
        adapter.listener = object : NoteAdapter.Listener {
            override fun onClick(note: Note) {
                note.id?.let {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeToNoteView(it)
                    )
                }
            }

            override fun onLongClick(note: Note) {
                BottomSheetFragment.getInstance(
                    onEdit = {
                        note.id?.let {
                            findNavController().navigate(
                                HomeFragmentDirections.actionHomeToEdit(it)
                            )
                        }
                    },
                    onDelete = {
                        val alertView = AlertDeleteNoteViewBinding.inflate(layoutInflater)
                        val deleteDialog = AlertDialog.Builder(requireContext())
                        deleteDialog.setView(alertView.root)
                        val temporaryDeleteDialog = deleteDialog.create()

                        alertView.btnDelete.setOnClickListener {
                            viewModel.deleteNotes(note.id!!)
                            temporaryDeleteDialog.dismiss() // Dismiss the dialog after deletion
                        }
                        alertView.btnCancel.setOnClickListener {
                            temporaryDeleteDialog.dismiss() // Dismiss the dialog when cancel button is clicked
                        }
                        temporaryDeleteDialog.show()
                    }
                ).show(parentFragmentManager, "bottomSheet")
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvNotes?.adapter = adapter
        binding?.rvNotes?.layoutManager = layoutManager
    }
}
