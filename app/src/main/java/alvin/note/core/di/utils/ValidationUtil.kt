package alvin.note.core.di.utils

import alvin.note.data.model.ValidationField

object ValidationUtil {
    fun validate(vararg fields: ValidationField): String? {
        fields.forEach { field ->
            if(!Regex(field.regExp).matches(field.value)) {
                return field.errorMsg
            }
        }
        return null
    }
}