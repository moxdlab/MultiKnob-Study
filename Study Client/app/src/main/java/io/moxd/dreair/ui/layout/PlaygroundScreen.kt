package io.moxd.dreair.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.moxd.dreair.ui.compose.Lane
import io.moxd.dreair.ui.compose.LifecycleAware
import io.moxd.dreair.ui.compose.SlowFastMixedRadioGroup
import io.moxd.dreair.viewmodel.PlaygroundViewModel

@Composable
fun PlaygroundScreen(viewModel: PlaygroundViewModel, navigateBack: () -> Unit) {
    val fingerCount by viewModel.fingerCount.observeAsState(0)
    val cursorPos by viewModel.cursorPos.observeAsState(
        initial = viewModel.initialCursorPos
    )
    PlaygroundScreen(
        goalPos = 0.7f,
        cursorPos = cursorPos,
        fingerCount = fingerCount,
        onClose = {
            viewModel.close()
            navigateBack()
        },
        onSlow = { viewModel.slow() },
        onFast = { viewModel.fast() },
        onMixed = { viewModel.mixed() },
    )

    LifecycleAware(
        onStart = { viewModel.startPlayground() },
        onStop = { viewModel.close() }
    )
}

@Composable
fun PlaygroundScreen(
    goalPos: Float,
    cursorPos: Float,
    fingerCount: Int,
    onClose: () -> Unit = {},
    onSlow: () -> Unit = {},
    onFast: () -> Unit = {},
    onMixed: () -> Unit = {},
) {
    Column() {
        SlowFastMixedRadioGroup(
            modifier = Modifier.padding(top = 16.dp),
            onSlowSelect = onSlow,
            onFastSelect = onFast,
            onMixedSelect = onMixed,
        )
        Text(
            text = "Finger count: $fingerCount",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Lane(goalPos = goalPos, cursorPos = cursorPos, modifier = Modifier.weight(1f))

        Button(onClick = { onClose() }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Exit")
        }
    }
}


@Preview(showSystemUi = true, device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Composable
fun PlaygroundScreenPreview() {
    PlaygroundScreen(0.3f, 0.7f, 3)
}