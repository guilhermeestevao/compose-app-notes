package com.yourcompany.android.jetnotes.ui.screens

import android.annotation.SuppressLint
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.android.jetnotes.R
import com.yourcompany.android.jetnotes.domain.model.ColorModel
import com.yourcompany.android.jetnotes.domain.model.NEW_NOTE_ID
import com.yourcompany.android.jetnotes.domain.model.NoteModel
import com.yourcompany.android.jetnotes.ui.components.NoteColor
import com.yourcompany.android.jetnotes.util.fromHex
import com.yourcompany.android.jetnotes.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SaveNoteScreen(
  viewModel: MainViewModel,
  onNavigateBack:() -> Unit = {}
) {

  val noteEntry  by viewModel.noteEntry.observeAsState(NoteModel())
  val colors by viewModel.colors.observeAsState(listOf())
  val bottonDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()
  val moveToTrashDialogShowState = rememberSaveable {
    mutableStateOf(false)
  }

  Scaffold(
    topBar = {
      val isEditingMode = noteEntry.id != NEW_NOTE_ID

      SaveNoteTopAppBar(
        isEditingMode = isEditingMode,
        onBackClick = { onNavigateBack() },
        onSaveNodeClick = {
          viewModel.saveNote(noteEntry)
          onNavigateBack()
        },
        onOpenColorPickerClick = {
          coroutineScope.launch {
            bottonDrawerState.open()
          }
        },
        onDeleteNodeClick = {
          moveToTrashDialogShowState.value = true
        }
      )
    },
    content = {
      BottomDrawer(
        drawerState = bottonDrawerState,
        drawerContent = {
          ColorPicker(
            colors = colors,
            onColorSelect = { newColor ->
              val newNoteEntry = noteEntry.copy(color = newColor)
              viewModel.onNoteEntryChange(newNoteEntry)
              coroutineScope.launch {
                bottonDrawerState.close()
              }
            }
          )
        }
      ) {
        SaveNoteContent(
          note = noteEntry,
          onNoteChange = { updatedNoteEntry ->
            viewModel.onNoteEntryChange(updatedNoteEntry)
          }
        )
      }
      if(moveToTrashDialogShowState.value) {
        AlertDialog(
          onDismissRequest = {
            moveToTrashDialogShowState.value = false
          },
          title = {
            Text(text = "Move the note to the trash?")
          },
          text = {
            Text(text = "Are you sure you want to move this note to the trash?")
          },
          confirmButton = {
            TextButton(
              onClick = {
                viewModel.moveNoteToTrash(noteEntry)
                onNavigateBack()
              }
            ) {
              Text("Confirm")
            }
          },
          dismissButton = {
            TextButton(
              onClick = {
                moveToTrashDialogShowState.value = false
              }
            ) {
              Text("Dismiss")
            }
          }
        )
      }
    }
  )
}

@Composable
fun ColorItem(
  color: ColorModel,
  onColorSelect: (ColorModel) -> Unit
){
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        onClick = {
          onColorSelect(color)
        }
      )
  ) {
    NoteColor(
      modifier = Modifier
        .padding(16.dp),
      color = Color.fromHex(color.hex),
      size = 80.dp,
      border = 2.dp
    )
    Text(
      text = color.name,
      fontSize = 27.sp,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .align(Alignment.CenterVertically)
    )
  }
}

@Composable
private fun ColorPicker(
  colors: List<ColorModel>,
  onColorSelect: (ColorModel) -> Unit
) {
  Column(Modifier.fillMaxWidth()) {
    Text(
      text = "Color picker",
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(8.dp)
    )
    LazyColumn {
      items(colors.size) {itemIndex ->
        val color = colors[itemIndex]
        ColorItem(
          color = color,
          onColorSelect = onColorSelect
        )
      }
    }
  }
}

