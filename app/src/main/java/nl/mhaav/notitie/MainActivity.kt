package nl.mhaav.notitie

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.mhaav.notitie.databinding.ActivityMainBinding
import nl.mhaav.notitie.databinding.ItemNoteBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repo: NoteRepository
    private val adapter = NoteAdapter(
        onToggle = { id ->
            repo.toggle(id)
            refresh()
        },
        onDelete = { id ->
            repo.delete(id)
            refresh()
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = NoteRepository.from(this)

        binding.noteList.layoutManager = LinearLayoutManager(this)
        binding.noteList.adapter = adapter
        binding.btnSave.setOnClickListener { save() }
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save()
                true
            } else {
                false
            }
        }
        refresh()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun save() {
        val note = repo.add(binding.input.text?.toString().orEmpty())
        if (note == null) {
            Toast.makeText(this, R.string.empty_note, Toast.LENGTH_SHORT).show()
            return
        }
        binding.input.text?.clear()
        refresh()
    }

    private fun refresh() {
        val notes = repo.getAll()
        adapter.submit(notes)
        WidgetUpdater.refreshAll(this)
        binding.empty.visibility = if (notes.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }
}

private class NoteAdapter(
    private val onToggle: (String) -> Unit,
    private val onDelete: (String) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.Holder>() {

    private var items: List<Note> = emptyList()

    fun submit(notes: List<Note>) {
        items = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class Holder(
        private val binding: ItemNoteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteText.text = note.text
            binding.btnToggle.text = if (note.done) {
                binding.root.context.getString(R.string.done_mark)
            } else {
                binding.root.context.getString(R.string.open_mark)
            }
            binding.noteText.paintFlags = if (note.done) {
                binding.noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            binding.noteText.alpha = if (note.done) 0.55f else 1f
            binding.btnToggle.setOnClickListener { onToggle(note.id) }
            binding.btnDelete.setOnClickListener { onDelete(note.id) }
        }
    }
}
