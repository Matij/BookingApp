package com.martafoderaro.bookingapp.ui

import androidx.compose.runtime.Immutable
import com.martafoderaro.bookingapp.base.UiEvent
import com.martafoderaro.bookingapp.base.UiState
import com.martafoderaro.bookingapp.ui.adapter.Event
import java.time.LocalDate
import java.time.Month
import java.time.Year

@Immutable
sealed class MainScreenUiEvent : UiEvent {
    data class ShowEvents(val events: Map<LocalDate, List<Event>>): MainScreenUiEvent()
    data class UpdateSelectedDate(val date: LocalDate): MainScreenUiEvent()
    data class UpdateSelectedDateEvents(val eventsForSelectedDate: List<Event>): MainScreenUiEvent()
    data class ShowError(val message: String): MainScreenUiEvent()
}

data class MainScreenState(
    val year: Int,
    val month: Int,
    val selectedDate: LocalDate,
    val events: Map<LocalDate, List<Event>>,
    val eventsForSelectedDate: List<Event>,
    val errorMessage: String?,
    val isLoading: Boolean,
): UiState {
    companion object {
        fun initial() = MainScreenState(
            year = Year.now().value,
            month = Month.MARCH.value,
            selectedDate = LocalDate.of(Year.now().value, Month.MARCH.value, 10),
            events = mutableMapOf(),
            eventsForSelectedDate = emptyList(),
            errorMessage  = null,
            isLoading = true
        )
    }

    override fun toString(): String {
        return "MainScreenState(year=$year, month=$month, selectedDate=$selectedDate, events=$events, " +
                "eventsForSelectedDate=$eventsForSelectedDate, errorMessage=$errorMessage, isLoading=$isLoading)"
    }


}