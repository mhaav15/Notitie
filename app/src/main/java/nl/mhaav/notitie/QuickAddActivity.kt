package nl.mhaav.notitie

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nl.mhaav.notitie.databinding.ActivityQuickAddBinding

class QuickAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.input.requestFocus()
        binding.btnSave.setOnClickListener { save() }
        binding.btnCancel.setOnClickListener { finish() }
        binding.scrim.setOnClickListener { finish() }
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save()
                true
            } else {
                false
            }
        }
    }

    private fun save() {
        val text = binding.input.text?.toString().orEmpty()
        val note = NoteRepository.from(this).add(text)
        if (note == null) {
            Toast.makeText(this, R.string.empty_note, Toast.LENGTH_SHORT).show()
            return
        }
        WidgetUpdater.refreshAll(this)
        finish()
    }
}
