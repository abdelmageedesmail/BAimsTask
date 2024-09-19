package com.abdelmageed.baimstask.domain.weather

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.google.gson.TypeAdapter

class WeatherTypeAdapter : TypeAdapter<WeatherType>() {

    override fun write(out: com.google.gson.stream.JsonWriter?, value: WeatherType?) {
        out?.beginObject()
        when (value) {
            is WeatherType.Clear -> {
                out?.name("type")?.value("clear")
                out?.name("weatherDesc")?.value(value.weatherDesc)
                out?.name("iconRes")?.value(value.iconRes)
            }

            is WeatherType.Rain -> {
                out?.name("type")?.value("Rain")
                out?.name("weatherDesc")?.value(value.weatherDesc)
                out?.name("iconRes")?.value(value.iconRes)
            }

            is WeatherType.Snow -> {
                out?.name("type")?.value("Snow")
                out?.name("weatherDesc")?.value(value.weatherDesc)
                out?.name("iconRes")?.value(value.iconRes)
            }

            is WeatherType.Cloud -> {
                out?.name("type")?.value("cloud")
                out?.name("weatherDesc")?.value(value.weatherDesc)
                out?.name("iconRes")?.value(value.iconRes)
            }

            is WeatherType.Thunderstorm -> {
                out?.name("type")?.value("thunderstorm")
                out?.name("weatherDesc")?.value(value.weatherDesc)
                out?.name("iconRes")?.value(value.iconRes)
            }

            else -> {
                out?.name("type")?.value("clear")
                out?.name("weatherDesc")?.value(value?.weatherDesc)
                out?.name("iconRes")?.value(value?.iconRes)
            }
        }
        out?.endObject()
    }

    override fun read(`in`: com.google.gson.stream.JsonReader?): WeatherType {
        var type: String? = null
        var weatherDesc: String? = null
        var iconRes: Int? = null
        `in`?.beginObject()
        while (`in`?.hasNext() == true) {
            when (`in`.nextName()) {
                "type" -> type = `in`?.nextString()
                "weatherDesc" -> weatherDesc = `in`.nextString()
                "iconRes" -> iconRes = `in`.nextInt()
            }
        }
        `in`?.endObject()
        return when (type) {
            "clear" -> WeatherType.Clear
            "Rain" -> WeatherType.Rain
            "Snow" -> WeatherType.Snow
            "cloud" -> WeatherType.Cloud
            "thunderstorm" -> WeatherType.Thunderstorm
            else -> WeatherType.Clear
        }
    }
}