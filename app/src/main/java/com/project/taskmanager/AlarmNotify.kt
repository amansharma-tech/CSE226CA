package com.project.taskmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class AlarmNotify : AppCompatActivity() {
    lateinit var setAlarmBtn: AppCompatButton
    lateinit var cancelAlarmBtn: AppCompatButton
    lateinit var selectTimeBtn: AppCompatButton
    lateinit var timeDisplayedTV: TextView
    lateinit var picker: MaterialTimePicker
    lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notify)

        setAlarmBtn = findViewById(R.id.setAlarmBtn)
        cancelAlarmBtn = findViewById(R.id.cancelAlarmBtn)
        selectTimeBtn = findViewById(R.id.selectTimeBtn)
        timeDisplayedTV = findViewById(R.id.timeDisplayedTV)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Set Reminder")

        setAlarmBtn.setOnClickListener {
            setAlarmFunction()
        }

        cancelAlarmBtn.setOnClickListener {
            cancelAlarmFunction()
        }

        selectTimeBtn.setOnClickListener {
            selectTimeFunction()
        }



        createNotificationChannel()

    }

    private fun cancelAlarmFunction() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun selectTimeFunction() {
        showTimePicker()
    }

    private fun setAlarmFunction() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker() {

        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Notification Time")
            .build()

        picker.show(supportFragmentManager, "Task Manager")

        picker.addOnPositiveButtonClickListener{
            if(picker.hour > 12) {
                timeDisplayedTV.text = String.format("%02d", picker.hour - 12) +":"+ String.format("%02d", picker.minute) +" "+ "PM"
            }
            else {
                timeDisplayedTV.text = String.format("%02d", picker.hour) + ":" + String.format("%02d", picker.minute) + " " + "AM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }


    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "NotificationManagerChannel"
            val description = "Channel for notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Task Manager", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }
}