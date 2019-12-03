package iv.nakonechnyi.worldweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.ui.model.WeatherModel
import kotlinx.android.synthetic.main.legend_map.view.*
import java.net.MalformedURLException
import java.net.URL


class WeatherMapFragment : SupportMapFragment(), OnMapReadyCallback {

    private val model by lazy {
        ViewModelProviders.of(requireActivity()).get(WeatherModel::class.java)
    }
    private var mMap: GoogleMap? = null
    private lateinit var tileOverlay: TileOverlay
    private lateinit var tileProvider: TileProvider
    private val mapLayers by lazy { requireContext().resources.getStringArray(R.array.mapLayers) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMapAsync(this)

    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutInflater.inflate(R.layout.legend_map, parent, false).apply {
            mapContainer.addView(super.onCreateView(layoutInflater, parent, savedInstanceState))
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.legend.apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, mapLayers)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?){}
                override fun onItemSelected(adapterView: AdapterView<*>?, itemView: View?, pos: Int, id: Long) {
                    val values = context.resources.getStringArray(R.array.mapLayers_values)
                    tileOverlay.remove()
                    model.mapLayer.value = values[pos]
                }
            }
        }

        model.mapLayer.observe(this, Observer {
            mMap?.let { it1 -> onMapReady(it1) }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_HYBRID
        }

        val lat = model.data.value?.city?.coord?.get("lat")
        val lon = model.data.value?.city?.coord?.get("lon")

        if (lat != null && lon != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 5f))
        }

        tileProvider = TileProvider(model.mapLayer.value)

        tileOverlay = mMap!!.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(tileProvider)
                .transparency(0.2f)
        )
    }

    private inner class TileProvider(private val layer: String?) : UrlTileProvider(256, 256) {
        override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
            val s = String.format(
                requireContext().resources.getString(R.string.baseUrlForecastMap,
                layer, zoom, x, y, getString(R.string.APPID))
            )
            return try {
                URL(s)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                URL("")
            }
        }
    }
}

