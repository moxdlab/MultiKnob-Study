package compose.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RememberedTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    label: String
) {
    var text by remember { mutableStateOf(value) }
    TextField(
        text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = { Text(text = label) },
        modifier = Modifier.width(130.dp)
    )
}

@Preview
@Composable
fun RememberedTextFieldPreview() {
    RememberedTextField(200.toString(), {}, "min")
}