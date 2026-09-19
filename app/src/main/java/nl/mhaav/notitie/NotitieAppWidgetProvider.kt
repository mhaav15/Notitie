package nl.mhaav.notitie

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

class NotitieAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        appWidgetIds.forEach { id ->
            updateWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val noteId = intent.getStringExtra(EXTRA_NOTE_ID) ?: return
        val repo = NoteRepository.from(context)
        when (intent.getStringExtra(EXTRA_ITEM_ACTION)) {
            ACTION_TOGGLE -> repo.toggle(noteId)
            ACTION_DELETE -> repo.delete(noteId)
            else -> return
        }
        WidgetUpdater.refreshAll(context)
    }

    companion object {
        const val ACTION_ITEM_CLICK = "nl.mhaav.notitie.ACTION_ITEM_CLICK"
        const val ACTION_TOGGLE = "toggle"
        const val ACTION_DELETE = "delete"
        const val EXTRA_NOTE_ID = "note_id"
        const val EXTRA_ITEM_ACTION = "item_action"

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_notitie)
            val count = NoteRepository.from(context).getAll().size
            views.setTextViewText(
                R.id.widget_count,
                context.resources.getQuantityString(R.plurals.note_count, count, count),
            )

            val addIntent = Intent(context, QuickAddActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val addPending = PendingIntent.getActivity(
                context,
                appWidgetId,
                addIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            views.setOnClickPendingIntent(R.id.quick_add_bar, addPending)
            views.setOnClickPendingIntent(R.id.btn_add, addPending)
            views.setOnClickPendingIntent(R.id.widget_title, addPending)

            val serviceIntent = Intent(context, NotitieWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.note_list, serviceIntent)
            views.setEmptyView(R.id.note_list, R.id.empty_view)

            val clickIntent = Intent(context, NotitieAppWidgetProvider::class.java).apply {
                action = ACTION_ITEM_CLICK
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val clickPending = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
            )
            views.setPendingIntentTemplate(R.id.note_list, clickPending)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
