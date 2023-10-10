package io.moxd.dreair.ui.layout

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.moxd.dreair.ui.compose.*
import io.moxd.dreair.viewmodel.StudyExecutionViewModel

@Composable
fun StudyExecutionScreen(
    viewModel: StudyExecutionViewModel,
    navigateEntry: () -> Unit,
    navigateCountdown: () -> Unit
) {
    val stateCursorPos by viewModel.cursorPos.observeAsState(-1f)
    val stateGoalPos by viewModel.goalPos.observeAsState(-1f)
    val metadata by viewModel.metadata.observeAsState()
    val context = LocalContext.current

    viewModel.navigateToCountdown = navigateCountdown
    viewModel.navigateToEntry = navigateEntry

    StudyExecutionScreen(
        goalPos = stateGoalPos,
        cursorPos = stateCursorPos,
        onCursorWidthChanged = { cWidth -> viewModel.cursorWidth = cWidth },
        onGoalWidthChanged = { gWidth -> viewModel.goalWidth = gWidth },
        onStopButtonClick = {
            Toast.makeText(context, "Long press to abort", Toast.LENGTH_SHORT).show()
        },
        onStopButtonClickLong = { viewModel.abort() },
        onSizeChanged = { size -> viewModel.laneWidth = size },
        metadata = metadata,
    )

    LifecycleAware(
        onCreate = { viewModel.nextTask() }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyExecutionScreen(
    goalPos: Float,
    cursorPos: Float,
    onCursorWidthChanged: (Int) -> Unit = {},
    onGoalWidthChanged: (Int) -> Unit = {},
    onSizeChanged: (Int) -> Unit = {},
    onStopButtonClick: () -> Unit = {},
    onStopButtonClickLong: () -> Unit = {},
    metadata: Metadata?
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        metadata?.let {
            MetadataRow(
                metadata = metadata,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Lane(
            modifier = Modifier.weight(1f),
            goalPos = goalPos,
            cursorPos = cursorPos,
            onCursorWidthChanged = onCursorWidthChanged,
            onGoalWidthChanged = onGoalWidthChanged,
            onSizeChanged = onSizeChanged
        )

        Box(
            Modifier
                .align(Alignment.Start)
                .padding(8.dp)
                .combinedClickable(
                    onClick = onStopButtonClick,
                    onLongClick = onStopButtonClickLong
                )
        ) {
            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = "Abort"
            )
        }
    }
}

@Preview(showSystemUi = true, device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Composable
fun StudyExecutionScreenPreview() {
    StudyExecutionScreen(
        goalPos = 0.3f,
        cursorPos = 0.7f,
        metadata = fakeMetaData
    )
}