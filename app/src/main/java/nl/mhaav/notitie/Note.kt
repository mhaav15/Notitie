package nl.mhaav.notitie

data class Note(
    val id: String,
    val text: String,
    val done: Boolean,
    val createdAt: Long,
)
