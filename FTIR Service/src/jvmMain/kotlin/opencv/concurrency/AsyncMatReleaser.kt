package opencv.concurrency

import concurrency.BaseConcurrency
import kotlinx.coroutines.Dispatchers
import org.opencv.core.Mat

private const val CONCURRENT_WORKERS = 1

object AsyncMatReleaser {

    private val concurrency = object : BaseConcurrency<Mat>(
        dispatcher = Dispatchers.Default,
        concurrentWorkers = CONCURRENT_WORKERS,
        workerAction = Mat::release
    ) {}

    fun scheduleRelease(mat: Mat) {
        //concurrency.addWorkElement(mat)
    }
}