@Composable
private fun SaveNoteTopAppBar(
  isEditingMode: Boolean,
  onBackClick: () -> Unit,
  onSaveNodeClick: () -> Unit,
  onOpenColorPickerClick: () -> Unit,
  onDeleteNodeClick: () -> Unit
){
  TopAppBar(
    title = {
      Text(
        text = "Save Note",
        color = MaterialTheme.colors.onPrimary
      )
    },
    navigationIcon = {
      IconButton(onClick = onBackClick) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Save Note Button Back",
          tint = MaterialTheme.colors.onPrimary
        )
      }
    },
    actions = {
      IconButton(onClick = onSaveNodeClick) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Save Note",
          tint = MaterialTheme.colors.onPrimary
        )
      }
      IconButton(onClick = onOpenColorPickerClick) {
        Icon(
          painter = painterResource(id = R.drawable.ic_baseline_color_lens_24),
          contentDescription = "Open Color Picker Button",
          tint = MaterialTheme.colors.onPrimary
        )
      }
      if(isEditingMode) {
        IconButton(onClick = onDeleteNodeClick) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Note Button",
            tint = MaterialTheme.colors.onPrimary
          )
        }
      }
    }
  )
}

@Composable
private fun SaveNoteContent(
  note: NoteModel,
  onNoteChange: (NoteModel) -> Unit
) {
  Column(Modifier.fillMaxSize()) {
    ContentTextField(
      label = "Title",
      text = note.title,
      onTextChange = { newTitle ->
        onNoteChange(note.copy(title = newTitle))
      }
    )
    ContentTextField(
      modifier = Modifier
        .heightIn(max = 240.dp)
        .padding(top = 16.dp),
      label = "Body",
      text = note.content,
      onTextChange = { newContent ->
        onNoteChange(note.copy(content = newContent))
      }
    )
    val canBeCheckedOff = note.isCheckedOff != null
    NoteCheckOption(
      isChecked = canBeCheckedOff,
      onCheckedChange = { canBeCheckedOffNewValue ->
        val isCheckedOff = if(canBeCheckedOffNewValue) false else null
        onNoteChange(note.copy(isCheckedOff = isCheckedOff))
      }
    )
    PickedColor(color = note.color)
  }
}
@Composable
private fun ContentTextField(
  modifier: Modifier = Modifier,
  label: String,
  text: String,
  onTextChange: (String) -> Unit
) {
  TextField(
    value = text,
    onValueChange = onTextChange,
    label = { Text(label) },
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    colors = TextFieldDefaults.textFieldColors(
      backgroundColor = MaterialTheme.colors.surface
    )
  )
}

@Composable
private fun NoteCheckOption(
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .padding(8.dp)
      .padding(top = 16.dp)
  ) {
    Text(
      text = "Can note be checked off?",
      modifier = Modifier
        .weight(1f)
        .align(Alignment.CenterVertically)
    )
    Switch(
      checked = isChecked,
      onCheckedChange = onCheckedChange,
      modifier = Modifier
        .padding(start = 8.dp)
    )
  }
}

@Composable
private fun PickedColor(color: ColorModel){
  Row(
    Modifier
      .padding(8.dp)
      .padding(top = 16.dp)
  ) {
    Text(
      text = "Picked Color",
      modifier = Modifier
        .weight(1f)
        .align(Alignment.CenterVertically)
    )
    NoteColor(
      color = Color.fromHex(color.hex),
      size = 40.dp,
      border = 1.dp,
      modifier = Modifier.padding(4.dp)
    )
  }
}

@Preview
@Composable
private fun SaveNoteTopAppBarPreview(){
  SaveNoteTopAppBar(
    isEditingMode = true,
    onBackClick = {},
    onSaveNodeClick = {},
    onOpenColorPickerClick = {},
    onDeleteNodeClick = {}
  )
}

@Preview
@Composable
private fun SaveNoteContentPreview(){
  SaveNoteContent(
    note = NoteModel(
      title = "Title 1",
      content = "Content 1",
    ),
    onNoteChange = {}
  )
}

@Preview
@Composable
private fun ColorItemPreview() {
  ColorItem(
    color = ColorModel.DEFAULT,
    onColorSelect = {}
  )
}

@Preview
@Composable
private fun ColorPickerPreview(){
  ColorPicker(
    colors = listOf(
      ColorModel.DEFAULT,
      ColorModel.DEFAULT,
      ColorModel.DEFAULT
    ),
    onColorSelect = {}
  )
}
@Preview
@Composable
private fun PickedColorPreview(){
  PickedColor(ColorModel.DEFAULT)
}

@Preview
@Composable
private fun NoteCheckOptionPreview() {
  NoteCheckOption(false, {})
}

@Preview
@Composable
private fun ContentTextFieldPreview(){
  ContentTextField(
    label = "Title",
    text = "",
    onTextChange = {}
  )
}

