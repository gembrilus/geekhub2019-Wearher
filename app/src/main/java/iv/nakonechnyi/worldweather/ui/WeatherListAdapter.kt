package iv.nakonechnyi.worldweather.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.data.WeatherConditions
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.ui.model.WeatherModel
import iv.nakonechnyi.worldweather.utils.getImageIdByFileName
import iv.nakonechnyi.worldweather.utils.getTemperatureSymbol
import iv.nakonechnyi.worldweather.utils.toDate
import kotlinx.android.synthetic.main.list_item_view.view.*

class WeatherListAdapter(private val model: WeatherModel) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    var listener: OnSimpleClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        with(LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)) {
            WeatherViewHolder(this)
        }

    override fun getItemCount(): Int = model.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        model.data.value?.let { holder.bind(it, it.list[position] )}
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(dailyWeatherHolder: DailyWeatherHolder, weatherConditions: WeatherConditions) =
            ViewBinder(dailyWeatherHolder, weatherConditions).bindAll()

        override fun onClick(p0: View?) {
            listener?.onClick(adapterPosition)
        }

        private inner class ViewBinder(
            private val dailyWeatherHolder: DailyWeatherHolder,
            private val weatherConditions: WeatherConditions) {

            private val ctx by lazy { itemView.context }
            private val shPref get() = ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            private val spHolder by lazy { SPHolder(ctx, shPref) }
            private val unit get() = getTemperatureSymbol(ctx, spHolder.units)
            private val imageId: Int get() = getImageIdByFileName(ctx, weatherConditions.weather[0].icon)

            fun bindAll(){
                with(itemView){
                    tv_city.text = with(dailyWeatherHolder){ "${city.name}, ${city.country}" }
                    tv_data.text = toDate(weatherConditions.dt)
                    image_weather.setImageResource(imageId)
                    temperature.text = context.getString(R.string.temperature_desc, weatherConditions.main.temp, unit)
                    pressure.text = context.getString(R.string.pressure_desc, weatherConditions.main.pressure)
                    humidity.text = context.getString(R.string.humidity_desc, weatherConditions.main.humidity)
                    main_description.text = weatherConditions.weather
                        .joinToString(
                            separator = ". ",
                            transform = {weather -> weather.description.capitalize() })
                }
            }
        }
    }

    interface OnSimpleClickListener{
        fun onClick(pos: Int)
    }

}