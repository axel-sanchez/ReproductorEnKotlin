package com.axel.reproductorenkotlin.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast

class HeadphonesDisconnectReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("recibio el aviso")
        if (intent!!.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            println("i should to pause music")
            when (intent.getIntExtra("state", -1)) {
                0 -> Toast.makeText(context, "Auricular conectado", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(context, "Auricular desconectado", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Estado desconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}