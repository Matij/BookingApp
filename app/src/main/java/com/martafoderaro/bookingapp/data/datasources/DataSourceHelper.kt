package com.martafoderaro.bookingapp.data.datasources

import com.martafoderaro.bookingapp.core.CoroutineDispatcherProvider
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DataSourceHelper @Inject constructor(
    private val dispatchers: CoroutineDispatcherProvider,
) {
    suspend fun <T> safelyFetchResource(
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatchers.io) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (e: IOException) {
                ResultWrapper.NetworkError(e.message)
            } catch (e: HttpException) {
                val code = e.code()
                ResultWrapper.GenericError(code, e)
            } catch (e: IllegalStateException) {
                ResultWrapper.GenericError()
            }
        }
    }


}