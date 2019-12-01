package iv.nakonechnyi.worldweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.ui.model.WeatherModel
import kotlinx.android.synthetic.main.fragment_weather_list.*
import kotlinx.android.synthetic.main.fragment_weather_list.view.*
import java.util.*

class WeatherListFragment : Fragment(){

    private val model by lazy { ViewModelProviders.of(requireActivity()).get(WeatherModel::class.java) }
    private val clickListener by lazy { activity as WeatherListAdapter.OnSimpleClickListener }

    companion object{
        fun newInstance() = WeatherListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.fetchWeatherForecast()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_weather_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mAdapter = WeatherListAdapter(model).apply {
            listener = clickListener
        }

        with(view.list){
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        swipe.setOnRefreshListener {
            model.fetchWeatherForecast{swipe.isRefreshing = false}
        }

        model.data.observe(this, Observer {
            mAdapter.notifyDataSetChanged()
        })
    }
}