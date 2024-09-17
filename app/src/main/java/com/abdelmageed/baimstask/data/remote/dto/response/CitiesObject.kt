package com.abdelmageed.baimstask.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CitiesObject(

    @field:SerializedName("cities")
    val cities: List<CitiesItem?>? = null
)

data class CitiesItem(

    @field:SerializedName("cityNameAr")
    val cityNameAr: String? = null,

    @field:SerializedName("cityNameEn")
    val cityNameEn: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
) {
    override fun toString(): String {
        return cityNameEn ?: ""
    }
}
