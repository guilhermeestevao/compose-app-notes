package com.yourcompany.android.jetnotes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yourcompany.android.jetnotes.domain.model.NoteModel
import com.yourcompany.android.jetnotes.ui.components.Note
import com.yourcompany.android.jetnotes.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
  viewModel: MainViewModel,
  onOpenNavigatinDrawer: () -> Unit = {},
  onNavigationToSaveNote: () -> Unit = {}
) {
  val notes by viewModel.notesNotInTrash.observeAsState(listOf())
  val scaffoldState = rememberScaffoldState()

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
      TopAppBar(
        title = {
          Text(
            "JetNotes",
            color = MaterialTheme.colors.onPrimary
          )
        },
        navigationIcon = {
          IconButton(onClick = { onOpenNavigatinDrawer() }) {
            Icon(
              imageVector =  Icons.Filled.List ,
              contentDescription = "Drawer Button"
            )
          }
        }
      )
    },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          viewModel.createNewNoteClick()
          onNavigationToSaveNote()
        },
        contentColor = MaterialTheme.colors.background,
        content = {
          Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Note Button"
          )
        }
      )
    },
    content = {
      if(notes.isNotEmpty()) {
        NotesList(
          notes = notes,
          onNoteCheckedChange = {
            viewModel.onNoteCheckedChange(it)
          },
          onNoteClicked = {
            viewModel.onNoteClick(it)
            onNavigationToSaveNote()
          }
        )
      }
    }
  )
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
        onNoteCheckedChange = onNoteCheckedChange,
        isSelected = false
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