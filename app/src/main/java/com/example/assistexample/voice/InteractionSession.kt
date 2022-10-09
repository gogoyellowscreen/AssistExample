package com.example.assistexample.voice

import android.app.assist.AssistContent
import android.app.assist.AssistStructure
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import com.example.assistexample.R

@RequiresApi(Build.VERSION_CODES.M)
class InteractionSession(context: Context) : VoiceInteractionSession(context) {

    override fun onCreate() {
        Log.d("ASSIST_EXAMPLE", "create voice interaction SESSION")
        super.onCreate()
    }

    override fun onHandleAssist(
        data: Bundle?,
        structure: AssistStructure?,
        content: AssistContent?
    ) {
        super.onHandleAssist(data, structure, content)
        Log.d("ASSIST_EXAMPLE", "data: $data, structure: $structure, content: $content")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateContentView(): View {
        context
        Log.d("ASSIST_EXAMPLE", "SESSION APP CONTEXT: ${context.applicationContext}")
        Log.d("ASSIST_EXAMPLE", "create voice interaction SESSION CONTENT VIEW")
        val view = layoutInflater.inflate(R.layout.activity_assist, null)
        val openOtherAppButton = view.findViewById<Button>(R.id.button)
        openOtherAppButton.setOnClickListener {
            openOtherApp()
        }
        return view
    }

    private fun openOtherApp() {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage("YOUR APP")
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)
        context.startActivity(intent)
    }

    override fun onDestroy() {
        Log.d("ASSIST_EXAMPLE", "destroy voice interaction SESSION")
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onHide() {
        finish()
    }
}
