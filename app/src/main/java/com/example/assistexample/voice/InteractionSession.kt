package com.example.assistexample.voice

import android.app.assist.AssistContent
import android.app.assist.AssistStructure
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.service.voice.VoiceInteractionSession
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import androidx.annotation.RequiresApi
import com.example.assistexample.MainActivity
import com.example.assistexample.R
import com.example.assistexample.RequestRecordAudioPermission
import java.io.File
import kotlin.math.min

const val TAG = "ASSIST_EXAMPLE"

/**
 * Voice interaction session with ability to record audio.
 */
@RequiresApi(Build.VERSION_CODES.M)
class InteractionSession(context: Context) : VoiceInteractionSession(context) {
    private lateinit var recordAudioButton: View
    // Reflects on audio input changes. Allows to see if any input is started.
    private lateinit var recordAudioView: View
    private var audioRecorder: MediaRecorder? = null
    private var inputVolumeListener: CountDownTimer? = null

    override fun onCreate() {
        Log.d(TAG, "create voice interaction SESSION")
        super.onCreate()
    }

    override fun onHandleAssist(
        data: Bundle?,
        structure: AssistStructure?,
        content: AssistContent?
    ) {
        super.onHandleAssist(data, structure, content)
        Log.d(TAG, "data: $data, structure: $structure, content: $content")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateContentView(): View {
        Log.d(TAG, "create voice interaction SESSION CONTENT VIEW")

        val view = layoutInflater.inflate(R.layout.activity_assist, null)
        val openOtherAppButton = view.findViewById<Button>(R.id.open_other_app_button)
        val openMainActivityButton = view.findViewById<Button>(R.id.open_activity_button)
        val recordAudioPermissionButton = view.findViewById<Button>(R.id.record_permission_button)
        val crashButton = view.findViewById<Button>(R.id.crash_button)
        recordAudioButton = view.findViewById(R.id.record_audio_button)
        recordAudioView = view.findViewById(R.id.record_audio_view)

        openOtherAppButton.setOnClickListener {
            openOtherApp()
        }
        openMainActivityButton.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        recordAudioPermissionButton.setOnClickListener {
            requestRecordAudioPermission()
        }
        crashButton.setOnClickListener {
            throw RuntimeException("Simulate app crash.")
        }
        recordAudioButton.setOnClickListener {
            switchRecordAudio()
        }

        return view
    }

    private fun openOtherApp() {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage("YOUR APP")
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)
        context.startActivity(intent)
    }

    private fun switchRecordAudio() {
        if (audioRecorder != null) {
            stopRecordAudio()
        } else {
            startRecordAudio()
        }
    }

    private fun startRecordAudio() {
        val audioPath = getAudioPath()
        audioRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioPath)
            prepare()
            start()
        }
        inputVolumeListener = object : CountDownTimer(60_000, 100) {
            override fun onTick(p0: Long) {
                val volume = audioRecorder?.maxAmplitude ?: 0
                handleVolume(volume)
            }

            override fun onFinish() {}
        }.apply{ start() }
        Log.d(TAG, "file will be here: $audioPath")
    }

    private fun handleVolume(volume: Int) {
        val scale = min(20.0, volume * 5 / MAX_RECORD_AMPLITUDE + 1.0).toFloat()

        recordAudioView.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setInterpolator(interpolator)
            .duration= VOLUME_UPDATE_DURATION
    }

    private fun stopRecordAudio() {
        audioRecorder?.stop()
        audioRecorder?.release()
        inputVolumeListener?.cancel()
        audioRecorder = null
        inputVolumeListener = null
    }

    private fun requestRecordAudioPermission() {
        // There is no way to request permission from VoiceInteractionSession.
        context.startActivity(Intent(context, RequestRecordAudioPermission::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    /**
     * @return filepath of recorded audio.
     */
    private fun getAudioPath(): String {
        return "${context.cacheDir.absolutePath}${File.pathSeparator}${System.currentTimeMillis()}.wav"
    }

    override fun onDestroy() {
        Log.d(TAG, "destroy voice interaction SESSION")
        stopRecordAudio()
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onHide() {
        finish()
    }

    companion object {
        private const val MAX_RECORD_AMPLITUDE = 32768.0
        private const val VOLUME_UPDATE_DURATION = 100L
        private val interpolator = OvershootInterpolator()
    }
}
