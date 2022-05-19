package com.martafoderaro.bookingapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martafoderaro.bookingapp.databinding.EventItemViewBinding
import com.martafoderaro.bookingapp.ui.layoutInflater
import com.squareup.picasso.Picasso
import java.time.LocalDate

data class Event(
    val id: String,
    val name: String,
    val image: String,
    val date: LocalDate,
    val timeInterval: String,
)

class EventsAdapter(
    val onClick: (Event) -> Unit
): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    var events = listOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            EventItemViewBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: EventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventsViewHolder(private val binding: EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) = with(event) {
            binding.eventName.text = name
            binding.eventTime.text = timeInterval
            Picasso
                .get()
                .load(event.image)
                .into(binding.eventImage)
        }
    }
}