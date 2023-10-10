package compose.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NamedCheckbox(
    name: String,
    isChecked: Boolean,
    onCheckChange: (isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Row {
            val centerVertically = Modifier.align(Alignment.CenterVertically)
            Checkbox(isChecked, onCheckChange, modifier = centerVertically)
            Text(name, modifier = centerVertically.padding(end = 16.dp))
        }
    }
}

@Preview
@Composable
fun NamedCheckboxPreview() {
    NamedCheckbox(name = "area", isChecked = true, onCheckChange = { })
}
