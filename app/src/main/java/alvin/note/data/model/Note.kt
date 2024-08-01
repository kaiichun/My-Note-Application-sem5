package alvin.note.data.model

data class Note(
    val id: String? = null,
    val title: String,
    val desc: String,
    val color: Int
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "title" to title,
            "desc" to desc,
            "color" to color
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Note {
            return Note(
                title = map["title"] as String,
                desc = map["desc"] as String,
                color = (map["color"] as? Long)?.toInt() ?: map["color"] as Int
            )
        }
    }
}