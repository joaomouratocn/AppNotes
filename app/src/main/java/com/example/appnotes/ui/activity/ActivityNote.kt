package com.example.appnotes.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appnotes.R
import com.example.appnotes.databinding.ActivityNoteBinding
import com.example.appnotes.databinding.DialogLoadImageBinding
import com.example.appnotes.data.room.database.AppDatabase
import com.example.appnotes.data.model.NoteModel
import com.example.appnotes.data.repository.NoteRepository
import com.example.appnotes.data.webclient.webclientservices.NoteWebService
import com.example.appnotes.util.NOTE_ID
import com.example.appnotes.util.createDialog
import com.example.appnotes.util.loadImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ActivityNote : AppCompatActivity() {
    private val binding by lazy {
        ActivityNoteBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        NoteRepository(
            noteDao = AppDatabase.getInstance(this).noteDao(),
            noteWebService = NoteWebService()
        )
    }

    private var noteImage:MutableStateFlow<String?> = MutableStateFlow(null)
    private lateinit var receivedNote: NoteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.str_new_note)
        binding.imgNote.visibility = GONE

        configureBtnImageClick()
        configureImageView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                getReceivedNote()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_act_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_done -> {
                saveNote()
            }
            R.id.menu_delete -> {
                showDialogDeleteNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun verifyFields():Boolean{
        return binding.edtTitleNote.text.isNotEmpty() && binding.edtContentNote.text.isNotEmpty()
    }

    private fun configureImageView(){
        lifecycleScope.launch {
            noteImage.collect{
                if (it.isNullOrEmpty()){
                    binding.imgNote.visibility = GONE
                }else{
                    binding.imgNote.apply {
                        loadImage(it)
                        visibility = VISIBLE
                    }
                }
            }
        }
    }

    private suspend fun getReceivedNote() {
        val receivedNoteId = intent.getStringExtra(NOTE_ID)
        receivedNoteId?.let {
            repository.getNoteById(receivedNoteId).filterNotNull().collect{
                receivedNote = it
                noteImage.value = receivedNote.imagem
                loadFields()
            }
        }
    }

    private fun loadFields() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        noteImage.value = receivedNote.imagem
        binding.apply {
            edtTitleNote.setText(receivedNote.titulo)
            edtContentNote.setText(receivedNote.descricao)
        }
    }

    private fun saveNote(){
        lifecycleScope.launch{
            createNote()?.let {
                repository.insertNote(it)
                finish()
            }
        }
    }

    private fun deleteNote(){
        lifecycleScope.launch {
            receivedNote.let {
                repository.deleteNote(it)
                finish()
            }
        }
    }

    private fun configureBtnImageClick(){
        binding.btnAddImage.setOnClickListener { showDialogLoadImage() }
    }

    private fun createNote(): NoteModel?{
        return if (!verifyFields()){
            Toast.makeText(this, getString(R.string.str_fields_invalid), Toast.LENGTH_SHORT).show()
            null
        }else{
            val noteTitle = binding.edtTitleNote.text.toString()
            val noteContent = binding.edtContentNote.text.toString()

            if (::receivedNote.isInitialized){
                 return NoteModel(
                    id = receivedNote.id,
                    titulo = noteTitle,
                    descricao = noteContent,
                    imagem = noteImage.value
                )
            }else{
                return NoteModel(
                    titulo = noteTitle,
                    descricao = noteContent,
                    imagem = noteImage.value
                )
            }
        }
    }

    private fun showDialogDeleteNote(){
        createDialog(
            title = getString(R.string.str_delete),
            message = getString(R.string.str_really_wish_delete_1s, receivedNote.titulo),
            negativeButtonText = getString(R.string.str_cancel),
            positiveButtonText = getString(R.string.str_yes)
        ){
            deleteNote()
        }.show()
    }

    private fun showDialogLoadImage(){
        val view = DialogLoadImageBinding.inflate(LayoutInflater.from(this))
        val dialog = createDialog(view)

        with(view) {
            var url:String? = null
            noteImage.value?.let {
                edtUrlNote.setText(it)
                this.imgNote.loadImage(it)
            }

            btnLoadImage.setOnClickListener {
                url = edtUrlNote.text.toString()
                this.imgNote.loadImage(url)
            }

            btnSaveImage.setOnClickListener {
                noteImage.value = url
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}