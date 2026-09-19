package nl.mhaav.notitie

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class NotitieWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return NoteRemoteViewsFactory(applicationContext)
    }
}

private class NoteRemoteViewsFactory(
    private val context: Context,
) : RemoteViewsService.RemoteViewsFactory {

    private var notes: List<Note> = emptyList()

    override fun onCreate() = Unit

    override fun onDataSetChanged() {
        notes = NoteRepository.from(context).getAll()
    }

    override fun onDestroy() {
        notes = emptyList()
    }

    override fun getCount(): Int = notes.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position !in notes.indices) {
            return RemoteViews(context.packageName, R.layout.widget_note_item)
        }
        val note = notes[position]
        val views = RemoteViews(context.packageName, R.layout.widget_note_item)
        views.setTextViewText(R.id.note_text, note.text)
        views.setContentDescription(
            R.id.btn_toggle,
            if (note.done) context.getString(R.string.mark_open) else context.getString(R.string.mark_done),
        )
        views.setContentDescription(R.id.btn_delete, context.getString(R.string.delete))
        views.setTextViewText(
            R.id.btn_toggle,
            if (note.done) context.getString(R.string.done_mark) else context.getString(R.string.open_mark),
        )

        val textColor = context.getColor(if (note.done) R.color.widget_text_muted else R.color.widget_text)
        views.setTextColor(R.id.note_text, textColor)
        val flags = if (note.done) {
            Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        } else {
            Paint.ANTI_ALIAS_FLAG
        }
        views.setInt(R.id.note_text, "setPaintFlags", flags)

        views.setOnClickFillInIntent(
            R.id.note_text,
            fillInIntent(note.id, NotitieAppWidgetProvider.ACTION_TOGGLE),
        )
        views.setOnClickFillInIntent(
            R.id.btn_toggle,
            fillInIntent(note.id, NotitieAppWidgetProvider.ACTION_TOGGLE),
        )
        views.setOnClickFillInIntent(
            R.id.btn_delete,
            fillInIntent(note.id, NotitieAppWidgetProvider.ACTION_DELETE),
        )
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long =
        notes.getOrNull(position)?.id.hashCode().toLong()

    override fun hasStableIds(): Boolean = true

    private fun fillInIntent(noteId: String, action: String): Intent {
        return Intent().apply {
            putExtra(NotitieAppWidgetProvider.EXTRA_NOTE_ID, noteId)
            putExtra(NotitieAppWidgetProvider.EXTRA_ITEM_ACTION, action)
            data = Uri.parse("notitie://note/$noteId/$action")
        }
    }
}
