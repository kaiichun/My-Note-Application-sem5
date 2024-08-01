package alvin.note.ui.screens.addEditView.add

import alvin.note.R
import alvin.note.ui.screens.addEditView.base.BaseManageAddEditFragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFragment: BaseManageAddEditFragment() {
    override val viewModel: AddViewModel by viewModels()

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().navigate(
                    AddFragmentDirections.actionAddToHome()
                )
            }
        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Note add successfully", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.darkGreen)
                ).show()
            }
        }
    }
}