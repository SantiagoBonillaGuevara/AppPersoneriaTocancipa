// app/src/main/java/com/personeriatocancipa/app/domain/model/PqrsUiModel.kt
package com.personeriatocancipa.app.domain.model

data class PqrsUiModel(
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val date: Long,
    val userName: String
)
