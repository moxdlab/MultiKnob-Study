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
import io.moxd.dreair.viewmodel.COUNTDOWN_DURATION
import io.moxd.dreair.viewmodel.CountdownViewModel

@Composable
fun CountdownScreen(viewModel: CountdownViewModel, navigateExecution: () -> Unit) {
    val timeLeftState by viewModel.timeLeft.observeAsState(COUNTDOWN_DURATION)
    viewModel.navigateToExecution = navigateExecution
    CountdownScreen(timeLeft = timeLeftState)
    LifecycleAware(
        onCreate = { viewModel.startCountdown() }
    )
}


@Preview(showSystemUi = true, device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Composable
fun CountdownScreenPreview() {
    CountdownScreen(timeLeft = COUNTDOWN_DURATION)
}

@Composable
fun CountdownScreen(timeLeft: Int) {

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Get ready! Next task in",
            fontSize = 20.sp,

            )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = timeLeft.toString(),
            fontSize = 70.sp,
        )
    }

}