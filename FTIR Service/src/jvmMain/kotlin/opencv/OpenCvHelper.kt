package opencv

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import mqtt.MqttQueue
import nu.pattern.OpenCV
import opencv.concurrency.BitmapProcessor
import opencv.concurrency.FileQueue
import opencv.internal.FrameProcessed
import opencv.internal.FrameProcessor
import opencv.rotation.RotationHelper
import opencv.rotation.RotationResult
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.videoio.VideoCapture

class OpenCvHelper(
    onFrames: (finalFrame: ImageBitmap, thresFrame: ImageBitmap) -> Unit = { _, _ -> },
    onData: (rotation: RotationResult?, fingerCount: Int) -> Unit = { _, _ -> },
) {

    init {
        OpenCV.loadLocally()
    }

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private val scope = CoroutineScope(Dispatchers.Default)

    private val bitmapProcessor = BitmapProcessor(
        onProcessed = { (final, thres) -> onFrames(final, thres) }
    )

    private val rotationHelper = RotationHelper()

    private val frameProcessor = FrameProcessor(
        onProcessed = { processed, rawFrame ->
            val rotationRes = rotationHelper.feedCircle(processed.rotationCircle)
            val mqttData = processed.toMqttData(rotationRes)
            MqttQueue.scheduleMqtt(mqttData)
            bitmapProcessor.scheduleFrames(processed.finaleImage to processed.thresImage)
            FileQueue.scheduleFile(mqttData, rawFrame)
            onData(rotationRes, processed.fingerCount)
        }, onCenterFound = { center ->
            rotationHelper.provideCenter(center)
        }
    )

    fun go(input: Input) {
        job = scope.launch {
            val frameFlow = frameFlow(input, this)
            frameFlow.collect {
                processFrame(it)
            }
        }
    }

    private fun frameFlow(input: Input, scope: CoroutineScope): Flow<Mat> {
        return when (input) {
            is VideoInput -> frameFlow(VideoCapture(input.filePath), scope)
            is CameraInput -> frameFlow(VideoCapture(input.cameraId), scope)
            is FrameInput -> frameFlow(Imgcodecs.imread(input.filePath))
        }
    }

    private fun frameFlow(frame: Mat): Flow<Mat> {
        return flowOf(frame)
    }

    private fun frameFlow(input: VideoCapture, scope: CoroutineScope): Flow<Mat> {
        val frame = Mat()
        return flow {
            while (input.read(frame) && scope.isActive) {
                val clone = frame.clone()
                emit(clone)
            }
        }.cancellable().flowOn(Dispatchers.IO)
    }

    private fun processFrame(frame: Mat) {
        frameProcessor.onNewFrame(frame)
    }

}

sealed class Input
data class VideoInput(val filePath: String) : Input()
data class CameraInput(val cameraId: Int) : Input()
data class FrameInput(val filePath: String) : Input()

fun FrameProcessed.toMqttData(rotationResult: RotationResult?): String {
    return listOf(
        System.currentTimeMillis().toString(),
        fingerCount.toString(),
        rotationResult?.rotation,
        rotationResult?.rotationSum,
    ).joinToString(",")
}