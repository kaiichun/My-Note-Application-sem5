package alvin.note.data.model

data class User(
    val id:String? = null,
    val firstName:String,
    val lastName:String,
    val email:String,
    val profilePicture: String? = null
){
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
        )
    }

    companion object{
        fun fromMap(map: Map<String, Any?>): User {
            return User(
                firstName = map["firstName"].toString(),
                lastName = map["lastName"].toString(),
                email = map["email"].toString(),
                profilePicture = map["profilePicture"].toString(),
            )
        }
    }
}