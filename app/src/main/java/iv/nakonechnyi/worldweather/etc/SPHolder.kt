package iv.nakonechnyi.worldweather.etc

import android.content.Context
import android.content.SharedPreferences
import iv.nakonechnyi.worldweather.R

class SPHolder(private val context: Context, private val shPref: SharedPreferences) {

    val map: MutableMap<String, String>
        get() = when (units) {
            "" -> mutableMapOf(context.getString(R.string.keywords) to keywords)
            else -> mutableMapOf(
                context.getString(R.string.keywords) to keywords,
                context.getString(R.string.units) to units
            )
        }

    var keywords: String
        get() = shPref.getString(KEYWORDS_RESPONSE, context.getString(R.string.default_keywords))!!
        set(value) {
            shPref.edit().putString(KEYWORDS_RESPONSE, value).apply()
            map[context.getString(R.string.keywords)] = value
        }

    var units: String
        get() = shPref.getString(METRIC_SYSTEM_RESPONSE, "")!!
        set(value) {
            shPref.edit().putString(METRIC_SYSTEM_RESPONSE, value).apply()
            map[context.getString(R.string.units)] = value
        }

    var isSchedule: Boolean
        get() = shPref.getBoolean(IS_SERVICE_SCHEDULED, false)
        set(value) = shPref.edit().putBoolean(IS_SERVICE_SCHEDULED, value).apply()

}