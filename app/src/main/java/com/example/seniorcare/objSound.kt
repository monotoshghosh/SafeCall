package com.example.seniorcare

import android.app.Activity
import android.media.MediaPlayer

object objSound {

    fun btnSound(activity: Activity){
        val mp: MediaPlayer = MediaPlayer.create(activity,R.raw.buttonpressed2)  //------------------- NEED TO DELETE---------------------
        mp.setVolume(0.3f,0.3f)   // SETTING THE VOLUME TO 30%
        mp.start()
        mp.setOnCompletionListener{       // ON COMPLETION RELEASE TO AVOID MEMORY LEAK
            mp.release()
        }
    }

    fun btnSoundStart(activity: Activity){
        val mp: MediaPlayer = MediaPlayer.create(activity,R.raw.siren1)  //------------------- NEED TO DELETE---------------------
        mp.setVolume(0.3f,0.3f)   // SETTING THE VOLUME TO 30%
        mp.start()
        mp.setOnCompletionListener{       // ON COMPLETION RELEASE TO AVOID MEMORY LEAK
            mp.release()
        }
    }

    fun btnSoundRemove(activity: Activity){
        val mp: MediaPlayer = MediaPlayer.create(activity,R.raw.remove1)  //------------------- NEED TO DELETE---------------------
        mp.setVolume(0.3f,0.3f)   // SETTING THE VOLUME TO 30%
        mp.start()
        mp.setOnCompletionListener{       // ON COMPLETION RELEASE TO AVOID MEMORY LEAK
            mp.release()
        }
    }

    fun btnSoundDialogOpen(activity: Activity){
        val mp: MediaPlayer = MediaPlayer.create(activity,R.raw.slide2)  //------------------- NEED TO DELETE---------------------
        mp.setVolume(0.3f,0.3f)   // SETTING THE VOLUME TO 30%
        mp.start()
        mp.setOnCompletionListener{       // ON COMPLETION RELEASE TO AVOID MEMORY LEAK
            mp.release()
        }
    }

}