package com.example.timetrackingapp

class Lesson(private val lessonNumber: Int, private var currentHour: Int, private var currentMinutes: Int, private var currentSeconds: Int) {
    // Variables to hold end time
    private var endHour: Int = 0
    private var endMinute: Int = 0
    private var endSecond: Int = 0

    // Array to hold time left (it holds hours, minutes and seconds)
    val timeLeft = IntArray(3)
    // String representation of lesson number
    private var lessonNumber_s: String = ""

    init {
        // starter methods to initialize the end time and calculate time left when the Lesson object is created
        setEndTime()
        calculateTimeLeft()
        numToString()
    }

    // Method to set end time based on lesson number
    private fun setEndTime() {
        endSecond = 0
        when (lessonNumber) {
            1 -> {
                endHour = 9
                endMinute = 30
            }
            2 -> {
                endHour = 11
                endMinute = 10
            }
            3 -> {
                endHour = 13
                endMinute = 20
            }
            4 -> {
                endHour = 15
                endMinute = 0
            }
            else -> {
                // If lesson number is not within 1-4, set end time based on current hour
                endHour = currentHour
                endMinute = when (currentHour) {
                    9 -> 40
                    11 -> 50
                    13 -> 30
                    else -> 0
                }
            }
        }
    }

    // Method to calculate time left
    fun calculateTimeLeft() {
        // Calculate total seconds for current and end time
        val totalSecondsCurrent: Int = currentHour * 3600 + currentMinutes * 60 + currentSeconds
        val totalSecondsEnd: Int = endHour * 3600 + endMinute * 60 + endSecond
        // Calculate total seconds left
        var totalSecondsLeft: Int = totalSecondsEnd - totalSecondsCurrent

        // Calculate hours left
        timeLeft[0] = totalSecondsLeft / 3600
        totalSecondsLeft -= 3600 * timeLeft[0]
        // Calculate minutes left
        timeLeft[1] = totalSecondsLeft / 60
        totalSecondsLeft -= 60 * timeLeft[1]
        // Calculate seconds left
        timeLeft[2] = totalSecondsLeft
    }

    // Method to convert lesson number to string representation
    private fun numToString() {
        lessonNumber_s = when (lessonNumber) {
            1 -> "First period"
            2 -> "Second period"
            3 -> "Third period"
            4 -> "Fourth period"
            else -> "Break"
        }
    }

    // Override toString method to return lesson number as string
    override fun toString(): String {
        return lessonNumber_s
    }
}