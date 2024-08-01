package alvin.note.ui.screens.addEditView.view

import alvin.note.data.model.Note
import alvin.note.data.repository.NoteRepository
import alvin.note.ui.screens.base.BaseViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: NoteRepository,
    private val state: SavedStateHandle
) : BaseViewModel() {
    val note: MutableStateFlow<Note?> = MutableStateFlow(null)
    override val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val noteId = state.get<String>("noteId")

    fun getNoteById(noteId: String) {
        viewModelScope.launch {
            isLoading.value = true
            note.value = repo.getNoteById(noteId)
            isLoading.value = false
        }
    }
}