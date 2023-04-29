package com.application.vladcelona.eximeeting.event_managment

import androidx.lifecycle.ViewModel
import com.application.vladcelona.eximeeting.data_classes.Event

class EventListViewModel : ViewModel() {

    private val eventRepository = EventRepository.get()
    val eventListLiveData = eventRepository.getEvents()

    fun addEvent(event: Event) {
        eventRepository.addEvent(event)
    }

}