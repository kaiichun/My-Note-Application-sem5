package alvin.note.ui.screens.addEditView.base

import alvin.note.ui.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseManageAddEditViewModel: BaseViewModel() {

    val loading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = loading
    override val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    abstract fun submitNote(title: String, description: String, color: Int)
}