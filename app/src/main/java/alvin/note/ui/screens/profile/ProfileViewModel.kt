package alvin.note.ui.screens.profile

import alvin.note.core.service.AuthService
import alvin.note.data.model.User
import alvin.note.data.repository.UserRepository
import alvin.note.ui.screens.base.BaseViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    val user = MutableLiveData<User>()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            errorHandler {
                userRepository.getUser()
            }?.let {
                user.value = it
            }
        }
    }

    fun updateUserProfile(imageName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            errorHandler {
                user.value?.let {
                    userRepository.updateUser(it.copy(profilePicture = imageName))
                }
                finish.emit(Unit)
            }
            _isLoading.value = false
        }
    }
}