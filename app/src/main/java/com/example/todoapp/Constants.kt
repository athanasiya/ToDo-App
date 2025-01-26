package com.example.todoapp

// Notification Channel constants

// Name of Notification Channel for verbose notifications of background work
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"

// Description of Notification Channel for verbose notifications of background work
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"

// Title of Notification for verbose notifications of background work
val NOTIFICATION_TITLE: CharSequence = "Task!"

// ID of Notification Channel for verbose notifications of background work
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"

// ID of Notification for verbose notifications of background work
const val NOTIFICATION_ID = 1

// Request code for pending intent
const val REQUEST_CODE = 0

// Reminder schedule
const val FIVE_SECONDS: Long = 5
const val THIRTY_SECONDS: Long = 30
const val ONE_MINUTE: Long = 1
const val TWO_MINUTES: Long = 2
const val ONE_DAY: Long = 1
const val SEVEN_DAYS: Long = 7
const val THIRTY_DAYS: Long = 30