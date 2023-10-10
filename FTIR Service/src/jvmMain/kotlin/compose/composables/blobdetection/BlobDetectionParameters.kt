package compose.composables.blobdetection

import BlobDetectionParameter
import BlobParameter
import ObservableMutableState
import ParameterChanger
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import opencv.FingertipBlobParams

@Composable
fun BlobDetectionParameters(blobParams: BlobDetectionParameter) {
    Column {
        blobParams.forEach { name, obsState ->
            val blobParam = obsState.state.value
            BlobParameterItem(
                name = name,
                isChecked = blobParam.isActive,
                onCheckChange = { obsState.set(blobParam.copy(isActive = it)) },
                min = blobParam.min,
                max = blobParam.max,
                onMinChange = { obsState.set(blobParam.copy(min = it)) },
                onMaxChange = { obsState.set(blobParam.copy(max = it)) },
            )
        }
    }
}

@Preview
@Composable
fun BlobDetectionParametersPreview() {
    val state = BlobDetectionParameter(FingertipBlobParams)
    BlobDetectionParameters(state)
}

@Composable
fun BlobDetectionParameter.forEach(
    action: @Composable (name: String, param: ObservableMutableState<BlobParameter>) -> Unit
) {
    action("area", obsAreaState)
    action("circularity", obsCircularityState)
    action("inertia", obsInertiaState)
    action("convexity", obsConvexityState)
}


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val paramChanger = remember { ParameterChanger() }
        BlobDetectionParameters(paramChanger.fingertipBlobParams)
    }
}