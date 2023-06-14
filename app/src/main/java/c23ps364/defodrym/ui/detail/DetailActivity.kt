package c23ps364.defodrym.ui.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import c23ps364.defodrym.R
import c23ps364.defodrym.ui.maps.MapsActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var textViewFoodName: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewIngredients: TextView
    private lateinit var textViewKalori: TextView
    private lateinit var textViewLemak: TextView
    private lateinit var textViewKarbohidrat: TextView
    private lateinit var textViewProtein: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val label = intent.getStringExtra("label")
        val photoPath = intent.getStringExtra("photoPath")
        textViewFoodName = findViewById(R.id.foodname)
        textViewDescription = findViewById(R.id.description)
        textViewIngredients = findViewById(R.id.ingredients)
        textViewKalori = findViewById(R.id.kalori)
        textViewLemak = findViewById(R.id.lemak)
        textViewKarbohidrat = findViewById(R.id.karbohidrat)
        textViewProtein = findViewById(R.id.protein)

        val photoImageView: ImageView = findViewById(R.id.imageView)
        val animator = ObjectAnimator.ofFloat(photoImageView, "rotation", 0f, 360f)
        animator.duration = 3000
        animator.repeatCount = 0
        animator.start()
        val photoFile = File(photoPath)
        if (photoFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            photoImageView.setImageBitmap(bitmap)
        }

        val mapsButton: Button = findViewById(R.id.maps)
        mapsButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("name", label)
            startActivity(intent)
        }

        val modifiedLabel = label?.replace(" ", "%20")
        val defodrymResId = resources.getIdentifier("defodrym1", "string", packageName)
        val url = getString(defodrymResId, modifiedLabel)
        FetchFoodDetailsTask().execute(url)
    }

    private inner class FetchFoodDetailsTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String? {
            val url = urls[0]
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                return response.body?.string()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result != null) {
                try {
                    val responseJson = JSONObject(result)
                    val status = responseJson.getString("status")
                    if (status == "success") {
                        val dataObject = responseJson.getJSONObject("data")
                        val dataArray = dataObject.getJSONArray("data")
                        if (dataArray.length() > 0) {
                            val foodObject = dataArray.getJSONObject(0)
                            val foodname = foodObject.getString("foodname")
                            val description = foodObject.getString("description")
                            val ingredients = foodObject.getString("ingredients")
                            val kalori = foodObject.getString("kalori")
                            val lemak = foodObject.getString("lemak")
                            val karbohidrat = foodObject.getString("karbohidrat")
                            val protein = foodObject.getString("protein")

                            animateTextViewChanges(textViewFoodName, foodname)
                            animateTextViewChanges(textViewDescription, description)
                            animateTextViewChanges(textViewIngredients, ingredients)
                            animateTextViewChanges(textViewKalori, kalori)
                            animateTextViewChanges(textViewLemak, lemak)
                            animateTextViewChanges(textViewKarbohidrat, karbohidrat)
                            animateTextViewChanges(textViewProtein, protein)

                            animateText(foodname, description, ingredients, kalori, lemak, karbohidrat, protein)
                        } else {
                            Log.d("FoodDetails", "No food found for the given foodname.")
                        }
                    } else {
                        Log.d("FoodDetails", "Failed to fetch food details. Status: $status")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Log.d("FoodDetails", "Failed to fetch food details.")
            }
        }
    }

    private fun animateTextViewChanges(textView: TextView, newText: String) {
        val animator = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f)
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                textView.text = newText
                animator.removeAllListeners()
            }
        })
        animator.start()
    }

    private fun animateText(foodname: String, description: String, Ingredients: String, kalori: String, lemak: String, karbohidrat: String, protein: String) {
        val textFoodName = foodname
        val textDescription = description
        val textIngredients = Ingredients
        val textKalori = kalori
        val textLemak = lemak
        val textKarbohidrat = karbohidrat
        val textProtein = protein

        var indexFoodName = 0
        var indexDescription = 0
        var indexIngredients = 0
        var indexKalori = 0
        var indexLemak = 0
        var indexKarbohidrat = 0
        var indexProtein = 0
        val delay: Long = 100

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (indexFoodName < textFoodName.length) {
                    textViewFoodName.text = textFoodName.substring(0, indexFoodName + 1)
                    indexFoodName++
                }
                if (indexDescription < textDescription.length) {
                    textViewDescription.text = textDescription.substring(0, indexDescription + 1)
                    indexDescription++
                }
                if (indexIngredients < textIngredients.length) {
                    textViewIngredients.text = textIngredients.substring(0, indexIngredients + 1)
                    indexIngredients++
                }
                if (indexKalori < textKalori.length) {
                    textViewKalori.text = textKalori.substring(0, indexKalori + 1)
                    indexKalori++
                }
                if (indexLemak < textLemak.length) {
                    textViewLemak.text = textLemak.substring(0, indexLemak + 1)
                    indexLemak++
                }
                if (indexKarbohidrat < textKarbohidrat.length) {
                    textViewKarbohidrat.text = textKarbohidrat.substring(0, indexKarbohidrat + 1)
                    indexKarbohidrat++
                }
                if (indexProtein < textProtein.length) {
                    textViewProtein.text = textProtein.substring(0, indexProtein + 1)
                    indexProtein++
                }
                if (indexFoodName < textFoodName.length || indexDescription < textDescription.length || indexIngredients < textIngredients.length || indexKalori < textKalori.length || indexLemak < textLemak.length || indexKarbohidrat < textKarbohidrat.length || indexProtein < textProtein.length) {
                    handler.postDelayed(this, delay)
                }
            }
        }, delay)
    }
}
