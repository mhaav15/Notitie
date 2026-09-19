package nl.mhaav.notitie

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.UUID

/**
 * Local-only note storage. One JSON file, newest notes first.
 */
class NoteRepository(private val storageFile: File) {

    fun getAll(): List<Note> = synchronized(lock) {
        readUnlocked()
    }

    fun add(text: String): Note? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return null
        return synchronized(lock) {
            val notes = readUnlocked().toMutableList()
            val note = Note(
                id = UUID.randomUUID().toString(),
                text = trimmed,
                done = false,
                createdAt = System.currentTimeMillis(),
            )
            notes.add(0, note)
            writeUnlocked(notes)
            note
        }
    }

    fun toggle(id: String): Note? = synchronized(lock) {
        val notes = readUnlocked().toMutableList()
        val index = notes.indexOfFirst { it.id == id }
        if (index < 0) return null
        val updated = notes[index].copy(done = !notes[index].done)
        notes[index] = updated
        writeUnlocked(notes)
        updated
    }

    fun delete(id: String): Boolean = synchronized(lock) {
        val notes = readUnlocked().toMutableList()
        val removed = notes.removeAll { it.id == id }
        if (removed) writeUnlocked(notes)
        removed
    }

    private fun readUnlocked(): List<Note> {
        if (!storageFile.exists()) return emptyList()
        val raw = storageFile.readText()
        if (raw.isBlank()) return emptyList()
        return try {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        Note(
                            id = obj.getString("id"),
                            text = obj.getString("text"),
                            done = obj.optBoolean("done", false),
                            createdAt = obj.optLong("createdAt", 0L),
                        ),
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun writeUnlocked(notes: List<Note>) {
        storageFile.parentFile?.mkdirs()
        val array = JSONArray()
        notes.forEach { note ->
            array.put(
                JSONObject().apply {
                    put("id", note.id)
                    put("text", note.text)
                    put("done", note.done)
                    put("createdAt", note.createdAt)
                },
            )
        }
        storageFile.writeText(array.toString())
    }

    companion object {
        private val lock = Any()
        const val FILE_NAME = "notes.json"

        fun from(context: Context): NoteRepository {
            return NoteRepository(File(context.applicationContext.filesDir, FILE_NAME))
        }
    }
}
