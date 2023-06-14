package c23ps364.defodrym

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import c23ps364.defodrym.ml.ConvertedModel
import c23ps364.defodrym.ui.detail.DetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var selectImageButton: Button
    private lateinit var makePredictionButton: Button
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var bitmap: Bitmap
    private lateinit var cameraButton: Button
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var label: String

    companion object {
        const val CAMERA_RESULT = 200
    }

    private fun checkAndGetPermissions() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        } else {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectImageButton = findViewById(R.id.galery)
        makePredictionButton = findViewById(R.id.identify)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.name)
        cameraButton = findViewById(R.id.camera)

        checkAndGetPermissions()

        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        selectImageButton.setOnClickListener {
            Log.d("mssg", "Button clicked")
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 250)
        }

        makePredictionButton.setOnClickListener {
            if (::bitmap.isInitialized) {
                val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
                val model = ConvertedModel.newInstance(this)

                val argbBitmap = resized.copy(Bitmap.Config.ARGB_8888, true)

                val tbuffer = TensorImage(DataType.FLOAT32)
                tbuffer.load(argbBitmap)

                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                inputFeature0.loadBuffer(tbuffer.buffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                val max = getMax(outputFeature0.floatArray)
                label = labels[max]

                textView.text = label

                model.close()

                val file = File(cacheDir, "photo.jpg")
                try {
                    val outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("label", label)
                intent.putExtra("photoPath", file.absolutePath)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val capturedImage = data?.extras?.get("data") as? Bitmap
                if (capturedImage != null) {
                    lifecycleScope.launch(Dispatchers.Default) {
                        bitmap = capturedImage
                        withContext(Dispatchers.Main) {
                            imageView.setImageBitmap(bitmap)
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        cameraButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(cameraIntent)
            }
        }
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        animation.duration = 2000
        animation.repeatCount = 0
        selectImageButton.startAnimation(animation)
        makePredictionButton.startAnimation(animation)
        cameraButton.startAnimation(animation)
        imageView.startAnimation(animation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 250 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                imageView.setImageURI(uri)
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            }
        }
    }

    private fun getMax(arr: FloatArray): Int {
        var ind = 0
        var max = arr[0]

        for (i in 1 until arr.size) {
            if (arr[i] > max) {
                max = arr[i]
                ind = i
            }
        }
        return ind
    }
}