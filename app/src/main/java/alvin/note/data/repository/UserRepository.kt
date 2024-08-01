package alvin.note.data.repository

import alvin.note.core.service.AuthService
import alvin.note.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val authService: AuthService
) {
    private fun getUid(): String {
        val uid = Firebase.auth.currentUser?.uid ?: throw  Exception ("User ID doesn't exist")
        return uid
    }

    private fun getUserCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    suspend fun createUser(user: User) {
        getUserCollectionReference().document(getUid()).set(user).await()
    }

    suspend fun getUser(): User? {
        val result = getUserCollectionReference().document(getUid()).get().await()
        return result.data?.let { User.fromMap(it) }
    }

    suspend fun updateUser(user: User) {
        getUserCollectionReference().document(getUid()).set(user).await()
    }

}
