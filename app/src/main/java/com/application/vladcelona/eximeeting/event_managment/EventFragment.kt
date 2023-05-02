package com.application.vladcelona.eximeeting.event_managment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.Event

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TAG = "EventFragment"
private const val ARG_EVENT_ID = "event_id"

class EventFragment : Fragment() {

    private lateinit var eventName: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventLocation: TextView
    private lateinit var eventStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        eventName = view.findViewById(R.id.fragment_event_name)
        eventDate = view.findViewById(R.id.fragment_event_date)
        eventLocation = view.findViewById(R.id.fragment_event_location)
        eventStatus = view.findViewById(R.id.fragment_event_status)

        eventName.text = arguments?.getString("name")
        eventDate.text = arguments?.getString("date")
        eventLocation.text = arguments?.getString("location")
        eventStatus.text = arguments?.getString("status")

        return view
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"

        @JvmStatic
        fun newInstance(event: Event) {

        }
    }
}