package com.monotoshghosh.safecall

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object objVibration {

    fun vibrate(context: Context, duration: Long = 200) {           // FUNC FOR VIBRATION
        val vib: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vib.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vib.vibrate(duration)
        }
    }
}