package io.moxd.dreair.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.moxd.dreair.R

@Preview
@Composable
fun SessionProgressRowPreview() {
    SessionProgressRow(fakeEntryData)
}

@Composable
fun SessionProgressRow(
    data: StudyEntryData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        SessionProgress(name = data.firstSpeed, completed = data.firstCompleted)
        SessionProgress(name = data.secondSpeed, completed = data.secondCompleted)
        SessionProgress(name = data.thirdSpeed, completed = data.thirdCompleted)
    }
}

val fakeEntryData = StudyEntryData(
    "Slow", "Mixed", "Fast", true, false, false
)

data class StudyEntryData(
    val firstSpeed: String, val secondSpeed: String, val thirdSpeed: String,
    val firstCompleted: Boolean, val secondCompleted: Boolean, val thirdCompleted: Boolean
)


@Preview
@Composable
fun SessionProgressPreview() {
    SessionProgress("Slow", completed = true)
}

@Composable
fun SessionProgress(name: String, completed: Boolean) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text("$name:", fontSize = 20.sp)

        val (drawable, color) =
            if (completed) R.drawable.check to Color.Green
            else R.drawable.cross to Color.Red
        Icon(
            painterResource(id = drawable),
            tint = color,
            contentDescription = null
        )
    }
}