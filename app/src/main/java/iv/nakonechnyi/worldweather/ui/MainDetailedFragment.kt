package iv.nakonechnyi.worldweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.etc.WEATHER_POS
import kotlinx.android.synthetic.main.fragment_main_detailed.view.*

class MainDetailedFragment : Fragment() {

    private var mPosition: Int = 0

    companion object{
        fun newInstance(pos: Int) = MainDetailedFragment().apply {
            arguments = Bundle().apply {
                putInt(WEATHER_POS, pos)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPosition = arguments?.getInt(WEATHER_POS) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_detailed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_fragment_container, ItemWeatherFragment.newInstance(mPosition))
            .commit()

        view.nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        when(it.itemId){
            R.id.navigation_home -> {
                transaction.replace(R.id.nav_fragment_container, ItemWeatherFragment.newInstance(mPosition)).commit()
                true
            }
            R.id.navigation_map -> {
                transaction.replace(R.id.nav_fragment_container, WeatherMapFragment()).commit()
                true
            }
            else -> false
        }
    }

}