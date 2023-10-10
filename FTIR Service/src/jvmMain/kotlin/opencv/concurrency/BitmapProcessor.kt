package opencv.concurrency

import androidx.compose.ui.graphics.ImageBitmap
import compose.toImageBitmap
import concurrency.BaseConcurrency
import kotlinx.coroutines.Dispatchers
import opencv.concurrency.AsyncMatReleaser.scheduleRelease
import opencv.internal.toByteArray
import org.opencv.core.Mat

// single channel + about same execution time preserves order; set as high as you want :)
private const val CONCURRENT_WORKERS = 3

class BitmapProcessor(private val onProcessed: (Pair<ImageBitmap, ImageBitmap>) -> Unit) {

    private val concurrency = object : BaseConcurrency<Pair<Mat, Mat>>(
        dispatcher = Dispatchers.Default,
        concurrentWorkers = CONCURRENT_WORKERS,
        workerAction = ::workerAction
    ) {}

    private fun workerAction(frames: Pair<Mat, Mat>) {
        val bitmap1 = frames.first.toByteArray().toImageBitmap()
        val bitmap2 = frames.second.toByteArray().toImageBitmap()
        scheduleRelease(frames.first)
        scheduleRelease(frames.second)
        onProcessed(bitmap1 to bitmap2)
    }

    fun scheduleFrames(frames: Pair<Mat, Mat>) {
        concurrency.addWorkElement(frames)
    }

}

