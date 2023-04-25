package com.application.vladcelona.eximeeting.event_managment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.databinding.FragmentEventBinding
import java.io.File
import java.util.*

private const val TAG = "EventFragment"

private const val ARG_EVENT_ID = "event_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val DATE_FORMAT = "EEE, MMM, dd"

class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding
    private val eventDetailViewModel: EventDetailViewModel by lazy {
        ViewModelProvider(this)[EventDetailViewModel::class.java]
    }

    private lateinit var event: Event
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        event = Event()
        val eventId: UUID = arguments?.getSerializable(ARG_EVENT_ID) as UUID

        eventDetailViewModel.loadEvent(eventId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = arguments?.getSerializable(ARG_EVENT_ID) as UUID
        eventDetailViewModel.loadEvent(eventId)

        eventDetailViewModel.eventLiveData.observe(viewLifecycleOwner, Observer { event ->
            event?.let {
                this.event = event
                photoFile = eventDetailViewModel.getPhotoFile(event)
                updateUI()
            }
        })
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        eventDetailViewModel.saveEvent(event)
    }

    private fun updateUI() {

    }

    companion object {

        fun newInstance(crimeId: UUID): EventFragment {
            val args = Bundle().apply {
                putSerializable(ARG_EVENT_ID, crimeId)
            }
            return EventFragment().apply {
                arguments = args
            }
        }
    }
}