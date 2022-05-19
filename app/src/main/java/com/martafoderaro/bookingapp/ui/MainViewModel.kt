package com.martafoderaro.bookingapp.ui

import androidx.lifecycle.viewModelScope
import com.martafoderaro.bookingapp.base.BaseViewModel
import com.martafoderaro.bookingapp.base.Reducer
import com.martafoderaro.bookingapp.core.CoroutineDispatcherProvider
import com.martafoderaro.bookingapp.data.datasources.ResultWrapper
import com.martafoderaro.bookingapp.domain.repository.BookingRepository
import com.martafoderaro.bookingapp.ui.adapter.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val dispatchers: CoroutineDispatcherProvider,
): BaseViewModel<MainScreenState, MainScreenUiEvent>() {

    private val reducer = MainReducer(MainScreenState.initial())

    override val state: StateFlow<MainScreenState>
        get() = reducer.state

    init {
        loadAllEvents()
    }

    fun onSelectedDate(date: LocalDate) = viewModelScope.launch(dispatchers.default) {
        sendEvent(MainScreenUiEvent.UpdateSelectedDate(date = date))
        sendEvent(MainScreenUiEvent.UpdateSelectedDateEvents(eventsForSelectedDate = state.value.events[date] ?: emptyList()))
    }

    private fun loadAllEvents() = viewModelScope.launch(dispatchers.default) {
        when (val data = bookingRepository.retrieveBookings().last()) {
            is ResultWrapper.Success -> {
                val mapOFData = data.value.toUiModel()
                sendEvent(MainScreenUiEvent.ShowEvents(events = mapOFData))
                sendEvent(MainScreenUiEvent.UpdateSelectedDateEvents(eventsForSelectedDate = mapOFData.get(state.value.selectedDate) ?: emptyList()))
            }
            is ResultWrapper.GenericError -> handleError(data.error?.message)
            is ResultWrapper.NetworkError -> handleError(data.message)
        }
    }

    private fun sendEvent(event: MainScreenUiEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: MainScreenState) : Reducer<MainScreenState, MainScreenUiEvent>(initial) {
        override fun reduce(oldState: MainScreenState, event: MainScreenUiEvent) {
            when (event) {
                is MainScreenUiEvent.ShowEvents -> {
                    setState(oldState.copy(events = event.events, isLoading = false))
                }
                is MainScreenUiEvent.UpdateSelectedDate -> {
                    setState(oldState.copy(selectedDate = event.date))
                }
                is MainScreenUiEvent.UpdateSelectedDateEvents -> {
                    setState(oldState.copy(eventsForSelectedDate = event.eventsForSelectedDate))
                }
                is MainScreenUiEvent.ShowError -> {
                    setState(oldState.copy(errorMessage = event.message, isLoading = false))
                }
            }
        }
    }

    private fun handleError(errorMessage: String?) {
        val message = errorMessage ?: "Unknown error."
        sendEvent(MainScreenUiEvent.ShowError(message))
    }
}