package opencv.concurrency

import concurrency.BaseConcurrency
import kotlinx.coroutines.Dispatchers
import opencv.persistence.FilePersistence
import org.opencv.core.Mat

object FileQueue {

    private val filePersistence = FilePersistence()

    private val concurrency = object : BaseConcurrency<Pair<String, Mat>>(
        dispatcher = Dispatchers.IO,
        concurrentWorkers = 3, //TODO: tweak?
        workerAction = { (name, content) -> filePersistence.saveFile(name, content) }
    ) {}

    fun scheduleFile(fileName: String, fileContents: Mat) {
        concurrency.addWorkElement(fileName to fileContents)
    }

}
