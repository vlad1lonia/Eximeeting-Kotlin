package com.application.vladcelona.eximeeting.login_register

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.application.vladcelona.eximeeting.*
import com.application.vladcelona.eximeeting.databinding.ActivityPickBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.util.*


private const val TAG = "PickActivity"

class PickActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "PickActivity created")
        binding = ActivityPickBinding.inflate(layoutInflater)

        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        // Check if user has an account in firebase
        FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
            databaseReference.child(it1.uid).get().addOnCompleteListener {
                    task: Task<DataSnapshot> ->
                if (task.isSuccessful && task.result.exists()) {
                    Toast.makeText(this@PickActivity,
                        "You have been logged in successfully", Toast.LENGTH_SHORT).show()
                    showNotification()

                    startActivity(Intent(this@PickActivity, MainActivity::class.java))
                }
            }
        }
        }

        // Set up animation for PickActivity Views
        val appNameAnimation = AnimationUtils.loadAnimation(
            this@PickActivity, R.anim.app_name_animation)
        val descriptionAnimation = AnimationUtils.loadAnimation(
            this@PickActivity, R.anim.description_animation)
        val buttonsAnimation = AnimationUtils.loadAnimation(
            this@PickActivity, R.anim.buttons_animation)

        // Apply animations to Views
        binding.startScreenAppName.startAnimation(appNameAnimation)
        binding.startScreenDescription.startAnimation(descriptionAnimation)
        binding.loginButton.startAnimation(buttonsAnimation)
        binding.registerButton.startAnimation(buttonsAnimation)

        binding.loginButton.setOnClickListener {
            Log.i(TAG, "Creating LoginActivity")
            supportFragmentManager.beginTransaction().replace(R.id.pick_fragment_container,
                LoginFragment.newInstance()).commit()
        }

        binding.registerButton.setOnClickListener {
            Log.i(TAG, "Creating RegisterActivity")
            supportFragmentManager.beginTransaction().replace(R.id.pick_fragment_container,
                RegisterFragment.newInstance()).commit()
        }

        setContentView(binding.root)
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val CHANNEL_ID = "MYCHANNEL"

        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)

        val notification: Notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setContentText("You have been registered successfully")
            .setContentTitle("Eximeeting").setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher).setChannelId(CHANNEL_ID)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(1, notification)
    }
}