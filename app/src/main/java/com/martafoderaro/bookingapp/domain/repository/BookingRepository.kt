package com.martafoderaro.bookingapp.domain.repository

import com.martafoderaro.bookingapp.data.datasources.ResultWrapper
import com.martafoderaro.bookingapp.domain.model.Booking
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun retrieveBookings(): Flow<ResultWrapper<List<Booking>>>
}