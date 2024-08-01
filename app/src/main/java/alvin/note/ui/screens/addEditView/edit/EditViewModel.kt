package alvin.note.ui.screens.addEditView.edit

import alvin.note.data.model.Note
import alvin.note.data.repository.NoteRepository
import alvin.note.ui.screens.addEditView.base.BaseManageAddEditViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repo: NoteRepository,
    state: SavedStateHandle
) : BaseManageAddEditViewModel() {
    val note: MutableStateFlow<Note?> = MutableStateFlow(null)

    fun getNoteById(noteId: String) {
        viewModelScope.launch {
            note.value = repo.getNoteById(noteId)
        }
    }

    override fun submitNote(title: String, description: String, color: Int) {
        note.value?.let {
            val newNote = it.copy(title = title, desc = description, color = color)
            viewModelScope.launch {
                repo.updateNote(newNote)
                finish.emit(Unit)
            }
        }
    }
}