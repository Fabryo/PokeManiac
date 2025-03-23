package com.shodo.android.tracking

class TrackingUtils {

    fun buildEventScreen(screenName: String): HashMap<String, String> {
        return hashMapOf<String, String>().apply {
            put(SCREEN_NAME, screenName)
        }
    }

    fun buildEventClick(clickName: String): HashMap<String, String> {
        return hashMapOf<String, String>().apply {
            put(CLICK_NAME, clickName)
        }
    }

    companion object {
        private const val SCREEN_NAME = "screen_name"
        private const val CLICK_NAME = "click_name"
    }
}