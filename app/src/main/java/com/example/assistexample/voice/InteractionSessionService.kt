package com.example.assistexample.voice

import android.os.Build
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.service.voice.VoiceInteractionSessionService
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class InteractionSessionService : VoiceInteractionSessionService() {
    override fun onNewSession(bundle: Bundle?): VoiceInteractionSession {
        Log.d(TAG, "start new session")
        return InteractionSession(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "start voice interaction session service")
    }

    override fun onDestroy() {
        Log.d(TAG, "destroy voice interaction session service")
        super.onDestroy()
    }
}
