package com.application.vladcelona.eximeeting.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.vladcelona.eximeeting.EximeetingApplication
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.user.User
import com.application.vladcelona.eximeeting.event_managment.EventListAdapter
import com.application.vladcelona.eximeeting.event_managment.EventViewModel
import com.application.vladcelona.eximeeting.event_managment.EventViewModelFactory
import com.application.vladcelona.eximeeting.firebase.EximeetingFirebase.Companion.getDocument
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "CompletedEventListFragment"

class CompletedEventListFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var notVisitedTextView: TextView

    private lateinit var user: User
    private lateinit var uid: String

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((activity?.application as EximeetingApplication).repository)
    }

    override fun onStart() {
        super.onStart()

        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        user = getDocument("users", uid) as User
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
        notVisitedTextView = view.findViewById(R.id.not_visited_text)

        val adapter = EventListAdapter(EventListAdapter.OnClickListener {
            Log.i(TAG, "Clicked an item")
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        populateRecyclerView(adapter)

        return view
    }

    override fun onResume() {
        super.onResume()

        val adapter = EventListAdapter(EventListAdapter.OnClickListener {
            Log.i(TAG, "Clicked an item")
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        populateRecyclerView(adapter)
    }

    /**
     * Method for populating [RecyclerView] with data from Cloud Firestore
     * @author Balandin (Vladcelona) Vladislav
     */
    private fun populateRecyclerView(adapter: EventListAdapter) {

        eventViewModel.events.observe(viewLifecycleOwner) { events ->

            for (event in events) {
                Log.i(TAG, "${event.id}: ${user.visitedEvents[event.id.toString()]}")
            }

            val completedEvents = events.filterIndexed { _, event ->
                Locale.getDefault().language == event.language
            }

            if (completedEvents.isNotEmpty()) {
                notVisitedTextView.visibility = View.INVISIBLE
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