package alvin.note.data.model

data class ValidationField(
    val value: String,
    val regExp: String,
    val errorMsg: String
)
