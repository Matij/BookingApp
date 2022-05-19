package com.martafoderaro.bookingapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.martafoderaro.bookingapp.R
import com.martafoderaro.bookingapp.databinding.CalendarDayBinding
import com.martafoderaro.bookingapp.databinding.CalendarHeaderBinding
import com.martafoderaro.bookingapp.databinding.MainFragmentBinding
import com.martafoderaro.bookingapp.ui.adapter.EventsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.main_fragment) {
    companion object {
        const val TAG = "MainFragment"

        fun instance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    private val eventsAdapter = EventsAdapter {
        Toast.makeText(requireContext(), "Tapped on event: ${it.name}", Toast.LENGTH_SHORT).show()
    }

    private val selectionFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding
    get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainFragmentBinding.bind(view)

        setupEventsRecyclerView()

        val daysOfWeek = daysOfWeekFromLocale()

        binding.calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].name.first().toString()
                        tv.setTextColorRes(R.color.black)
                    }
                }
            }
        }

        observeState(daysOfWeek)
    }

    private fun observeState(daysOfWeek: Array<DayOfWeek>) = lifecycleScope.launch {
        viewModel.state.collect { state ->

            val yearMonth = YearMonth.of(state.year, state.month)
            if (homeActivityToolbar.title.isNullOrBlank()) {
                homeActivityToolbar.title = titleSameYearFormatter.format(yearMonth)
            }

            if (binding.calendar.isEmpty()) {
                binding.calendar.apply {
                    setup(yearMonth, yearMonth, daysOfWeek.first())
                }
            }

            binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(viewModel, view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.binding.dayTextView
                    val dotView = container.binding.dotView

                    textView.text = day.date.dayOfMonth.toString()

                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.makeVisible()
                        when (day.date) {
                            state.selectedDate -> {
                                textView.setTextColorRes(R.color.blue)
                                textView.setBackgroundResource(R.drawable.selected_bg)
                                dotView.makeInVisible()
                            }
                            else -> {
                                textView.setTextColorRes(R.color.black)
                                textView.background = null
                                dotView.isVisible = state.events[day.date].orEmpty().isNotEmpty()
                            }
                        }
                    } else {
                        textView.makeInVisible()
                        dotView.makeInVisible()
                    }
                }
            }
            eventsAdapter.events = state.eventsForSelectedDate
            updateViews(state.selectedDate)
        }
    }

    private fun setupEventsRecyclerView() {
        binding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
    }

    private fun updateViews(date: LocalDate) {
        if (!binding.calendar.isComputingLayout && binding.calendar.scrollState == SCROLL_STATE_IDLE) {
            binding.calendar.notifyDateChanged(date)
        }
        val formattedDate = selectionFormatter.format(date).uppercase()
        binding.selectedDateText.text = getString(R.string.bookings_label).format(formattedDate)
        eventsAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DayViewContainer(viewModel: MainViewModel, view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = CalendarDayBinding.bind(view)

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                viewModel.onSelectedDate(day.date)
            }
        }
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
}
