package iv.nakonechnyi.worldweather.etc

import android.content.Context
import android.content.SharedPreferences
import iv.nakonechnyi.worldweather.R

class SPHolder(private val context: Context, private val shPref: SharedPreferences) {

    val map: MutableMap<String, String>
        get() {
            val unit = shPref.getString(METRIC_SYSTEM_RESPONSE, "")
            val lang = shPref.getString(LANGUAGE_RESPONSE, context.getString(R.string.default_language))
            val keywords = shPref.getString(KEYWORDS_RESPONSE, context.getString(R.string.default_keywords))
            return when {
                unit == "" && lang == context.getString(R.string.default_language) -> {
                    mutableMapOf(context.getString(R.string.keywords) to keywords!!)
                }
                unit == "" -> {
                    mutableMapOf(
                        context.getString(R.string.keywords) to keywords!!,
                        context.getString(R.string.units) to unit)
                }
                else -> {
                    mutableMapOf(
                        context.getString(R.string.keywords) to keywords!!,
                        context.getString(R.string.lang) to lang!!,
                        context.getString(R.string.units) to unit!!
                    )
                }
            }
        }

    fun setUnits(value: String) {
        shPref.edit().putString(METRIC_SYSTEM_RESPONSE, value).apply()
        map[context.getString(R.string.units)] = value
    }

    fun setLanguage(value: String) {
        shPref.edit().putString(LANGUAGE_RESPONSE, value).apply()
        map[context.getString(R.string.lang)] = value
    }
    
    fun setKeywords(value: String) {
        shPref.edit().putString(KEYWORDS_RESPONSE, value).apply()
        map[context.getString(R.string.keywords)] = value
    }

}