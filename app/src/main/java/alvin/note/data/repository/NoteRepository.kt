package alvin.note.data.repository

import alvin.note.core.service.AuthService
import alvin.note.data.model.Note
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NoteRepository(private val authService: AuthService) {
    private val collection = Firebase.firestore.collection("notes")
    private fun getCollection(): CollectionReference {
        val uid = authService.getUid() ?: throw  Exception ("User ID doesn't exist")
        return  Firebase.firestore.collection("root_db/$uid/notes")
    }

    fun getAllNotes() = callbackFlow<List<Note>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            val notes = mutableListOf<Note>()
            value?.documents?.map { item ->
                item.data?.let { todoMap ->
                    val note = Note.fromMap(todoMap)
                    notes.add(note.copy(id = item.id))
                }
            }
            trySend(notes)
        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addNote(note: Note): String? {
        val response = getCollection().add(note.toMap()).await()
        return response?.id
    }

    suspend fun deleteNote(id:String) {
        getCollection().document(id).delete().await()
    }

    suspend fun updateNote(note: Note) {
        if ( note.id != null) {
            getCollection().document(note.id).set(note.toMap()).await()
        }
    }

    suspend fun getNoteById(id: String):Note? {
        val res = getCollection().document(id).get().await()
        return res.data?.let { Note.fromMap(it).copy(id = res.id) }
    }
}
