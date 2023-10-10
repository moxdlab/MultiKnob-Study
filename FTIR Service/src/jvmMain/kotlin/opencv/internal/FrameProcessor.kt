package opencv.internal

import opencv.concurrency.AsyncMatReleaser.scheduleRelease
import opencv.*
import org.opencv.core.Mat

val name = { "${System.currentTimeMillis()}.jpg" }

class FrameProcessor(
    private val onProcessed: (result: FrameProcessed, rawFrame: Mat) -> Unit,
    private val onCenterFound: (center: Circle) -> Unit,
) {

    private var capCircle: Circle? = null
    private lateinit var thresMask: Mask
    private lateinit var rotationMask: Mask

    fun onNewFrame(frame: Mat) {
        if (capCircle == null) {
            frame.findCapCircle()?.also {
                thresMask = frame.getTouchableAreaMask(it)
                rotationMask = frame.getRotationMask(it)
                onCenterFound(it)
                capCircle = it
            } ?: run { scheduleRelease(frame) }.apply { return }
        }

        val greyscaled = frame.greyscaleFrame()
        val rotationCircle = greyscaled.getRotationBlob(rotationMask)

        val blurred = greyscaled.blurFrame(BlurParam.General).also { scheduleRelease(greyscaled) }
        val preprocessed = blurred.drawBoundaryCircles(capCircle).also { scheduleRelease(blurred) }

        val thres = preprocessed.getThres(thresMask)
        val fingerKeypoints = thres.findFingerKeypoints()

        val keypointsImage = preprocessed.drawKeypoints(fingerKeypoints).also { scheduleRelease(preprocessed) }
        val finalImage = keypointsImage.drawCircle(rotationCircle).also { scheduleRelease(keypointsImage) }

        onProcessed(FrameProcessed(finalImage, thres, rotationCircle, fingerKeypoints.getCircleCount()), frame)
    }
}

data class FrameProcessed(
    val finaleImage: Mat,
    val thresImage: Mat,
    val rotationCircle: Circle?,
    val fingerCount: Int
)