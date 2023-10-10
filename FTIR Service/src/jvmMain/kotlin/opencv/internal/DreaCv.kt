package opencv.internal

import opencv.concurrency.AsyncMatReleaser.scheduleRelease
import opencv.*
import org.opencv.core.*
import org.opencv.features2d.SimpleBlobDetector
import org.opencv.imgproc.Imgproc


val white = Scalar(255.0, 255.0, 255.0)
val black = Scalar(0.0, 0.0, 0.0)

private fun Mat.toBlankMask() = Mask().apply {
    Core.inRange(this@toBlankMask, Scalar(256.0), Scalar(256.0), this)
}

fun Mat.getTouchableAreaMask(capCircle: Circle): Mask {
    val mask = toBlankMask()
    Imgproc.circle(mask, capCircle.center(), capCircle.radius.toInt() + MaskParam.outerPlus, white, -1, 8, 0)
    Imgproc.circle(mask, capCircle.center(), capCircle.radius.toInt() + MaskParam.innerPlus, black, -1, 8, 0)
    return mask
}

fun Mat.getRotationMask(capCircle: Circle): Mask {
    val mask = toBlankMask()
    Imgproc.circle(mask, capCircle.center(), capCircle.radius.toInt() + MaskParam.rotationCapPlus, white, -1, 8, 0)
    return mask
}

fun Mat.getThres(thresMask: Mask): Mat {
    val masked = thresMask.applyTo(this)
    Core.inRange(masked, Scalar(ThresParam.lowestWhite), Scalar(255.0), masked)
    Imgproc.morphologyEx(masked, masked, Imgproc.MORPH_OPEN, freshKernel(4))

    val morph = { origin: Mat, kernelsize: Int?, morph: Mat.(kernelSize: Int) -> Mat ->
        kernelsize?.let { origin.morph(it) } ?: origin
    }

    return if (ThresParam.dilateFirst) {
        morph(morph(masked, ThresParam.dilateKernelsize, Mat::dilate), ThresParam.erodeKernelsize, Mat::erode)
    } else {
        morph(morph(masked, ThresParam.erodeKernelsize, Mat::erode), ThresParam.dilateKernelsize, Mat::dilate)
    }

}
private val fingerDetector = SimpleBlobDetector.create(FingertipBlobParams.toOpenCvParams())
private val rotationDetector = SimpleBlobDetector.create(RotationBlobParams.toOpenCvParams())

fun Mat.findFingerKeypoints(): MatOfKeyPoint {
    return this.findKeypoints(fingerDetector)
}

fun GreyscaledMat.getRotationBlob(rotationMask: Mask): Circle? {
    val masked = rotationMask.applyTo(this)
    val blurred = masked.blurFrame(BlurParam.Rotation).also { scheduleRelease(masked) }
    val keypoints = blurred.findKeypoints(rotationDetector)
    return keypoints.getFirstCircle().also {
        scheduleRelease(blurred)
        scheduleRelease(keypoints)
    }
}

fun Mat.findCapCircle(): Circle? {
    val greyscaled = greyscaleFrame()
    val blurred = greyscaled.blurFrame(BlurParam.CapCircle).also { scheduleRelease(greyscaled) }
    val circles = Mat()
    val rows = size().height
    Imgproc.HoughCircles(
        blurred, circles, Imgproc.HOUGH_GRADIENT, 1.0, rows / 8, 100.0, 30.0, 80
    )

    return circles.get(0, 0)?.let {
        val firstCircle = circles.get(0, 0)
        val x = firstCircle[0]
        val y = firstCircle[1]
        val r = firstCircle[2]
        Circle(x, y, r)
    }.also {
        scheduleRelease(circles)
        scheduleRelease(blurred)
    }

}

fun Mat.drawCircle(circle: Circle?): Mat {
    if (circle == null) return this.clone()
    return this.clone().apply {
        Imgproc.circle(this, circle.center(), circle.radius.toInt(), white, 1)
    }
}

fun Mat.drawBoundaryCircles(circle: Circle?): Mat {
    if (circle == null) return this
    val dest = this.clone()
    Imgproc.circle(dest, circle.center(), (circle.radius + MaskParam.innerPlus).toInt(), white, 1)
    Imgproc.circle(dest, circle.center(), (circle.radius + MaskParam.outerPlus).toInt(), white, 1)
    return dest
}