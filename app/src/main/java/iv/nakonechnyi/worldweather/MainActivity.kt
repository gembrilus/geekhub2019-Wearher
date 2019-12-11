package iv.nakonechnyi.worldweather

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import iv.nakonechnyi.worldweather.etc.BROADCAST_ACTION_FILTER
import iv.nakonechnyi.worldweather.etc.ON_CHANGE_SETTINGS_CODE
import iv.nakonechnyi.worldweather.etc.SETTINGS_CHANGED
import iv.nakonechnyi.worldweather.services.weatherservice.ServiceReceiver
import iv.nakonechnyi.worldweather.services.weatherservice.WeatherService
import iv.nakonechnyi.worldweather.ui.MainDetailedFragment
import iv.nakonechnyi.worldweather.ui.WeatherListAdapter
import iv.nakonechnyi.worldweather.ui.WeatherListFragment
import iv.nakonechnyi.worldweather.ui.model.WeatherModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    AppCompatActivity(),
    WeatherListAdapter.OnSimpleClickListener {

    private val dualPane get() = item_weather_container != null

    private val model by lazy { ViewModelProviders.of(this).get(WeatherModel::class.java) }

    private var mPosition: Int = 0

    private val receiver by lazy { ServiceReceiver() }

    private val listFragment by lazy { WeatherListFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        with(transaction) {
            if (savedInstanceState == null) {
                add(R.id.main_fragment_container, listFragment)
            }
            if (dualPane) {
                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                replace(
                    R.id.item_weather_container, MainDetailedFragment.newInstance(mPosition)
                )
            }
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(BROADCAST_ACTION_FILTER))
        receiver.serviceStatusListener = listFragment
        startService(Intent(this, WeatherService::class.java))
        model.isNoNetworkConnection.observe(this, Observer {
            supportActionBar?.subtitle =
                if (it) getString(R.string.actionbar_subtilte_on_no_internet)
                else ""
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.settings -> {
            startActivityForResult(Intent(this, SettingsActivity::class.java), ON_CHANGE_SETTINGS_CODE)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        if(resultCode == Activity.RESULT_OK && requestCode == ON_CHANGE_SETTINGS_CODE){
            if(data.getBooleanExtra(SETTINGS_CHANGED, false))

                model.update()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(pos: Int) {
        mPosition = pos
        val fragment = MainDetailedFragment.newInstance(pos)
        val transaction = supportFragmentManager.beginTransaction()

        with(transaction) {
            if (dualPane) {
                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                replace(R.id.item_weather_container, fragment)
            } else {
                replace(R.id.main_fragment_container, fragment)
                addToBackStack(null)
            }
            commit()
        }
    }
}