package com.application.vladcelona.eximeeting.event_managment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.data_classes.User
import com.application.vladcelona.eximeeting.databinding.FragmentEventBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

private const val TAG = "EventFragment"
private const val ARG_EVENT_ID = "event_id"

class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding

    private lateinit var user: User
    private lateinit var uid: String
    private lateinit var eventId: String
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()


    }

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
        
        eventId = arguments?.getInt("id").toString()

        binding.fragmentEventStatus.setTextColor(Event.getStatusCodeColor
            (arguments?.getInt("status")))

        binding.businessProgrammeButton.setOnClickListener {
            findNavController().navigate(R.id.businessProgrammeFragment,
                bundleOf("businessProgramme" to arguments
                    ?.getSerializable("businessProgramme")))
        }

        if (uid.isNotEmpty()) {
            setFloatingButton()
        }

        return binding.root
    }

    private fun setFloatingButton() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                Log.i(TAG, user.visitedEvents)

                val visitedEvents: HashMap<String, Boolean> = Gson()
                        .fromJson(user.visitedEvents, HashMap<String, Boolean>()::class.java)
                Log.i(TAG, visitedEvents.toString())
                Log.i(TAG, "$eventId: " + visitedEvents[eventId].toString())

                binding.eventVisitedButton.apply {

                    setOnClickListener {

                        if (visitedEvents[eventId]!!) {
                            isClickable = false
                            setImageResource(R.drawable.completed_icon)
                        } else {
                            setImageResource(R.drawable.completed_icon_outlined)
                        }

                        if (!visitedEvents[eventId]!!) {
                            visitedEvents[eventId] = true
                            setImageResource(R.drawable.completed_icon)
                            user.visitedEvents = Gson().toJson(visitedEvents)

                            val newUserValues: Map<String, Any> = user.toMap()
                            databaseReference.child(uid).updateChildren(newUserValues)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    activity, "Failed to download data from Database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}