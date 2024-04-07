package com.example.timetrackingapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val timeLeft = findViewById<TextView>(R.id.time_left)

        val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"))
        val currentDate: Date = convertToDate(currentDateTime)
        Log.e("tag1", "currentDate: $currentDate")

        var resultMessage = " "

        // Check if current date and time falls within lesson hours
        if (checkDateTime(currentDate)) {
            Log.e("tag2", "WE ARE IN")
            val currentLesson = getCurrentLesson(currentDate) // Get current lesson based on current time

            // Generate result message including current lesson and time left
            resultMessage = "Now it is $currentLesson"
            val (hoursLeft, minutesLeft, secondsLeft) = currentLesson.timeLeft

            resultMessage += ".\n\n Until it's ent it is: $hoursLeft h. $minutesLeft min. $secondsLeft sec. "
        } else {
            resultMessage = "No lessons!" // If not within lesson hours, display "no lessons" message
        }

        timeLeft.text = resultMessage // Set the result message in the TextView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToDate(currentDateTime: ZonedDateTime): Date {
        val localOffset = ZoneOffset.systemDefault().rules.getOffset(LocalDateTime.now())
        val zonedMillis: Long =
            1000L * currentDateTime.toLocalDateTime().toEpochSecond(localOffset) + currentDateTime.toLocalDateTime()
                .getNano() / 1000000L
        return Date(zonedMillis)
    }

    // Check if current date and time falls within lesson hours (Monday to Friday, 08:00 - 15:00)
    private fun checkDateTime(currentDate: Date): Boolean {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        val hourNum : Int = cal.get(Calendar.HOUR_OF_DAY)
        val dayOfTheWeekNum : Int = cal.get(Calendar.DAY_OF_WEEK)
        Log.e("tag1", "HOUR_OF_DAY: $hourNum")
        Log.e("tag1", "DAY_OF_WEEK: $dayOfTheWeekNum")

        // Return true if it's between 08:00 and 15:00 on Monday to Friday
        // Note: Sunday is the first day of the week in Japan
        return hourNum in 8..15 && dayOfTheWeekNum != 6 && dayOfTheWeekNum != 1
    }

    // Determine the current lesson based on the current time
    private fun getCurrentLesson(currentDate: Date): Lesson {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        val seconds = cal.get(Calendar.SECOND)

        var lessonNumber = 0 // By default, it's a break

        when (hour) {
            8 -> lessonNumber = 1
            9 -> {
                lessonNumber = if (minutes <= 30) 1 else if (minutes >= 40) 2 else 0
            }
            10 -> lessonNumber = 2
            11 -> {
                lessonNumber = if (minutes <= 10) 2 else if (minutes >= 50) 3 else 0
            }
            12 -> lessonNumber = 3
            13 -> {
                lessonNumber = if (minutes <= 20) 3 else if (minutes >= 30) 4 else 0
            }
            14 -> lessonNumber = 4
        }

        // Return the corresponding Lesson object
        return Lesson(lessonNumber, hour, minutes, seconds)
    }

    // Handle click event of reload the button
    fun clickReloadButton(view: View) {
        val intent = intent
        finish()
        startActivity(intent)
    }
}