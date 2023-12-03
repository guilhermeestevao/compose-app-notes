package com.yourcompany.android.jetnotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.android.jetnotes.routing.Screen
import com.yourcompany.android.jetnotes.theme.JetNotesTheme
import com.yourcompany.android.jetnotes.theme.JetNotesThemeSettings

@Composable
fun AppDrawer(
  currentSreen: Screen,
  onScreenSelected: (Screen) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    AppDrawerHeader()
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
    ScreenNavigationButton(
      icon = Icons.Filled.Home,
      label = "Notes",
      isSelected = currentSreen == Screen.Notes,
      onClick = {
        onScreenSelected(Screen.Notes)
      }
    )
    ScreenNavigationButton(
      icon = Icons.Filled.Delete,
      label = "Trash",
      isSelected = currentSreen == Screen.Trash,
      onClick = {
        onScreenSelected(Screen.Trash)
      }
    )
    LightDarkThemeItem()
  }
}

@Composable
private fun AppDrawerHeader() {
  Row(
    modifier = Modifier.fillMaxWidth()
  ) {
    Image(
      imageVector = Icons.Filled.Menu,
      contentDescription = "Drawer app icon",
      colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
      modifier = Modifier.padding(16.dp)
    )
    Text(
      text = "Jet Notes",
      modifier = Modifier.align(Alignment.CenterVertically)
    )
  }
}

@Composable
private fun ScreenNavigationButton(
  icon: ImageVector,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val colors = MaterialTheme.colors
  val imageAlpha = if (isSelected) 1f else 0.6f
  val testColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)
  val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.12f) else colors.surface

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        start = 8.dp,
        end = 8.dp,
        top = 8.dp
      ),
    color = backgroundColor,
    shape = MaterialTheme.shapes.small
  ) {
    Row(
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .clickable(onClick = onClick)
        .fillMaxWidth()
        .padding(4.dp)
    ) {
      Image(
        imageVector = icon,
        contentDescription = "Screen navigation Button",
        colorFilter = ColorFilter.tint(testColor),
        alpha = imageAlpha
      )
      Spacer(Modifier.width(16.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.body2,
        color = testColor,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }

}

@Composable
private fun LightDarkThemeItem() {
  Row(Modifier.padding(8.dp)) {
    Text(
      text = "Trun on dark theme",
      style = MaterialTheme.typography.body2,
      color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
      modifier = Modifier
        .weight(1f)
        .padding(
          start = 8.dp,
          top = 8.dp,
          bottom = 8.dp,
          end = 8.dp
        )
        .align(Alignment.CenterVertically)
    )
    Switch(
      checked = JetNotesThemeSettings.isDarkThemeEnabled,
      onCheckedChange = {
        JetNotesThemeSettings.isDarkThemeEnabled = it
      },
      modifier = Modifier
        .padding(
          start = 8.dp,
          end = 8.dp
        )
        .align(Alignment.CenterVertically)
    )
  }
}

@Preview
@Composable
private fun AppDrawerPreview() {
  JetNotesTheme {
    AppDrawer(Screen.Notes, {})
  }
}

@Preview
@Composable
private fun AppDrawerHeaderPreview() {
  JetNotesTheme {
    AppDrawerHeader()
  }
}

@Preview
@Composable
private fun ScreenNavigationButtonPreview() {
  JetNotesTheme {
    ScreenNavigationButton(
      icon = Icons.Filled.Home,
      label = "Notes",
      isSelected = true,
      onClick = {}
    )
  }
}

@Preview
@Composable
private fun LightDarkThemeItemPreview() {
  JetNotesTheme {
    LightDarkThemeItem()
  }
}