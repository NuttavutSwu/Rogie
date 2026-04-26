package com.rogie.threekingdoms.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.rogie.threekingdoms.R

object SoundManager {
    private var soundPool: SoundPool? = null
    private val sounds = mutableMapOf<String, Int>()

    fun init(context: Context) {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attrs)
            .build()

        // These would normally be raw resource IDs
        // sounds["click"] = soundPool!!.load(context, R.raw.snd_click, 1)
        // sounds["attack"] = soundPool!!.load(context, R.raw.snd_attack, 1)
    }

    fun play(key: String) {
        sounds[key]?.let { soundPool?.play(it, 1f, 1f, 0, 0, 1f) }
    }
}
