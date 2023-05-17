package com.application.vladcelona.eximeeting.event_managment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.databinding.FragmentEventBinding

private const val TAG = "EventFragment"
private const val ARG_EVENT_ID = "event_id"

class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        binding.fragmentEventName.text = arguments?.getString("name")
        binding.fragmentEventDate.text = arguments?.getString("date")
        binding.fragmentEventLocation.text = arguments?.getString("location")
        binding.fragmentEventStatus.text = Event.convertStatusCode(arguments?.getInt("status"))
        binding.fragmentEventDescription.text = arguments?.getString("description")
        binding.fragmentEventSpeakers.text = arguments?.getString("speakers")
        binding.fragmentEventModerators.text = arguments?.getString("moderators")

        binding.fragmentEventStatus.setTextColor(Event.getStatusCodeColor
            (arguments?.getInt("status")))

        binding.businessProgrammeButton.setOnClickListener {
            findNavController().navigate(R.id.businessProgrammeFragment,
            bundleOf("businessProgramme" to arguments?.getSerializable("businessProgramme")))
        }

        return binding.root
    }

}