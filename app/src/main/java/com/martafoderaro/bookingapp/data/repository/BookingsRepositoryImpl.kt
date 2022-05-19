package com.martafoderaro.bookingapp.data.repository

import com.martafoderaro.bookingapp.core.CoroutineDispatcherProvider
import com.martafoderaro.bookingapp.data.datasources.DataSourceHelper
import com.martafoderaro.bookingapp.data.datasources.ResultWrapper
import com.martafoderaro.bookingapp.domain.datasource.DataSource
import com.martafoderaro.bookingapp.domain.model.Booking
import com.martafoderaro.bookingapp.domain.repository.BookingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookingsRepositoryImpl @Inject constructor(
    private val dataSource: DataSource,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val dataSourceHelper: DataSourceHelper,
): BookingRepository {
    override fun retrieveBookings(): Flow<ResultWrapper<List<Booking>>> = flow {
        emit(dataSourceHelper.safelyFetchResource {
            val data = dataSource.retrieveBookings()

            if (data == null) ResultWrapper.GenericError(code = -1, error = IllegalStateException("Data not found!"))

            data!!.map {
                with (it) {
                    Booking(
                        startsAt = starts_at,
                        endsAt = ends_at,
                        spaceName = space_name,
                        spaceTimezone = space_timezone,
                        spaceImage = space_image,
                    )
                }
            }
        })
    }.flowOn(dispatcherProvider.default)
}