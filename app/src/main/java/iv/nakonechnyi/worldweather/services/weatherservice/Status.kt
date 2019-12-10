package iv.nakonechnyi.worldweather.services.weatherservice

import java.io.Serializable

enum class Status: Serializable {
    START,
    UPDATE,
    NETWORK_ERROR,
    ON_RESPONSE_ERROR,
    FAILURE,
    STOP
}