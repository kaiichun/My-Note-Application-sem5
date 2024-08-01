package alvin.note.ui.screens.addEditView.base

import alvin.note.R
import alvin.note.databinding.FragmentBaseManageAddEditBinding
import alvin.note.ui.screens.base.BaseFragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

abstract class BaseManageAddEditFragment: BaseFragment<FragmentBaseManageAddEditBinding>() {
    override val viewModel: BaseManageAddEditViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_base_manage_add_edit
    var selectedColorId = R.color.white

    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.btnSubmit?.setOnClickListener {
            val title = binding?.etTitle?.text.toString()
            val description = binding?.etDescription?.text.toString()
            val color = ContextCompat.getColor(
                requireContext(),
                selectedColorId
            ) // Get actual color value

            viewModel.submitNote(title, description, color)
        }

        // Set click listeners for color selection views
        binding?.viewRed?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewYellow?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewGreen?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewCyan?.setOnClickListener { onClickColorSelect(it) }
        binding?.viewPurple?.setOnClickListener { onClickColorSelect(it) }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding?.progressBarContainer?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

     fun onClickColorSelect(view: View) {
        val colorId = when (view.id) {
            R.id.viewRed -> R.color.red
            R.id.viewYellow -> R.color.yellow
            R.id.viewGreen -> R.color.green
            R.id.viewCyan -> R.color.cyan
            R.id.viewPurple -> R.color.purple
            else -> R.color.white
        }

        resetColors()

        // Set the selected background color and border
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
        view.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.color_picker_border)

        // Set the root background color
        binding?.root?.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))

        selectedColorId = colorId
    }

     private fun resetColors() {
        binding?.run {
            setBackgroundAndBorder(viewRed, R.color.red)
            setBackgroundAndBorder(viewYellow, R.color.yellow)
            setBackgroundAndBorder(viewGreen, R.color.green)
            setBackgroundAndBorder(viewCyan, R.color.cyan)
            setBackgroundAndBorder(viewPurple, R.color.purple)
        }
    }

    private fun setBackgroundAndBorder(view: View, colorId: Int) {
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
        view.foreground = null // Remove the border for non-selected colors
    }

    fun getColorId(color: Int): Int {
        return when (color) {
            ContextCompat.getColor(requireContext(), R.color.red) -> R.color.red
            ContextCompat.getColor(requireContext(), R.color.yellow) -> R.color.yellow
            ContextCompat.getColor(requireContext(), R.color.green) -> R.color.green
            ContextCompat.getColor(requireContext(), R.color.cyan) -> R.color.cyan
            ContextCompat.getColor(requireContext(), R.color.purple) -> R.color.purple
            else -> R.color.white
        }
    }

    fun applyColor(colorId: Int) {
        val colorView = when (colorId) {
            R.color.red -> binding?.viewRed
            R.color.yellow -> binding?.viewYellow
            R.color.green -> binding?.viewGreen
            R.color.cyan -> binding?.viewCyan
            R.color.purple -> binding?.viewPurple
            else -> null
        }
        colorView?.setBackgroundResource(R.drawable.color_picker_border)
    }
}