package ru.elocont.locationbeaconsandroid.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.elocont.locationbeaconsandroid.R
import ru.elocont.locationbeaconsandroid.model.response.CellLocation
import ru.elocont.locationbeaconsandroid.utils.getCurrentCellInfo

class LocationActivity : AppCompatActivity() {

    private lateinit var viewModel: LocationViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var mapView: WebView
    private lateinit var textLocation: TextView
    private lateinit var textAddress: TextView
    private lateinit var buttonFind: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        mapView = findViewById(R.id.mapView)
        textLocation = findViewById(R.id.text_location)
        textAddress = findViewById(R.id.text_address)
        buttonFind = findViewById(R.id.button_find)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestPermission()
        initViewModel()
        initLocationLiveData()
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        buttonFind.setOnClickListener(::onClickFindLocation)
        mapView.settings.javaScriptEnabled = true
        mapView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            LocationViewModelFactory()
        )[LocationViewModel::class.java]
    }

    private fun initLocationLiveData() {
        viewModel.locationLiveData.observe(
            this,
            Observer(::onStateChanged)
        )
    }

    private fun onClickFindLocation(view: View) {
        val popupMenu = PopupMenu(this, view)

        val allCellInfo = getCurrentCellInfo(this)
        allCellInfo.forEachIndexed { index, cellInfo ->
            popupMenu.menu.add(0, index, 0, "${cellInfo.radio}")
        }

        popupMenu.setOnMenuItemClickListener {
            viewModel.fetchLocation(allCellInfo[it.itemId])
            true
        }

        popupMenu.show()
    }

    private fun onStateChanged(state: State) {
        when (state) {
            is State.Loading -> {
                buttonFind.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                mapView.visibility = View.GONE
            }

            is State.Failed -> {
                buttonFind.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                mapView.visibility = View.GONE
                showToast(state.message)
            }

            is State.Success -> {
                buttonFind.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                mapView.visibility = View.VISIBLE
                showLocationInfo(state.response)
            }
        }
    }

    private fun showLocationInfo(cellLocation: CellLocation) {
        textLocation.text = getString(
            R.string.text_location_format,
            cellLocation.latitude,
            cellLocation.longitude
        )
        textAddress.text = cellLocation.address
        mapView.loadUrl(
            "https://www.google.com/maps/place/${cellLocation.latitude},${cellLocation.longitude}"
        )
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2000
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}