package iv.nakonechnyi.worldweather

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import iv.nakonechnyi.worldweather.etc.ON_CHANGE_SETTINGS_CODE
import iv.nakonechnyi.worldweather.etc.SETTINGS_CHANGED
import iv.nakonechnyi.worldweather.model.WeatherModel
import iv.nakonechnyi.worldweather.ui.ItemWeatherFragment
import iv.nakonechnyi.worldweather.ui.WeatherListAdapter
import iv.nakonechnyi.worldweather.ui.WeatherListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    AppCompatActivity(),
    WeatherListAdapter.OnSimpleClickListener {

    private val dualPane by lazy { item_weather_container?.isVisible ?: false }

    private val model by lazy { ViewModelProviders.of(this).get(WeatherModel::class.java) }

    private var mPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        with(transaction) {
            if (savedInstanceState == null) {
                add(R.id.main_fragment_container, WeatherListFragment.newInstance())
            }
            if (dualPane) {
                replace(
                    R.id.item_weather_container, ItemWeatherFragment.newInstance(mPosition)
                )
            }
            commit()
        }
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
            if(data.getBooleanExtra(SETTINGS_CHANGED, false)) model.fetchWeatherForecast()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(pos: Int) {
        mPosition = pos
        val fragment = ItemWeatherFragment.newInstance(pos)
        val transaction = supportFragmentManager.beginTransaction()

        with(transaction) {
            if (dualPane) {
                replace(R.id.item_weather_container, fragment)
            } else {
                replace(R.id.main_fragment_container, fragment)
                addToBackStack(null)
            }
            commit()
        }
    }
}