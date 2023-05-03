package com.application.vladcelona.eximeeting.event_managment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.Event

class EventListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Event, EventListAdapter.EventViewHolder>(EventsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(current)
            holder.itemView.findNavController().navigate(R.id.eventFragment,
                current.toBundle())
        }
        holder.bind(current)
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val eventName: TextView = itemView.findViewById(R.id.event_name)
        private val eventDate: TextView = itemView.findViewById(R.id.event_date)
        private val eventLocation: TextView = itemView.findViewById(R.id.event_location)
        private val eventStatus: TextView = itemView.findViewById(R.id.event_status)

        @SuppressLint("SetTextI18n")
        fun bind(event: Event?) {
            eventName.text = event?.name
            eventDate.text = "${event?.fromDate} - ${event?.toDate}"
            eventLocation.text = event?.location
            eventStatus.text = event?.convertStatusCode()
        }

        companion object {
            fun create(parent: ViewGroup): EventViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return EventViewHolder(view)
            }
        }
    }

    class EventsComparator : DiffUtil.ItemCallback<Event>() {

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event): Unit = clickListener(event)
    }
}