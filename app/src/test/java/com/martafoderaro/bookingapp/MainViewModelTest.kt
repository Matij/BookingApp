package com.martafoderaro.bookingapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.martafoderaro.bookingapp.core.CoroutineDispatcherProvider
import com.martafoderaro.bookingapp.data.datasources.ResultWrapper
import com.martafoderaro.bookingapp.domain.model.Booking
import com.martafoderaro.bookingapp.domain.repository.BookingRepository
import com.martafoderaro.bookingapp.ui.MainScreenState
import com.martafoderaro.bookingapp.ui.MainViewModel
import com.martafoderaro.bookingapp.ui.adapter.Event
import com.martafoderaro.bookingapp.util.ISO_DATE_TIME_COLON_DELIMITER
import com.martafoderaro.bookingapp.util.ISO_DATE_TIME_T_DELIMITER
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.Month
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

class MainViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val bookingRepository: BookingRepository = mockk()
    private val dispatcher = Dispatchers.Unconfined
    private val coroutineDispatchers = CoroutineDispatcherProvider(
        io = dispatcher, main = dispatcher, default = dispatcher
    )

    private val testId = UUID.randomUUID().toString()
    private val testName = "test space name"
    private val testTimezone = "Europe/London"
    private val testUrl = "http://url.com/image.png"
    private val testISODateTime = "2022-03-10T09:00:00.000Z"
    private val testLocalDate = LocalDate.of(2022, Month.MARCH, 10)
    private val testBookings = ResultWrapper.Success(listOf(
        Booking(
            id = testId,
            startsAt = testISODateTime,
            endsAt = testISODateTime,
            spaceTimezone = testTimezone,
            spaceName = testName,
            spaceImage = testUrl
        ),
        Booking(
            startsAt = "2022-03-22T09:00:00.000Z",
            endsAt = testISODateTime,
            spaceTimezone = testTimezone,
            spaceName = testName,
            spaceImage = testUrl
        )
    ))
    private val allEvents = testBookings.value.map { it.toUiModel() }

    @Before
    fun setup() {
        every { bookingRepository.retrieveBookings() } returns flowOf(testBookings)

        viewModel = MainViewModel(
            bookingRepository = bookingRepository,
            dispatchers = coroutineDispatchers
        )
    }

    @Test
    fun `given onSelectedDate, save date in state and update eventsForSelectedDate`() {
        val eventsForTestDate = listOf(
            Event(id = testId, date = testLocalDate, name = testName, image = testUrl, timeInterval = "09:00 - 09:00")
        )
        val expectedState = MainScreenState.initial().copy(
            selectedDate = testLocalDate,
            events = allEvents.groupBy { it.date },
            eventsForSelectedDate = eventsForTestDate,
            isLoading = false,
        )

        viewModel.onSelectedDate(testLocalDate)

        assertEquals(viewModel.state.value, expectedState)
    }

    private fun Booking.toUiModel() = Event(
        id = id,
        name = spaceName,
        image = spaceImage,
        date = LocalDate.parse(startsAt.substringBefore(
            ISO_DATE_TIME_T_DELIMITER
        )),
        timeInterval = formatTimeInterval(startsAt, endsAt)
    )

    private fun formatTimeInterval(start: String, end: String): String {
        return "${extractTimeFromIsoDateTime(start)} - ${extractTimeFromIsoDateTime(end)}"
    }

    private fun extractTimeFromIsoDateTime(time: String): String {
        return time.substringAfter(ISO_DATE_TIME_T_DELIMITER).substringBeforeLast(
            ISO_DATE_TIME_COLON_DELIMITER
        )
    }
}