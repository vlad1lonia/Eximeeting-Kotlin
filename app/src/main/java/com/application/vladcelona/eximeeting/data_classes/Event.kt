package com.application.vladcelona.eximeeting.data_classes

import java.util.Date
import java.util.UUID

data class Event(val id: UUID = UUID.randomUUID(), var title: String = "",
                 var fromDate: Date = Date(), var toDate: Date = Date(),
                 var location: String = "", var eventStatus: Short = 0)
