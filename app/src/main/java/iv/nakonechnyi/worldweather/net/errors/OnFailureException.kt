package iv.nakonechnyi.worldweather.net.errors

interface OnFailureException {
    fun handle(t: Throwable)
}