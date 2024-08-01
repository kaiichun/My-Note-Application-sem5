package alvin.note.ui.screens

import alvin.note.databinding.LayoutBottomSheetBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment private constructor(
    val onEdit: () -> Unit,
    val onDelete: () -> Unit
): BottomSheetDialogFragment() {

    private  lateinit var binding: LayoutBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivEdit.setOnClickListener { onEdit(); this.dismiss() }
        binding.ivDelete.setOnClickListener { onDelete(); dismiss() }
    }

    companion object {
        fun getInstance(
            onEdit: () -> Unit,
            onDelete: () -> Unit
        ): BottomSheetFragment {
            return  BottomSheetFragment(onEdit, onDelete)
        }
    }

}