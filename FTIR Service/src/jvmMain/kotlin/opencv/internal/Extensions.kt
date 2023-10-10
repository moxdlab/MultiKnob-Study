package opencv.internal

import opencv.concurrency.AsyncMatReleaser.scheduleRelease
import opencv.*
import org.opencv.core.*
import org.opencv.features2d.Features2d
import org.opencv.features2d.SimpleBlobDetector
import org.opencv.features2d.SimpleBlobDetector_Params
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

fun KeypointParam.toOpenCvParams(): SimpleBlobDetector_Params {

    data class TripleParam(val filterBy: Boolean?, val min: Float?, val max: Float?)

    fun BlobParam.toParams(): TripleParam {
        return when (this) {
            BlobParam.Default -> TripleParam(null, null, null)
            BlobParam.NoFilter -> TripleParam(false, null, null)
            is BlobParam.Filter -> TripleParam(true, this.min, this.max)
        }
    }

    return SimpleBlobDetector_Params().apply {
        _filterByColor = false

        @Suppress("DuplicatedCode")
        area.toParams().also { param ->
            param.filterBy?.let { _filterByArea = it }
            param.min?.let { _minArea = it }
            param.max?.let { _maxArea = it }
        }

        convexity.toParams().also { param ->
            param.filterBy?.let { _filterByConvexity = it }
            param.min?.let { _minConvexity = it }
            param.max?.let { _maxConvexity = it }
        }

        inertia.toParams().also { param ->
            param.filterBy?.let { _filterByInertia = it }
            param.min?.let { _minInertiaRatio = it }
            param.max?.let { _maxInertiaRatio = it }
        }

        circularity.toParams().also { param ->
            param.filterBy?.let { _filterByCircularity = it }
            param.min?.let { _minCircularity = it }
            param.max?.let { _maxCircularity = it }
        }
    }
}

fun Mat.greyscaleFrame(): GreyscaledMat {
    val dest = GreyscaledMat()
    Imgproc.cvtColor(this, dest, Imgproc.COLOR_BGR2GRAY)
    return dest
}

fun Mat.blurFrame(blurParam: BlurParam) = blurFrame(blurParam.ksize)

fun Mat.blurFrame(ksize: Double): BlurredMat { //TODO: inline?
    val dest = BlurredMat()
    Imgproc.blur(this, dest, Size(ksize, ksize))
    return dest
}

fun MatOfKeyPoint.getFirstCircle(): Circle? {
    return this.get(0, 0)?.let { k0 ->
        Circle(k0[0], k0[1], k0[2])
    }
}

fun Mat.findKeypoints(detector: SimpleBlobDetector): MatOfKeyPoint {
    val resultKeypoints = MatOfKeyPoint()
    detector.detect(this, resultKeypoints)
    return resultKeypoints
}

fun Mat.drawKeypoints(keypoints: MatOfKeyPoint): Mat {
    val dest = Mat()
    Features2d.drawKeypoints(
        this, keypoints, dest, Scalar(0.0, 0.0, 255.0), Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS
    )
    return dest
}

fun MatOfKeyPoint.getCircleCount(): Int = this.size().height.toInt()

fun Mask.applyTo(image: Mat) = Mat().apply {
    Core.bitwise_and(image, image, this, this@applyTo)
}

val freshKernel = { kernelSize: Int ->
    Mat(kernelSize, kernelSize, CvType.CV_8UC1, Scalar(1.0))
}

fun Mat.dilate(kernelSize: Int) = Mat().also {
    Imgproc.dilate(this, it, freshKernel(kernelSize), Point(-1.0, -1.0), 3)
}

fun Mat.erode(kernelSize: Int) = Mat().also {
    Imgproc.erode(this, it, freshKernel(kernelSize), Point(-1.0, -1.0), 3)
}

fun Mat.toByteArray(): ByteArray {
    val dest = MatOfByte()
    Imgcodecs.imencode(".png", this, dest)
    val byteArray = ByteArray((this.total() * this.channels()).toInt())
    dest.get(0, 0, byteArray)
    return byteArray.also {
        scheduleRelease(dest)
    }
}