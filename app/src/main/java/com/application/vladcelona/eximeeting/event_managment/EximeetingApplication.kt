package com.application.vladcelona.eximeeting.event_managment

import android.app.Application

class EximeetingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EventRepository.initialize(this)
    }
}