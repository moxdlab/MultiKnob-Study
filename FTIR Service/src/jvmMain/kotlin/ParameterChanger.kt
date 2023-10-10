import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import opencv.BlobParam
import opencv.FingertipBlobParams
import opencv.KeypointParam
import opencv.RotationBlobParams
import kotlin.reflect.KMutableProperty0

/*
This is the link between the CV and the Compose parameters.
Use this class to make changes being reflected in UI as well as in the actual CV model.
Remember an instance of this class within compose.
 */
class ParameterChanger {
    val fingertipBlobParams = BlobDetectionParameter(FingertipBlobParams).apply {
        obsAreaState.set(BlobParameter(true, 200f, 50_000f))
        obsCircularityState.set(BlobParameter(true, 0.2f, 50_000f))
    }

    val rotationBlobParams = BlobDetectionParameter(RotationBlobParams).apply {
        obsAreaState.set(BlobParameter(true, 700f, 1000f))
    }

    //TODO: other parameters
}

class BlobDetectionParameter(private val cvParam: KeypointParam) {
    private val actualAreaState: MutableState<BlobParameter> = mutableStateOf(BlobParameter())
    val obsAreaState = ObservableMutableState(actualAreaState) {
        set(it, cvParam::area)
    }

    private val actualCircularityState: MutableState<BlobParameter> = mutableStateOf(BlobParameter())
    val obsCircularityState = ObservableMutableState(actualCircularityState) {
        set(it, cvParam::circularity)
    }

    private val actualInertiaState: MutableState<BlobParameter> = mutableStateOf(BlobParameter())
    val obsInertiaState = ObservableMutableState(actualInertiaState) {
        set(it, cvParam::inertia)
    }

    private val actualConvexityState: MutableState<BlobParameter> = mutableStateOf(BlobParameter())
    val obsConvexityState = ObservableMutableState(actualConvexityState) {
        set(it, cvParam::convexity)
    }

    private fun set(value: BlobParameter, property: KMutableProperty0<BlobParam>) {
        if (value.isActive) property.set(BlobParam.Filter(value.min, value.max))
        else property.set(BlobParam.NoFilter)
        //TODO: UI does not reflect BlobParam.Default
    }
}

data class BlobParameter(val isActive: Boolean = false, val min: Float = 0f, val max: Float = 0f)

class ObservableMutableState<T>(private val _state: MutableState<T>, private val onChange: (new: T) -> Unit) {

    val state: State<T> = _state
    fun set(value: T) {
        onChange(value)
        _state.value = value
    }
}
