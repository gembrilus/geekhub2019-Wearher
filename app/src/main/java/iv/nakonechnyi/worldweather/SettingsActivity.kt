package iv.nakonechnyi.worldweather

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import iv.nakonechnyi.worldweather.etc.SETTINGS_CHANGED
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.services.jobscheduler.SchedulerJobService
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity :
    AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedPreferences by lazy {
        getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    private val spHolder by lazy {
        SPHolder(
            this,
            sharedPreferences
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val _keywords = spHolder.keywords
        val _unit = spHolder.units
        val _isSchedule = spHolder.isSchedule

        with(keywords) {
            setText(_keywords)
            setOnEditorActionListener { _, _, _ ->
                false
            }

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?){ spHolder.keywords = p0.toString() }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }

        with(units) {
            forEach {
                it as RadioButton
                if (it.tag == _unit) it.isChecked = true
            }

            setOnCheckedChangeListener { _, i ->
                val unit = when (i) {
                    R.id.metric -> getString(R.string.metric_units)
                    R.id.imperial -> getString(R.string.imperial_units)
                    else -> ""
                }
                spHolder.units = unit
            }
        }

        with(scheduler_checker){
            isChecked = _isSchedule
            setOnCheckedChangeListener { _, isChecked ->
                spHolder.isSchedule = isChecked
                if (isChecked) SchedulerJobService.scheduleJob(this@SettingsActivity)
                else SchedulerJobService.cancelJob(this@SettingsActivity)
            }
        }

    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        setResult(Activity.RESULT_OK, Intent().putExtra(SETTINGS_CHANGED, true))
    }
}