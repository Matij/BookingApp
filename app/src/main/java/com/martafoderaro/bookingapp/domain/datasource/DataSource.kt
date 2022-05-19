package com.martafoderaro.bookingapp.domain.datasource

import com.martafoderaro.bookingapp.data.datasources.FileBookingResponse

interface DataSource {
    fun retrieveBookings(): List<FileBookingResponse>?
}