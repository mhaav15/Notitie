package nl.mhaav.notitie

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import nl.mhaav.notitie.databinding.ActivityNotitieblokBinding

class NotitieblokActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotitieblokBinding
    private lateinit var store: NotepadStore
    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    private val handler = Handler(Looper.getMainLooper())
    private val saveRunnable = Runnable { persist(updateWidget = true) }
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotitieblokBinding.inflate(layoutInflater)
        setContentView(binding.root)
        store = NotepadStore.from(this)
        bindWidget(intent)

        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (loading) return
                handler.removeCallbacks(saveRunnable)
                handler.postDelayed(saveRunnable, SAVE_DELAY_MS)
            }
        })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        persist(updateWidget = true)
        bindWidget(intent)
    }

    private fun bindWidget(intent: Intent) {
        widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        )
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        loading = true
        binding.input.setText(store.getText(widgetId))
        binding.input.setSelection(binding.input.text?.length ?: 0)
        loading = false
        binding.input.requestFocus()
    }

    override fun onPause() {
        handler.removeCallbacks(saveRunnable)
        persist(updateWidget = true)
        super.onPause()
    }

    override fun onDestroy() {
        handler.removeCallbacks(saveRunnable)
        super.onDestroy()
    }

    private fun persist(updateWidget: Boolean) {
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) return
        store.setText(widgetId, binding.input.text?.toString().orEmpty())
        if (updateWidget) {
            WidgetUpdater.refreshNotepad(this, widgetId)
        }
    }

    companion object {
        private const val SAVE_DELAY_MS = 400L
    }
}
