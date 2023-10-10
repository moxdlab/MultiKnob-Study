package io.moxd.dreair.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Preview
@Composable
fun LanePreview() {
    Lane(cursorPos = 0.3f, goalPos = 0.7f)
}

@Composable
fun Lane(
    modifier: Modifier = Modifier,
    goalPos: Float, //TODO: to goalOffset
    cursorPos: Float, //TODO: to cursorOffset
    goalSize: Pair<Dp, Dp>? = null,
    cursorSize: Pair<Dp, Dp>? = null,
    onGoalWidthChanged: (width: Int) -> Unit = {},
    onCursorWidthChanged: (width: Int) -> Unit = {},
    onSizeChanged: (width: Int) -> Unit = {},
) {

    var goalWidth by remember { mutableStateOf(0) }
    var cursorWidth by remember { mutableStateOf(0) }

    var goalOffset by remember { mutableStateOf(0f) }
    var cursorOffset by remember { mutableStateOf(0f) }

    fun getOffset(childPos: Float, childWidth: Int, parentWidth: Int): Float {
        return when (childPos) {
            0f -> 0f
            1f -> (parentWidth - childWidth).toFloat()
            else -> parentWidth * childPos - (childWidth / 2)
        }
    }

    Box(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .onSizeChanged {
                onSizeChanged(it.width)
                goalOffset = getOffset(goalPos, goalWidth, it.width)
                cursorOffset = getOffset(cursorPos, cursorWidth, it.width)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Line(Modifier.fillMaxWidth())

        val goalBoxMod = goalSize?.let { Modifier.size(it.first, it.second) } ?: Modifier
        GoalBox(
            modifier = goalBoxMod
                .absoluteOffset {
                    IntOffset(goalOffset.roundToInt(), 0)
                }
                .onSizeChanged {
                    goalWidth = it.width
                    onGoalWidthChanged(goalWidth)
                }
        )

        val cursorMod = cursorSize?.let { Modifier.size(it.first, it.second) } ?: Modifier
        Cursor(
            modifier = cursorMod
                .absoluteOffset {
                    IntOffset(cursorOffset.roundToInt(), 0)
                }
                .onSizeChanged {
                    cursorWidth = it.width
                    onCursorWidthChanged(cursorWidth)
                }
        )
    }
}

@Preview
@Composable
fun GoalBox(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Color(0xFF33AC34))
            .size(15.dp, 70.dp)
    )
}

@Preview
@Composable
fun Cursor(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Color(0xFFA53535))
            .size(2.dp, 90.dp)
    )
}

@Preview
@Composable
fun Line(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Color(0xFF8A8A8A))
            .size(width = 100.dp, height = 2.dp)
    )
}
