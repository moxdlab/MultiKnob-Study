// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import opencv.CameraInput
import opencv.OpenCvHelper
import opencv.VideoInput

fun main() = application {

    val appState = remember { ApplicationState() }

    appState.finalFrameWindow?.let {
        AWindow(it, appState.finalFrame)
    }
    appState.thresFrameWindow?.let {
        AWindow(it, appState.thresFrame)
    }

    Window(onCloseRequest = ::exitApplication) {
        MainView(
            enableShowFinalFrameWindow = appState.finalFrameWindow == null,
            onShowFinalFrameWindow = { appState.showFinalFrameWindow() },
            enableShowThresFrameWindow = appState.thresFrameWindow == null,
            onShowThresFrameWindow = { appState.showThresFrameWindow() },
            onGo = {
                appState.openCvHelper.go(VideoInput("drea_short.mov"))
            }
        )
    }
}


@Preview
@Composable
fun MainViewPreview() {
    MainView(true, {}, true, {}, {})
}

@Composable
fun MainView(
    enableShowFinalFrameWindow: Boolean,
    onShowFinalFrameWindow: () -> Unit,
    enableShowThresFrameWindow: Boolean,
    onShowThresFrameWindow: () -> Unit,
    onGo: () -> Unit,
) {

    Column {
        Row {
            Button(
                onClick = onShowFinalFrameWindow,
                enabled = enableShowFinalFrameWindow
            ) {
                Text("final frame")
            }
            Button(
                onClick = onShowThresFrameWindow,
                enabled = enableShowThresFrameWindow
            ) {
                Text("thres frame")
            }
        }

        Button(onClick = onGo) {
            Text("Go")
        }
    }
}

class ApplicationState {

    var finalFrame by mutableStateOf<ImageBitmap?>(null)
    var thresFrame by mutableStateOf<ImageBitmap?>(null)

    val openCvHelper = OpenCvHelper(onFrames = { final, thres ->
        finalFrame = final
        thresFrame = thres
    })

    var finalFrameWindow by mutableStateOf<WindowState?>(null)
        private set
    var thresFrameWindow by mutableStateOf<WindowState?>(null)
        private set

    fun showFinalFrameWindow() {
        finalFrameWindow = WindowState("Final Frame") {
            finalFrameWindow = null
        }
    }

    fun showThresFrameWindow() {
        thresFrameWindow = WindowState("Thres Frame") {
            thresFrameWindow = null
        }
    }
}

class WindowState(
    var title: String,
    private val close: (WindowState) -> Unit
) {
    fun close() {
        close(this)
    }
}

@Composable
fun AWindow(state: WindowState, frameToShow: ImageBitmap?) {
    Window(onCloseRequest = state::close, title = state.title) {
        frameToShow?.let {
            Image(
                bitmap = it,
                contentDescription = state.title,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}