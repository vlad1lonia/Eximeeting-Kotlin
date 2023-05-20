package com.application.vladcelona.eximeeting.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.vladcelona.eximeeting.EximeetingApplication
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.User
import com.application.vladcelona.eximeeting.event_managment.EventListAdapter
import com.application.vladcelona.eximeeting.event_managment.EventViewModel
import com.application.vladcelona.eximeeting.event_managment.EventViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "CompletedEventListFragment"

class CompletedEventListFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView

    private lateinit var databaseReference: DatabaseReference
    private lateinit var visitedEvents: HashMap<String, Boolean>

    private lateinit var user: User
    private lateinit var uid: String

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((activity?.application as EximeetingApplication).repository)
    }

    override fun onStart() {
        super.onStart()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                visitedEvents = Gson().fromJson(user.visitedEvents,
                    HashMap<String, Boolean>()::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to download data from Database")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed_event_list, 
            container, false)
        
        recyclerView = view.findViewById(R.id.completed_recyclerview)
        val adapter = EventListAdapter(EventListAdapter.OnClickListener {
            Log.i(TAG, "Clicked an item")
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        eventViewModel.events.observe(viewLifecycleOwner) { events ->

            for (event in events) {
                Log.i(TAG, "${event.id}: ${visitedEvents[event.id.toString()]}")
            }

            val completedEvents = events.filterIndexed { _, event ->
                visitedEvents[event.id.toString()]!!
                        && Locale.getDefault().language == event.language
            }

            completedEvents.let { adapter.submitList(it) }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val adapter = EventListAdapter(EventListAdapter.OnClickListener {
            Log.i(TAG, "Clicked an item")
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        eventViewModel.events.observe(viewLifecycleOwner) { events ->

            for (event in events) {
                Log.i(TAG, "${event.id}: ${visitedEvents[event.id.toString()]}")
            }

            val completedEvents = events.filterIndexed { _, event ->
                visitedEvents[event.id.toString()]!!
                        && Locale.getDefault().language == event.language
            }

            completedEvents.let { adapter.submitList(it) }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CompletedEventListFragment {
            return CompletedEventListFragment()
        }
    }
}