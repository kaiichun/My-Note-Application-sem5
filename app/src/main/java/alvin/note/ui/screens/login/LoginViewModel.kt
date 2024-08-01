package alvin.note.ui.screens.login

import alvin.note.core.service.AuthService
import alvin.note.data.repository.NoteRepository
import alvin.note.ui.screens.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: NoteRepository
) : BaseViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            errorHandler {
                validate(email, password)
                authService.loginWithEmailAndPassword(email, password)

            }?.let {
                success.emit(Unit)
            }
            _isLoading.emit(false)
        }
    }

    private fun validate(email: String, password: String) {
        if (email.isEmpty()) {
            throw Exception("Email cannot be empty")
        }
        if (password.isEmpty()) {
            throw Exception("Password cannot be empty")
        }
    }

}