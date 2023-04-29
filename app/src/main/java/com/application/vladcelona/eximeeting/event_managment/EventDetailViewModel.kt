package com.application.vladcelona.eximeeting.event_managment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.application.vladcelona.eximeeting.data_classes.Event
import java.io.File
import java.util.*

class EventDetailViewModel : ViewModel() {

    private val eventRepository = EventRepository.get()
    private val eventIdLiveData = MutableLiveData<UUID>()

    val eventLiveData: LiveData<Event?> =
        Transformations.switchMap(eventIdLiveData) { eventId ->
            eventRepository.getEvent(eventId)
        }

    fun loadEvent(eventId: UUID) {
        eventIdLiveData.value = eventId
    }

    fun saveEvent(event: Event) {
        eventRepository.updateEvent(event)
    }

    fun getPhotoFile(event: Event): File {
        return eventRepository.getPhotoFile(event)
    }
}