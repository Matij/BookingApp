package com.martafoderaro.bookingapp.ui.adapter

import com.martafoderaro.bookingapp.domain.model.Booking
import com.martafoderaro.bookingapp.util.ISO_DATE_TIME_COLON_DELIMITER
import com.martafoderaro.bookingapp.util.ISO_DATE_TIME_T_DELIMITER
import java.time.LocalDate

fun List<Booking>.toUiModel(): Map<LocalDate, List<Event>> {
    val events = map { it.toUiModel() }
    return events.groupBy { it.date }
}

fun Booking.toUiModel() = Event(
    id = id,
    name = spaceName,
    image = spaceImage,
    date = LocalDate.parse(startsAt.substringBefore(ISO_DATE_TIME_T_DELIMITER)),
    timeInterval = formatTimeInterval(startsAt, endsAt),
)

private fun formatTimeInterval(start: String, end: String): String {
    return "${extractTimeFromIsoDateTime(start)} - ${extractTimeFromIsoDateTime(end)}"
}

private fun extractTimeFromIsoDateTime(time: String): String {
    return time.substringAfter(ISO_DATE_TIME_T_DELIMITER).substringBeforeLast(ISO_DATE_TIME_COLON_DELIMITER)
}

