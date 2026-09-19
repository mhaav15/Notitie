package nl.mhaav.notitie

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

class NotitieblokAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        appWidgetIds.forEach { id ->
            updateWidget(context, appWidgetManager, id)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        val store = NotepadStore.from(context)
        appWidgetIds.forEach { store.delete(it) }
    }

    override fun onRestored(context: Context, oldWidgetIds: IntArray, newWidgetIds: IntArray) {
        val store = NotepadStore.from(context)
        oldWidgetIds.forEachIndexed { index, oldId ->
            val newId = newWidgetIds.getOrNull(index) ?: return@forEachIndexed
            store.rename(oldId, newId)
        }
    }

    companion object {
        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
        ) {
            val text = NotepadStore.from(context).getText(appWidgetId)
            val views = RemoteViews(context.packageName, R.layout.widget_notitieblok)
            if (text.isBlank()) {
                views.setTextViewText(R.id.notepad_body, context.getString(R.string.notepad_hint))
                views.setTextColor(R.id.notepad_body, context.getColor(R.color.paper_hint))
            } else {
                views.setTextViewText(R.id.notepad_body, text)
                views.setTextColor(R.id.notepad_body, context.getColor(R.color.paper_text))
            }

            val editIntent = Intent(context, NotitieblokActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse("notitieblok://widget/$appWidgetId")
            }
            val editPending = PendingIntent.getActivity(
                context,
                appWidgetId,
                editIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            views.setOnClickPendingIntent(R.id.notepad_root, editPending)
            views.setOnClickPendingIntent(R.id.notepad_body, editPending)
            views.setOnClickPendingIntent(R.id.notepad_title, editPending)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
