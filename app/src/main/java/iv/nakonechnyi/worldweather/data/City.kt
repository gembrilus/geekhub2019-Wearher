package iv.nakonechnyi.worldweather.data

data class City(
    val name: String,
    val coord: Map<String, Double>,
    val country: String
)