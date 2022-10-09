package com.example.assistexample.voice

import android.service.voice.VoiceInteractionService
import android.util.Log

class InteractionService : VoiceInteractionService() {
    init {
        Log.d("ASSIST_EXAMPLE", "initing interaction")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ASSIST_EXAMPLE", "on create VoiceInteractionService")
    }

    override fun onDestroy() {
        Log.d("ASSIST_EXAMPLE", "on destroy VoiceInteractionService")
        super.onDestroy()
    }
}
