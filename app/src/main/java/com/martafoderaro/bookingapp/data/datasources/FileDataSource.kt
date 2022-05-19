package com.martafoderaro.bookingapp.data.datasources

import android.content.res.AssetManager
import com.martafoderaro.bookingapp.domain.datasource.DataSource
import com.martafoderaro.bookingapp.util.BOOKINGS_RESPONSE_FILE_NAME
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
import java.lang.ClassCastException
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class FileBookingResponse(
    val starts_at: String,
    val ends_at: String,
    val space_name: String,
    val space_timezone: String,
    val space_image: String,
)

class FileDataSource @Inject constructor(
    private val assetManager: AssetManager,
    private val moshi: Moshi,
): DataSource {
    override fun retrieveBookings() = fetchDataFromFile()

    private fun fetchDataFromFile(): List<FileBookingResponse>? {
        val bookingResponse = assetManager.open(BOOKINGS_RESPONSE_FILE_NAME).bufferedReader().use { it.readText() }
        return try {
            moshi.parseList(bookingResponse)
        } catch (e: JsonDataException) {
            null
        } catch (e: IOException) {
            null
        } catch (e: ClassCastException) {
            null
        }
    }
}

inline fun <reified T> Moshi.parseList(jsonString: String): List<T>? {
    return adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java)).fromJson(jsonString)
}