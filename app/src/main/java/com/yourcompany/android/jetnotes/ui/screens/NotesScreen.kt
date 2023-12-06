package com.yourcompany.android.jetnotes.ui.screens

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.yourcompany.android.jetnotes.domain.model.NoteModel
import com.yourcompany.android.jetnotes.ui.components.Note
import com.yourcompany.android.jetnotes.ui.components.TopAppBar
import com.yourcompany.android.jetnotes.viewmodel.MainViewModel

@Composable
fun NotesScreen(viewModel: MainViewModel) {

  val notes by viewModel.notesNotInTrash.observeAsState(listOf())

  Column {
    TopAppBar(
      title = "JetNotes",
      icon = Icons.Filled.List,
      onIconClick = {}
    )
    NotesList(
      notes = notes,
      onNoteCheckedChange = {
        viewModel.onNoteCheckedChange(it)
      },
      onNoteClicked = {
        viewModel.onNoteClick(it)
      }
    )
  }
}

@Composable
fun NotesList(
  notes: List<NoteModel>,
  onNoteCheckedChange: (NoteModel) -> Unit,
  onNoteClicked: (NoteModel) -> Unit,
) {

  LazyColumn {
    items(count = notes.size) {
      val note = notes[it]
      Note(
        note = note,
        onNoteClick = onNoteClicked,
        onNoteCheckedChange = onNoteCheckedChange
      )
    }
  }
}

@Preview
@Composable
private fun NotesListPreview(){
  NotesList(
    notes = listOf(
      NoteModel(1, "Note1", "Content1", null),
      NoteModel(2, "Note2", "Content2", false),
      NoteModel(3, "Note3", "Content3", true)
    ),
    onNoteCheckedChange = {},
    onNoteClicked = {}
  )
}