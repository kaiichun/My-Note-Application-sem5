package alvin.note.ui.screens.home

import alvin.note.data.model.Note
import alvin.note.data.repository.NoteRepository
import alvin.note.ui.screens.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: NoteRepository
) : BaseViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            repo.getAllNotes().collect { noteList ->
                _notes.value = noteList
                _isLoading.value = false
            }
        }
    }

    fun deleteNotes(noteId: String) {
        viewModelScope.launch {
            errorHandler {
                repo.deleteNote(noteId)
                finish.emit(Unit)
            }
        }
    }
}
