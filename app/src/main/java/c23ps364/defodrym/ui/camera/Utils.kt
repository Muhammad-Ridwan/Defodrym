package c23ps364.defodrym.ui.camera

import android.app.Application
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

object FileUtil {
    private const val FILE_FORMAT = "dd-MMM-yyyy"
    private val timeStamp: String = SimpleDateFormat(FILE_FORMAT, Locale.US).format(System.currentTimeMillis())

    fun createFile(application: Application): File {
        val media = application.externalMediaDirs.firstOrNull()?.let {
            File(it, "MyCamera").apply { mkdirs() }
        }
        val outputDirectory = if (media != null && media.exists()) media else application.filesDir
        return File(outputDirectory, "$timeStamp.jpg")
    }

}
