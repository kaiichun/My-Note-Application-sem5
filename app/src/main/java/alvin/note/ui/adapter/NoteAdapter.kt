package alvin.note.ui.adapter

import alvin.note.data.model.Note
import alvin.note.databinding.LayoutNoteCardBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var notes: List<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = LayoutNoteCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = notes[position]
        holder.bind(item)
    }

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    fun letTextBecomeDot(text: String, maxLength: Int): String {
        val lines = text.lines()
        val firstLine = lines.firstOrNull() ?: ""
        return if (firstLine.length > maxLength) {
            firstLine.substring(0, maxLength) + "..."
        } else {
            if (lines.size > 1) {
                "$firstLine..."
            } else {
                firstLine
            }
        }
    }

    inner class NoteViewHolder(
        private val binding: LayoutNoteCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            val shortTitle = letTextBecomeDot(note.title, 30)
            binding.tvTitle.text = shortTitle
            val shortDescription = letTextBecomeDot(note.desc, 30)
            binding.tvDescription.text = shortDescription
            binding.cvNote.setBackgroundColor(note.color)
            binding.cvNote.setOnClickListener {
                listener?.onClick(note)
            }
            binding.cvNote.setOnLongClickListener {
                listener?.onLongClick(note)
                return@setOnLongClickListener true
            }
        }
    }

    interface Listener {
        fun onClick(note: Note)
        fun onLongClick(note:Note)
    }
}
