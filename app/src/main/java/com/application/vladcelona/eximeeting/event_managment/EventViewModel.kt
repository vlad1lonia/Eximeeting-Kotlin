package com.application.vladcelona.eximeeting.event_managment

import androidx.lifecycle.*
import com.application.vladcelona.eximeeting.data_classes.Event
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val events: LiveData<List<Event>> = repository.events.asLiveData()

    fun insert(event: Event) = viewModelScope.launch { repository.insert(event) }
}

class EventViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(repository) as T
        }

        throw IllegalStateException("Unknown ViewModel class")
    }
}