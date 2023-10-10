package io.moxd.dreair.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.moxd.dreair.ui.compose.LifecycleAware
import io.moxd.dreair.ui.compose.SessionProgressRow
import io.moxd.dreair.ui.compose.StudyEntryData
import io.moxd.dreair.ui.compose.fakeEntryData
import io.moxd.dreair.viewmodel.StudyEntryViewModel

@Composable
fun StudyEntryScreen(
    viewModel: StudyEntryViewModel,
    navigatePlayground: () -> Unit,
    navigateExecution: () -> Unit
) {
    val subjectId by viewModel.currentSubjectId.observeAsState()
    val completedSessions by viewModel.completedSessions.observeAsState()
    val studyData by viewModel.studyEntryData.observeAsState()
    StudyEntryScreen(
        subjectId = subjectId,
        sessionsCompleted = completedSessions,
        data = studyData,
        onPlayground = navigatePlayground,
        onGo = navigateExecution
    )
    LifecycleAware(
        onCreate = { viewModel.fetchAll() }
    )
}

@Composable
fun StudyEntryScreen(
    subjectId: Int?,
    sessionsCompleted: Int?,
    data: StudyEntryData?,
    onPlayground: () -> Unit,
    onGo: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        subjectId?.let {
            Text(
                "Hello Subject $it!",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(top = 16.dp),
            )
        }
        sessionsCompleted?.let {
            Text(
                "Sessions completed: $it/3",
                fontSize = 20.sp
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            data?.let {
                SessionProgressRow(it)
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Button(onClick = { onPlayground() }) {
                Text("Playground")
            }
            Button(onClick = { onGo() }) {
                Text("Go")
            }
        }

    }
}


@Preview(showSystemUi = true, device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Composable
fun StudyEntryScreenPreview() {
    StudyEntryScreen(0, 1, fakeEntryData, {}, {})
}
