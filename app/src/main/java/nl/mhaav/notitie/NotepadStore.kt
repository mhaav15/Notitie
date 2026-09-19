package nl.mhaav.notitie

import android.content.Context
import java.io.File

/**
 * Local freeform notepad text, one file per widget instance.
 */
class NotepadStore(private val dir: File) {

    fun getText(widgetId: Int): String = synchronized(lock) {
        val file = fileFor(widgetId)
        if (!file.exists()) return@synchronized ""
        file.readText()
    }

    fun setText(widgetId: Int, text: String) = synchronized(lock) {
        dir.mkdirs()
        fileFor(widgetId).writeText(text)
    }

    fun delete(widgetId: Int) = synchronized(lock) {
        fileFor(widgetId).delete()
    }

    fun rename(oldWidgetId: Int, newWidgetId: Int) = synchronized(lock) {
        if (oldWidgetId == newWidgetId) return@synchronized
        val oldFile = fileFor(oldWidgetId)
        if (!oldFile.exists()) return@synchronized
        val newFile = fileFor(newWidgetId)
        newFile.delete()
        oldFile.renameTo(newFile)
    }

    private fun fileFor(widgetId: Int): File = File(dir, "blok_$widgetId.txt")

    companion object {
        private val lock = Any()
        const val DIR_NAME = "notepads"

        fun from(context: Context): NotepadStore {
            return NotepadStore(File(context.applicationContext.filesDir, DIR_NAME))
        }
    }
}
