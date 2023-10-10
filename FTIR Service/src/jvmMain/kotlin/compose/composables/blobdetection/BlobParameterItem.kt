package compose.composables.blobdetection

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.composables.NamedCheckbox
import compose.composables.RememberedTextField

@Composable
fun BlobParameterItem(
    name: String,
    isChecked: Boolean,
    onCheckChange: (isChecked: Boolean) -> Unit,
    min: Float,
    max: Float,
    onMinChange: (min: Float) -> Unit,
    onMaxChange: (max: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(8.dp).fillMaxWidth()) {
        NamedCheckbox(
            name,
            isChecked,
            onCheckChange,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            RememberedTextField(
                min.toString(),
                onValueChange = { onMinChange(it.toFloat()) },
                label = "min"
            )
            Spacer(modifier.width(8.dp))
            RememberedTextField(
                max.toString(),
                onValueChange = { onMaxChange(it.toFloat()) },
                label = "max"
            )
        }
    }
}

@Preview
@Composable
fun ParameterItemPreview() {
    BlobParameterItem(
        "area", true, {}, 200.0f, 50000.0f, {}, {}
    )
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        ParameterItemPreview()
    }
}