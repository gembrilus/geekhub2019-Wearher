package iv.nakonechnyi.worldweather

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import iv.nakonechnyi.worldweather.etc.SETTINGS_CHANGED
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.prefs.Preferences

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

    private val langMap by lazy {
        mapOf(
            getString(R.string.default_language) to getString(R.string.english),
            getString(R.string.ru) to getString(R.string.russian),
            getString(R.string.ua) to getString(R.string.ukrainian)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val map = spHolder.map
        val _keywords = map[getString(R.string.keywords)]
        val _unit = map[getString(R.string.units)]
        val _lang = langMap[map[getString(R.string.lang)]]

        with(keywords) {
            setText(_keywords)
            setOnEditorActionListener { view, _, _ ->
                false
            }

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) = spHolder.setKeywords(p0.toString())
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
                spHolder.setUnits(unit)
            }
        }

        with(languages) {
            adapter = ArrayAdapter(
                this@SettingsActivity,
                android.R.layout.simple_list_item_1,
                langMap.values.toList()
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            forEachIndexed { i, view ->
                if ((view as TextView).text.toString() == _lang) languages.setSelection(i)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    val lang = when ((view as TextView).text) {
                        getString(R.string.russian) -> getString(R.string.ru)
                        getString(R.string.ukrainian) -> getString(R.string.ua)
                        else -> getString(R.string.default_language)
                    }
                    spHolder.setLanguage(lang)
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        setResult(Activity.RESULT_OK, Intent().putExtra(SETTINGS_CHANGED, true))
    }
}

