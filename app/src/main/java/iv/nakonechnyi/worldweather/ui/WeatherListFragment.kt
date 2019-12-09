package iv.nakonechnyi.worldweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.services.Status
import iv.nakonechnyi.worldweather.services.WeatherService
import iv.nakonechnyi.worldweather.ui.model.WeatherModel
import iv.nakonechnyi.worldweather.utils.showErrorMessage
import kotlinx.android.synthetic.main.fragment_weather_list.*
import kotlinx.android.synthetic.main.fragment_weather_list.view.*

class WeatherListFragment : Fragment(), WeatherService.ServiceStatusListener{

    private val model by lazy { ViewModelProviders.of(requireActivity()).get(WeatherModel::class.java) }
    private val clickListener by lazy { activity as WeatherListAdapter.OnSimpleClickListener }

    companion object{
        fun newInstance() = WeatherListFragment()
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
            model.refreshModel()
        }

        model.data.observe(this, Observer {
            mAdapter.notifyDataSetChanged()
            swipe.isRefreshing = false
        })
    }

    override fun start() {
        swipe.isRefreshing = true
    }

    override fun update() {
        model.refreshModel()
    }

    override fun stop() {
        swipe.isRefreshing = false
    }

    override fun onServiceError(status: Status) {
        when(status){
            Status.NETWORK_ERROR -> {}
            Status.FAILURE -> showErrorMessage(requireContext(), getString(R.string.unknown_error))
        }
    }

    override fun onServiceResponseError(code: Int) {
        when(code){
            400 -> showErrorMessage(requireContext(), getString(R.string.programm_error))
            404 -> showErrorMessage(requireContext(), getString(R.string.no_url_or_broken))
            500 -> showErrorMessage(requireContext(), getString(R.string.server_error_500))
            else -> showErrorMessage(requireContext(), getString(R.string.error))
        }
    }
}