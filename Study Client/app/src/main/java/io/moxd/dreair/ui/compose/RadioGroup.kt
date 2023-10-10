package io.moxd.dreair.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SlowFastMixedRadioGroup(
    modifier: Modifier = Modifier,
    onSlowSelect: () -> Unit = {},
    onFastSelect: () -> Unit = {},
    onMixedSelect: () -> Unit = {},
) {
    val s = "Slow"
    val f = "Fast"
    val m = "Mixed"
    HorizontalRadioGroup(items = listOf(s, f, m), modifier) {
        when (it) {
            s -> onSlowSelect()
            f -> onFastSelect()
            m -> onMixedSelect()
        }
    }
}

@Preview
@Composable
fun HorizontalRadioGroupPreview() {
    HorizontalRadioGroup(listOf("a", "b", "c", "d"))
}

@Composable
fun HorizontalRadioGroup(
    items: List<String>,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit = { }
) {

    val selected = remember { mutableStateOf("") }

    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach {
            RadioButtonLabeled(text = it, selected.value == it, modifier = modifier.clickable {
                selected.value = it
                onSelect(it)
            })
        }
    }
}

@Composable
@Preview
fun RadioBPreview() {
    RadioButtonLabeled(text = "Test", selected = true)
}

@Composable
fun RadioButtonLabeled(text: String, selected: Boolean, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(2.dp))
        Text(text)
    }
}