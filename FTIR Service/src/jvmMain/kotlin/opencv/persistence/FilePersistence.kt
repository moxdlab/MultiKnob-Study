package opencv.persistence

import opencv.concurrency.AsyncMatReleaser.scheduleRelease
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.io.File

class FilePersistence(private val path: String = "./imgs/") {

    init {
        File(path).mkdir()
    }

    fun saveFile(name: String, content: Mat) {
        val finalName = "$path$name.jpg"
        Imgcodecs.imwrite(finalName, content)
        scheduleRelease(content)
    }
}