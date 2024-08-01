package alvin.note.ui.screens.addEditView.add

import alvin.note.data.model.Note
import alvin.note.data.repository.NoteRepository
import alvin.note.ui.screens.addEditView.base.BaseManageAddEditViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repo: NoteRepository
) : BaseManageAddEditViewModel() {

    override fun submitNote(title: String, description: String, color: Int) {
        viewModelScope.launch {
            loading.value = true
            repo.addNote(
                Note(
                    title = title,
                    desc = description,
                    color = color
                )
            )
            loading.value = false
            finish.emit(Unit)
        }

    }

}