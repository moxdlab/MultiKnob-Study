package io.moxd.dreair.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LaneWithButtonsPreview() {
    LaneWithButtons(0.7f)
}

@Composable
fun LaneWithButtons(goalPos: Float) {

    Column {
        var cursorPos by remember { mutableStateOf(0.3f) }

        Lane(
            goalPos = goalPos,
            cursorPos = cursorPos,
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                cursorPos -= 0.01f
            }) {
                Text("- 0.01f")
            }
            Box(Modifier.size(20.dp, 0.dp))
            Button(onClick = {
                cursorPos += 0.01f
            }) {
                Text("+ 0.01f")
            }
        }

    }
}