package iv.nakonechnyi.worldweather.utils

import android.content.Context
import android.widget.Toast

fun showErrorMessage(ctx: Context, message: String){
    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
}