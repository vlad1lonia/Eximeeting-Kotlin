package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlin.random.Random

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {
        
        fun pushEventData() {
            val descriptionText = """Join us for an exciting evening of networking and knowledge sharing at our annual Business Innovation Summit. This event brings together industry leaders, entrepreneurs, and professionals from diverse sectors to explore the latest trends and strategies shaping the business landscape.

Featuring renowned keynote speakers, interactive panel discussions, and breakout sessions, the Business Innovation Summit offers valuable insights into disruptive technologies, market trends, and successful business models. Gain inspiration from thought-provoking presentations and engage in meaningful conversations with like-minded professionals.

Whether you're an aspiring entrepreneur, a seasoned executive, or a business enthusiast, this event provides an excellent platform to expand your network, forge new partnerships, and stay ahead of the curve. Connect with industry experts, discover innovative solutions, and explore opportunities for collaboration in a dynamic and vibrant environment.

Don't miss this opportunity to join the conversation and be part of the business revolution. Reserve your spot at the Business Innovation Summit today and unlock new possibilities for growth, innovation, and success."""

            val businessProgramme: LinkedHashMap<String, ArrayList<String?>?>? = LinkedHashMap()
            businessProgramme?.set("Monday", """9:00 AM - 10:30 AM: Financial Analysis Meeting
10:30 AM - 11:30 AM: Sales Training Session
11:30 AM - 12:30 PM: Brainstorming Session
12:30 PM - 1:30 PM: Lunch Break
1:30 PM - 3:00 PM: Team Building Activity
3:00 PM - 4:30 PM: Client Presentation Preparation
4:30 PM - 5:30 PM: Project Evaluation Meeting""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Tuesday", """9:00 AM - 10:00 AM: Team Meeting
10:00 AM - 11:00 AM: Project Planning Session
11:00 AM - 12:00 PM: Client Call
12:00 PM - 1:00 PM: Lunch Break
1:00 PM - 3:00 PM: Marketing Strategy Workshop
3:00 PM - 4:00 PM: Product Development Discussion
4:00 PM - 5:00 PM: Networking Event""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Wednesday", """9:00 AM - 10:00 AM: Departmental Meeting
10:00 AM - 11:00 AM: Market Research Analysis
11:00 AM - 12:00 PM: Product Design Review
12:00 PM - 1:00 PM: Lunch Break
1:00 PM - 2:30 PM: Business Development Discussion
2:30 PM - 4:00 PM: Training Workshop
4:00 PM - 5:00 PM: Team Collaboration Session""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Thursday", """9:00 AM - 10:30 AM: Sales Pitch Practice
10:30 AM - 11:30 AM: Vendor Meeting
11:30 AM - 12:30 PM: Customer Feedback Analysis
12:30 PM - 1:30 PM: Lunch Break
1:30 PM - 3:00 PM: Project Status Update
3:00 PM - 4:30 PM: Strategic Planning Session
4:30 PM - 5:30 PM: Networking Event""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Friday", """9:00 AM - 10:00 AM: Team Stand-up Meeting
10:00 AM - 11:00 AM: Marketing Campaign Review
11:00 AM - 12:00 PM: Sales Report Analysis
12:00 PM - 1:00 PM: Lunch Break
1:00 PM - 3:00 PM: Client Meeting
3:00 PM - 4:30 PM: Product Demo
4:30 PM - 5:30 PM: Project Retrospective""".trim().split("\n").toList() as ArrayList<String?>?
            )

            val events: ArrayList<Event> = ArrayList()

            var event = Event(
                Random.nextInt(), "en", "First Conference", Event.randomDate(),
                Event.randomDate(), "Moscow, Russia", "117892, Random Street",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Second Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Third Conference", Event.randomDate(),
                Event.randomDate(), "Madrid, Spain", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Fourth Conference", Event.randomDate(),
                Event.randomDate(), "Casablanca, Morocco", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Fifth Conference", Event.randomDate(),
                Event.randomDate(), "Minsk, Belarus", "Random Drive",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Sixth Conference", Event.randomDate(),
                Event.randomDate(), "Los Angeles, CA, USA", "Random Road",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Seventh Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Event",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Eighth Conference", Event.randomDate(),
                Event.randomDate(), "Seattle, WA, USA", "Random Street",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Ninth Conference", Event.randomDate(),
                Event.randomDate(), "New York, NY, USA", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "en", "Tenth Conference", Event.randomDate(),
                Event.randomDate(), "Albany, NY, USA", "Random Road",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)

            val databaseReference = FirebaseDatabase
                .getInstance().getReference("Events")
            for (element in events) {
                databaseReference.child(element.id.toString()).setValue(Gson().toJson(element))
            }

            Thread.sleep(1000)

            val descriptionTextRu: String = """Присоединяйтесь к нам, чтобы провести увлекательный вечер общения и обмена знаниями на нашем ежегодном Саммите бизнес-инноваций. Это мероприятие объединяет лидеров отрасли, предпринимателей и профессионалов из различных секторов для изучения последних тенденций и стратегий, формирующих бизнес-ландшафт.

Благодаря выступлениям известных основных докладчиков, интерактивным панельным дискуссиям и секционным заседаниям, Саммит бизнес-инноваций предлагает ценные знания о разрушительных технологиях, тенденциях рынка и успешных бизнес-моделях. Наберитесь вдохновения от презентаций, наводящих на размышления, и примите участие в содержательных беседах с профессионалами-единомышленниками.

Если вы начинающий предприниматель, опытный руководитель или энтузиаст бизнеса, это мероприятие станет отличной платформой для расширения вашей сети, налаживания новых партнерских отношений и опережающего развития. Общайтесь с экспертами отрасли, открывайте для себя инновационные решения и изучайте возможности для сотрудничества в динамичной и оживленной обстановке.

Не упустите эту возможность присоединиться к разговору и стать частью революции в бизнесе. Забронируйте свое место на Саммите бизнес-инноваций сегодня и откройте новые возможности для роста, инноваций и успеха."""

            val businessProgrammeRu: LinkedHashMap<String, ArrayList<String?>?>? = LinkedHashMap()

            businessProgrammeRu?.set("Понедельник", """9:00 - 10:00: Собрание команды
10:00 - 11:00: Сессия планирования проекта
11:00 - 12:00: Звонок клиента
12:00 - 13:00: Перерыв на обед
13:00 - 15:00: Семинар по маркетинговой стратегии
15:00 - 16:00: Обсуждение разработки продукта
16:00 - 17:00: Сетевое мероприятие""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgrammeRu?.set("Вторник", """9:00 - 10:30: Совещание по финансовому анализу
10:30 - 11:30: Тренинг по продажам
11:30 - 12:30: Сессия мозгового штурма
12:30 - 13:30: Перерыв на обед
13:30 - 15:00: Мероприятие по формированию команды
15:00 - 16:30: Подготовка презентации клиента
16:30 - 17:30: Встреча по оценке проекта""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgrammeRu?.set("Среда", """9:00 - 10:00: Заседание кафедры
10:00 - 11:00: Анализ маркетинговых исследований
11:00 - 12:00: Обзор дизайна продукта
12:00 - 13:00: Перерыв на обед
13:00 - 14:30: Обсуждение развития бизнеса
14:30 - 16:00: Учебный семинар
16:00 - 17:00: Сессия командного взаимодействия""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgrammeRu?.set("Четверг", """9:00 - 10:30: Практика продаж
10:30 - 11:30: Встреча с поставщиками
11:30 - 12:30: Анализ отзывов клиентов
12:30 - 13:30: Перерыв на обед
13:30 - 15:00: Обновление статуса проекта
15:00 - 16:30: Сессия стратегического планирования
16:30 - 17:30: Сетевое мероприятие""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgrammeRu?.set("Пятница", """9:00 - 10:00: Собрание команды
10:00 - 11:00: Обзор маркетинговой кампании
11:00 - 12:00: Анализ отчета о продажах
12:00 - 13:00: Перерыв на обед
13:00 - 15:00: Встреча с клиентом
15:00 - 16:30: Демонстрация продукта
16:30 - 17:30: Ретроспектива проекта""".trim().split("\n").toList() as ArrayList<String?>?
            )

            event = Event(
                Random.nextInt(), "ru", "Первая конференция", Event.randomDate(),
                Event.randomDate(), "Москва, Россия", "Проектируемый проезд №0001",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Вторая конференция", Event.randomDate(),
                Event.randomDate(), "Санкт-Петербург, Россия", "Проектируемый проезд №0121",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Третья конференция", Event.randomDate(),
                Event.randomDate(), "Саратов, Россия", "Проектируемый проезд №9001",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Четвёртая конференция", Event.randomDate(),
                Event.randomDate(), "Калининград, Россия", "Проектируемый проезд №3241",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Пятая конференция", Event.randomDate(),
                Event.randomDate(), "Владивосток, Россия", "Проектируемый проезд №5001",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Шестая конференция", Event.randomDate(),
                Event.randomDate(), "Минск, Беларусь", "Проектируемый проезд №4621",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Седьмая конференция", Event.randomDate(),
                Event.randomDate(), "Москва, Россия", "Проектируемый проезд №45001",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Восьмая конференция", Event.randomDate(),
                Event.randomDate(), "Мурманск, Россия", "Проектируемый проезд №0001",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Девятая конференция", Event.randomDate(),
                Event.randomDate(), "Владикавказ, Россия", "Проектируемый проезд №578001",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)
            event = Event(
                Random.nextInt(), "ru", "Десятая конференция", Event.randomDate(),
                Event.randomDate(), "Астрахань, Россия", "Проектируемый проезд №5601",
                "Eximeeting (Экзимитинг)", descriptionTextRu, Event.randomSpeakersRu(),
                Event.randomModeratorsRu(), businessProgrammeRu)
            events.add(event)

            for (element in events) {
                databaseReference.child(element.id.toString()).setValue(Gson().toJson(element))
            }
        }

        fun getEventData(): List<Event> {
            val events: ArrayList<Event> = ArrayList()

            val databaseReference: DatabaseReference = FirebaseDatabase
                .getInstance().getReference("Events")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (eventSnapshot in snapshot.children) {
                        val jsonEvent: String? = eventSnapshot.getValue(String::class.java)
                        if (jsonEvent != null) {
                            events.add(Gson().fromJson(jsonEvent, Event::class.java))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "The load of Events has been cancelled")
                }

            })

            val eventsId: HashMap<String, Boolean> = HashMap()
            for (event in events) {
                eventsId[event.id.toString()] = false
            }

            Log.i(TAG, Gson().toJson(eventsId))

            return events
        }

        fun defaultVisitedMap(): HashMap<String, Boolean> {

            val defaultMap: HashMap<String, Boolean> = HashMap()

            val databaseReference: DatabaseReference = FirebaseDatabase
                .getInstance().getReference("Events")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (eventSnapshot in snapshot.children) {
                        defaultMap[eventSnapshot.key!!] = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "The load of Events has been cancelled")
                }

            })

            Thread.sleep(1500)

            return defaultMap
        }
    }
}