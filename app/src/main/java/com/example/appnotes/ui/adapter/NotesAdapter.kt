package com.example.appnotes.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnotes.databinding.RecycleItemNotePadBinding
import com.example.appnotes.data.model.NoteModel
import com.example.appnotes.util.loadImage

class NotesAdapter(private val onItemClick : (noteModel: NoteModel) -> Unit) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private val data : MutableList<NoteModel> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder.inflate(parent)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = data[position]
        holder.bind(note, onItemClick)
    }

    fun updateNotes(allNotes:List<NoteModel>){
        data.clear()
        data.addAll(allNotes)
        notifyDataSetChanged()
    }

    class NotesViewHolder(private val binding: RecycleItemNotePadBinding):RecyclerView.ViewHolder(binding.root){
        private val imageNote = binding.imgNote
        private val titleNote = binding.txvTitleNote
        private val contentNote = binding.txvContentNote

        companion object{
            fun inflate(viewGroup: ViewGroup):NotesViewHolder{
                val binding = RecycleItemNotePadBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return NotesViewHolder(binding)
            }
        }

        fun bind(note: NoteModel, onItemClick: (noteModel: NoteModel) -> Unit) {

            titleNote.text = note.titulo
            contentNote.text = note.descricao

            if(note.imagem.isNullOrEmpty()){
                imageNote.visibility = View.GONE
            }
            else {
                imageNote.apply {
                    visibility = View.VISIBLE
                    loadImage(note.imagem)
                }
            }

            binding.root.setOnClickListener {
                onItemClick(note)
            }
        }
    }
}