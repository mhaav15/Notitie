package nl.mhaav.notitie

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

object WidgetUpdater {
    fun refreshAll(context: Context) {
        refreshComponent(context, NotitieAppWidgetProvider::class.java) { manager, ids ->
            manager.notifyAppWidgetViewDataChanged(ids, R.id.note_list)
        }
    }

    fun refreshNotepad(context: Context, widgetId: Int? = null) {
        refreshComponent(
            context,
            NotitieblokAppWidgetProvider::class.java,
            widgetId,
        )
    }

    private fun refreshComponent(
        context: Context,
        provider: Class<*>,
        widgetId: Int? = null,
        extra: ((AppWidgetManager, IntArray) -> Unit)? = null,
    ) {
        val appContext = context.applicationContext
        val manager = AppWidgetManager.getInstance(appContext)
        val ids = if (widgetId != null) {
            intArrayOf(widgetId)
        } else {
            manager.getAppWidgetIds(ComponentName(appContext, provider))
        }
        if (ids.isEmpty()) return
        extra?.invoke(manager, ids)
        val intent = Intent(appContext, provider).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
        appContext.sendBroadcast(intent)
    }
}
