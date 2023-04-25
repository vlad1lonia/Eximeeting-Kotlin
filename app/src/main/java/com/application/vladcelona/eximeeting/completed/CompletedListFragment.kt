package com.application.vladcelona.eximeeting.completed

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.databinding.FragmentCompletedListBinding
import com.application.vladcelona.eximeeting.event_managment.EventListViewModel
import java.util.*

private const val TAG = "CompletedListFragment"
private const val SAVED_SUBTITLE_VISIBLE = "subtitle"

class CompletedListFragment : Fragment() {

    private lateinit var binding: FragmentCompletedListBinding

    private var adapter: CompletedEventAdapter? = CompletedEventAdapter(emptyList())
    private val eventListViewModel: EventListViewModel by lazy {
        ViewModelProvider(this)[EventListViewModel::class.java]
    }

    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onEventSelected(eventId: UUID)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedListBinding.inflate(inflater, container, false)

        binding.completedRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.completedRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        eventListViewModel.eventListLiveData.observe(
            viewLifecycleOwner
        ) { events ->
            events?.let {
                Log.i(TAG, "Got eventLiveData ${events.size}")
                updateUI(events)
            }
        }
    }

    private fun updateUI(events: List<Event>) {
        adapter?.let { it.events = events } ?: kotlin.run { adapter = CompletedEventAdapter(events) }
        binding.completedRecyclerView.adapter = adapter
    }


    private inner class CompletedEventHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var event: Event

        private val eventName: TextView = itemView.findViewById(R.id.event_name)
        private val fromDateTextView: TextView = itemView.findViewById(R.id.from_date)
        private val eventImage: ImageView = itemView.findViewById(R.id.event_image)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(event: Event) {
            this.event = event

            eventName.text = this.event.eventName
            fromDateTextView.text = this.event.fromDate.toString()
            eventImage.visibility = View.VISIBLE
        }

        override fun onClick(p0: View?) {
            callbacks?.onEventSelected(event.id)
        }

    }

    private inner class CompletedEventAdapter(var events: List<Event>) :
        RecyclerView.Adapter<CompletedEventHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedEventHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_event, parent, false)

            return CompletedEventHolder(view)
        }

        override fun onBindViewHolder(holder: CompletedEventHolder, position: Int) {
            val event = events[position]
            holder.bind(event)
        }

        override fun getItemCount(): Int = events.size

    }

    companion object {
        fun newInstance(): CompletedListFragment {
            return CompletedListFragment()
        }
    }
}