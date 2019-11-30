package iv.nakonechnyi.worldweather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.data.WeatherConditions
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.etc.WEATHER_POS
import iv.nakonechnyi.worldweather.model.WeatherModel
import iv.nakonechnyi.worldweather.utils.*
import kotlinx.android.synthetic.main.view_weather_item.view.*

class ItemWeatherFragment : Fragment(){

    private val model by lazy { ViewModelProviders.of(requireActivity()).get(WeatherModel::class.java) }
    private val dailyWeatherHolder by lazy { model.data.value }
    private lateinit var fragmentView: View
    private var mPosition: Int = 0

    companion object{
        fun newInstance(pos: Int) = ItemWeatherFragment().apply {
            arguments = Bundle().apply {
                putInt(WEATHER_POS, pos)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.view_weather_item, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPosition = arguments?.getInt(WEATHER_POS) ?: 0

        dailyWeatherHolder?.let {
            ViewBinder(it, it.list[mPosition]).bindAll()
        }
    }



    private inner class ViewBinder(
        private val dailyWeatherHolder: DailyWeatherHolder,
        private val weatherConditions: WeatherConditions
    ) {

        private val ctx by lazy { requireContext() }
        private val shPref get() = ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
        private val unit get() = getTemperatureSymbol(ctx, shPref.getString(ctx.getString(R.string.units), "")!!)
        private val imageId: Int get() = getImageIdByFileName(ctx, weatherConditions.weather[0].icon)

        @SuppressLint("DefaultLocale")
        fun bindAll(){
            with(fragmentView){
                with(dailyWeatherHolder) {
                    tv_data.text = toDate(list[mPosition].dt)
                    weather.text = weatherConditions.weather
                        .joinToString(
                            separator = ". ",
                            transform = {weather -> weather.description.capitalize() })
                    image_weather.setImageResource(imageId)
                    main_info.text = context.getString(
                        R.string.main_info_desc,
                        weatherConditions.main.temp,
                        unit,
                        weatherConditions.main.temp_min,
                        weatherConditions.main.temp_max,
                        weatherConditions.main.pressure,
                        weatherConditions.main.sea_level,
                        weatherConditions.main.grnd_level,
                        weatherConditions.main.humidity)
                    wind.text = weatherConditions.wind.run {
                        val speed = get("speed")
                        val deg = get("deg")?.toInt()
                        context.getString(R.string.wind_data, getSpeedUnits(context, unit), speed, deg, getHumanReadableWindDirection(context, deg!!))
                    }

                    clouds.text = weatherConditions.clouds?.get("all").toString()
                    rain.text = weatherConditions.rain?.get("3h").toString()
                    snow.text = weatherConditions.snow?.get("3h").toString()
                }
            }
        }
    }
}