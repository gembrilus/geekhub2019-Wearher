package iv.nakonechnyi.worldweather.net.errors

interface OnResponseException {
    fun handle(errorCode: Int)
}