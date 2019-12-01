package iv.nakonechnyi.worldweather.ui

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class WeatherMapFragment : SupportMapFragment(), OnMapReadyCallback{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap?) {

    }
}