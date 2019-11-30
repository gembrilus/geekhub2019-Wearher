package iv.nakonechnyi.worldweather.utils

import android.content.Context
import iv.nakonechnyi.worldweather.R
import java.text.SimpleDateFormat
import java.util.*

fun toDate(timeInMs: Long): String {
        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = timeInMs * 1000L
        val date = calendar.time
        return SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale.getDefault()).format(date)
}

fun getTemperatureSymbol(context: Context, unit: String) = when(unit){
        context.getString(R.string.metric_units) -> "°C"
        context.getString(R.string.imperial_units) -> "F"
        else -> "K"
}

fun getImageIdByFileName(ctx: Context, name: String): Int {
        val imageName = "i$name"
        val imageName2 = imageName.replace("[dn]".toRegex(), "dn")
        val resId: Int = ctx.resources.getIdentifier(imageName, "drawable", ctx.packageName)
        return if (resId != 0) resId
        else ctx.resources.getIdentifier(imageName2, "drawable", ctx.packageName)
}

fun getSpeedUnits(ctx: Context, unit:String) = when(unit){
        ctx.getString(R.string.imperial_units) -> ctx.getString(R.string.imperial_speed_unit)
        else -> ctx.getString(R.string.metric_speed_unit)
}

fun getHumanReadableWindDirection(ctx: Context, degrees: Int) = when(degrees){
        in 12..34 -> ctx.getString(R.string.NNE)
        in 35..56 -> ctx.getString(R.string.NE)
        in 57..78 -> ctx.getString(R.string.ENE)
        in 79..101 -> ctx.getString(R.string.E)
        in 102..123 -> ctx.getString(R.string.ESE)
        in 124..146 -> ctx.getString(R.string.SE)
        in 147..169 -> ctx.getString(R.string.SSE)
        in 170..191 -> ctx.getString(R.string.S)
        in 192..213 -> ctx.getString(R.string.SSW)
        in 214..236 -> ctx.getString(R.string.SW)
        in 237..258 -> ctx.getString(R.string.WSW)
        in 259..281 -> ctx.getString(R.string.W)
        in 282..303 -> ctx.getString(R.string.WNW)
        in 304..326 -> ctx.getString(R.string.NW)
        in 327..348 -> ctx.getString(R.string.NNW)
        else -> ctx.getString(R.string.N)

}