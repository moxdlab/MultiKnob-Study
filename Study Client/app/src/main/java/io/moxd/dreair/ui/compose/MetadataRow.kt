package io.moxd.dreair.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun MetadataRowPreview() {
    val fakeData = Metadata(18, 50, 20, "Slow")
    MetadataRow(fakeData)
}

@Composable
fun MetadataRow(
    metadata: Metadata,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BigText("Subject-ID: ${metadata.subjectId}")
        BigText("Session-ID: ${metadata.sessionId}")
        BigText("Task: ${metadata.taskNumber}")
        BigText("Case: ${metadata.speed}")
    }
}

@Preview
@Composable
fun BigTextPreview(){
    BigText(text = "This is how the text looks like.")
}

@Composable
fun BigText(text: String, modifier: Modifier = Modifier) {
    Text(text, modifier = modifier, fontSize = 18.sp)
}


data class Metadata(val subjectId: Int, val sessionId: Long, val taskNumber: Int, val speed: String)

val fakeMetaData = Metadata(18, 50, 20, "Slow")