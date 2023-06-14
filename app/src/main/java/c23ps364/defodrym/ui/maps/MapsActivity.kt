package c23ps364.defodrym.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.GeolocationPermissions
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import c23ps364.defodrym.MainActivity
import c23ps364.defodrym.R

class MapsActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var searchUrl: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        hideSystemUI()

        webView = findViewById(R.id.webView)

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setGeolocationEnabled(true)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
            }
        }

        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        webView.addJavascriptInterface(NavigationInterface(), "Android")

        val name = intent.getStringExtra("name")
        if (name != null) {
            webView.loadUrl(
                "javascript: function getLocation() {" +
                        "    if (navigator.geolocation) {" +
                        "        navigator.geolocation.getCurrentPosition(showPosition);" +
                        "    } else {" +
                        "        console.error('Geolocation is not supported by this browser.');" +
                        "    }" +
                        "}" +
                        "function showPosition(position) {" +
                        "    var lat = position.coords.latitude;" +
                        "    var lng = position.coords.longitude;" +
                        "    var radius = 0.00000001;)" +
                        "    var url = 'https://www.google.com/maps/search/?api=1&query=${Uri.encode(name)}&ll=' + lat + ',' + lng + '&radius=' + radius;" +
                        "    window.location.href = url;" +
                        "}" +
                        "getLocation();"
            )
            searchUrl = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(name)}&ll=' + lat + ',' + lng + '&radius=0.00000001"+ "getLocation();"
            webView.loadUrl(searchUrl)
        } else {
            webView.loadUrl(
                "javascript: function getLocation() {" +
                        "    if (navigator.geolocation) {" +
                        "        navigator.geolocation.getCurrentPosition(showPosition);" +
                        "    } else {" +
                        "        console.error('Geolocation is not supported by this browser.');" +
                        "    }" +
                        "}" +
                        "function showPosition(position) {" +
                        "    var lat = position.coords.latitude;" +
                        "    var lng = position.coords.longitude;" +
                        "    var radius = 0.00000001;)" +
                        "    var url = 'https://www.google.com/maps/search/?api=1&query=${Uri.encode(name)}&ll=' + lat + ',' + lng + '&radius=' + radius;" +
                        "    window.location.href = url;" +
                        "}" +
                        "getLocation();"
            )
            searchUrl = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(name)}&radius=0.00000001"+ "getLocation();"
            webView.loadUrl(searchUrl)
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val searchButton: Button = findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            webView.loadUrl(searchUrl)
        }
        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    inner class NavigationInterface {
        @JavascriptInterface
        fun startNavigation(latitude: Double, longitude: Double) {
            val uri = "google.navigation:q=$latitude,$longitude"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
            }
        }
    }
}
