package alvin.note.ui.screens.register

import alvin.note.core.di.utils.ValidationUtil
import alvin.note.core.service.AuthService
import alvin.note.data.model.User
import alvin.note.data.model.ValidationField
import alvin.note.data.repository.UserRepository
import alvin.note.ui.screens.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepository
) : BaseViewModel() {

    fun register(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
        val error = ValidationUtil.validate(
            ValidationField(firstName,"[a-zA-Z ]{2,20}", "Enter a valid name"),
            ValidationField(lastName,"[a-zA-Z ]{2,20}", "Enter a valid name"),
            ValidationField(email,"[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+", "Enter a valid name"),
            ValidationField(password,"[a-zA-Z0-9#$%]{6,20}", "Enter a valid password")
        )
        if(error == null) {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.emit(true)
                errorHandler {
                    authService.createUserWithEmailAndPassword(email, password)
                }?.let {
                    userRepo.createUser(
                        User(
                            firstName = firstName,
                            lastName = lastName,
                            email = email
                        )
                    )
                    success.emit(Unit)
                }
                _isLoading.emit(false)
            }
        } else {
            viewModelScope.launch {
                _error.emit(error)
            }
        }
    }
}