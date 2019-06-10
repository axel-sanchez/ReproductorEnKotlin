package com.axel.reproductorenkotlin

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    var playPausa: Button = findViewById(R.id.btnPlayPause)
    var btnRepetir: Button = findViewById(R.id.btnRepetir)
    var portada: ImageView = findViewById(R.id.imageView)
    var repetir = 2
    companion object {
        var posicion = 0
    }
    var barraProgreso: SeekBar = findViewById(R.id.seekBar)
    var canciones: MutableList<Cancion> = LinkedList()
    var txtMinuto: TextView = findViewById(R.id.txtMinuto)
    var txtDuracion: TextView = findViewById(R.id.txtDuracion)
    var txtNombre: TextView = findViewById(R.id.txtNombre)
    var minutoMilisegundos: Float = 0.0f
    var duracionMilisegundos: Float = 0.0f
    var hiloplay: HiloPlay = HiloPlay()
    var ejecutar = true
    var iteradorSegundos: Float = 0.0f
    var mediaPlayer: MediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        canciones.add(Cancion(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"))
        canciones.add(Cancion(R.raw.edsheerandive, R.drawable.dive, "Dive"))
        canciones.add(Cancion(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"))
        canciones.add(Cancion(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"))
        canciones.add(Cancion(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"))
        canciones.add(Cancion(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"))
        canciones.add(Cancion(R.raw.edsheeranone, R.drawable.one, "One"))
        canciones.add(Cancion(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"))
        canciones.add(Cancion(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"))
        canciones.add(Cancion(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"))
        canciones.add(Cancion(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"))
        canciones.add(Cancion(R.raw.photographedsheeran, R.drawable.photograph, "Photograph"))

        txtNombre.text = canciones.get(posicion).getNombre()

        duracionMilisegundos = mediaPlayer.duration.toFloat() //En milisegundos
        txtDuracion.text = pasarAminutos(duracionMilisegundos)
        barraProgreso.max = duracionMilisegundos.toInt()

        barraProgreso.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    iteradorSegundos = progress /1000f
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        } )
    }

    //Hilo para la reproducción de canciones
    inner class HiloPlay: Thread(){
        override fun run() {
            while(iteradorSegundos < (duracionMilisegundos / 1000f) && ejecutar ){
                minutoMilisegundos = iteradorSegundos * 1000 //En milisegundos
                txtMinuto.text = pasarAminutos(minutoMilisegundos)
                barraProgreso.setProgress(minutoMilisegundos.toInt())
                iteradorSegundos++
                SystemClock.sleep(1000)
            }
            if(iteradorSegundos >= (duracionMilisegundos / 1000f)){
                siguiente()
            }
        }
    }

    //Método para reproducir y pausar
    fun playPause(view: View){
        if(mediaPlayer.isPlaying){
            ejecutar = false
            mediaPlayer.pause()
            playPausa.setBackgroundResource(R.drawable.playy)
        } else{
            mediaPlayer.start()
            playPausa.setBackgroundResource(R.drawable.pause)
            ejecutar = true
            hiloplay = HiloPlay()
            hiloplay.start()
        }
    }

    //Método utilizado para pasar de milisegundos a minutos y segundos
    fun pasarAminutos(milis: Float): String{
        var minutos: Float = (milis/1000f/60f)
        var segundos: Float = (minutos - minutos.toInt()) * 60f
        var s: String
        var m: String
        if( segundos.toInt() < 10){
            s = "0${segundos.toInt()}"
        } else{
            s = "${segundos.toInt()}"
        }
        if(minutos.toInt() < 10){
            m = "0${minutos.toInt()}"
        } else{
            m = "${minutos.toInt()}"
        }
        return "$m:$s"
    }

    //Método para la lista de reproduccion
    fun listaDeReproduccion(view: View){
        var intent = Intent()
        startActivity(intent)
    }

    //Método para repetir la canción
    fun repetir(view: View){
        if(repetir == 1){
            btnRepetir.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this, "No repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.setLooping(false)
            repetir = 2
        } else{
            btnRepetir.setBackgroundResource(R.drawable.repetir)
            Toast.makeText(this, "Repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.setLooping(true)
            repetir = 1
        }
    }

    //Método para volver a la canción anterior
    fun anterior(view: View){
        if(posicion >= 1){
            if(mediaPlayer.isPlaying()){
                iteradorSegundos = 0.0f

                try{
                    mediaPlayer.reset()
                    mediaPlayer.prepare()
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    //mediaPlayer = null TODO: CAPAZ DE ERROR

                } catch (e: Exception){
                    e.printStackTrace()
                }

                canciones.set(0, Cancion(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"))
                canciones.set(1, Cancion(R.raw.edsheerandive, R.drawable.dive, "Dive"))
                canciones.set(2, Cancion(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"))
                canciones.set(3, Cancion(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"))
                canciones.set(4, Cancion(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"))
                canciones.set(5, Cancion(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"))
                canciones.set(6, Cancion(R.raw.edsheeranone, R.drawable.one, "One"))
                canciones.set(7, Cancion(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"))
                canciones.set(8, Cancion(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"))
                canciones.set(9, Cancion(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"))
                canciones.set(10, Cancion(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"))
                canciones.set(11, Cancion(R.raw.photographedsheeran, R.drawable.photograph, "Photograph"))

                posicion--
                mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())
                barraProgreso.setProgress(0)
                minutoMilisegundos = 0.0f
                txtMinuto.text = "00:00"
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                barraProgreso.max = duracionMilisegundos.toInt()
                txtDuracion.text = pasarAminutos(mediaPlayer.duration.toFloat())
                mediaPlayer.start()

                portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            } else{
                posicion--
                mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                txtDuracion.text = pasarAminutos(duracionMilisegundos)

                portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            }
        } else{
            Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show()
        }
    }

    //Método para ir a la siguiente canción
    fun siguiente(view: View){
        if(posicion < canciones.size -1){
            if(mediaPlayer.isPlaying){
                iteradorSegundos = 0.0f

                try{
                    mediaPlayer.reset()
                    mediaPlayer.prepare()
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    //mediaPlayer = null TODO: PUEDE LARGAR UN ERROR
                } catch (e: Exception){
                    e.printStackTrace()
                }

                canciones.set(0, Cancion(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"))
                canciones.set(1, Cancion(R.raw.edsheerandive, R.drawable.dive, "Dive"))
                canciones.set(2, Cancion(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"))
                canciones.set(3, Cancion(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"))
                canciones.set(4, Cancion(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"))
                canciones.set(5, Cancion(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"))
                canciones.set(6, Cancion(R.raw.edsheeranone, R.drawable.one, "One"))
                canciones.set(7, Cancion(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"))
                canciones.set(8, Cancion(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"))
                canciones.set(9, Cancion(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"))
                canciones.set(10, Cancion(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"))
                canciones.set(11, Cancion(R.raw.photographedsheeran, R.drawable.photograph, "Photograph"))

                posicion++
                mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())
                barraProgreso.setProgress(0)
                minutoMilisegundos = 0.0f
                txtMinuto.text = "00:00"
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                barraProgreso.max = duracionMilisegundos.toInt()
                txtDuracion.text = pasarAminutos(duracionMilisegundos)
                mediaPlayer.start()

                portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            } else{
                posicion++
                mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                txtDuracion.text = pasarAminutos(duracionMilisegundos)

                portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            }
        } else{
            Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show()
        }
    }

    //Método para ir a la canción siguiente cuando termine la canción
    fun siguiente(){
        if(posicion < canciones.size){
            iteradorSegundos = 0.0f
            try {
                mediaPlayer.reset()
                mediaPlayer.prepare()
                mediaPlayer.stop()
                mediaPlayer.release()
                //mediaPlayer = null TODO: ESTA LÍNEA PUEDE DAR ERROR
            } catch(e: Exception){
                e.printStackTrace()
            }

            canciones.set(0, Cancion(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"))
            canciones.set(1, Cancion(R.raw.edsheerandive, R.drawable.dive, "Dive"))
            canciones.set(2, Cancion(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"))
            canciones.set(3, Cancion(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"))
            canciones.set(4, Cancion(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"))
            canciones.set(5, Cancion(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"))
            canciones.set(6, Cancion(R.raw.edsheeranone, R.drawable.one, "One"))
            canciones.set(7, Cancion(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"))
            canciones.set(8, Cancion(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"))
            canciones.set(9, Cancion(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"))
            canciones.set(10, Cancion(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"))
            canciones.set(11, Cancion(R.raw.photographedsheeran, R.drawable.photograph, "Photograph"))

            posicion++
            mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())
            barraProgreso.setProgress(0)
            minutoMilisegundos = 0.0f
            txtMinuto.text = "00:00"
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            barraProgreso.max = duracionMilisegundos.toInt()
            txtDuracion.text = pasarAminutos(duracionMilisegundos)
            mediaPlayer.start()
            ejecutar = false
            hiloplay.interrupt()
            hiloplay = HiloPlay()
            ejecutar = true
            hiloplay.start()
            runOnUiThread(object : Runnable {
                override fun run() {
                    portada.setImageResource(canciones.get(posicion).getPortada())
                    txtNombre.text = canciones.get(posicion).getNombre()
                }
            })
        } else{
            runOnUiThread(object: Runnable{
                override fun run() {
                    Toast.makeText(applicationContext, "No hay más canciones", Toast.LENGTH_SHORT).show() //TODO: NO ESTOY SEGURO DE SI ESTÁ BIEN
                }
            })
        }
    }

    //Método para detener la canción
    fun detener(view: View){
        if(mediaPlayer.isPlaying){
            ejecutar = false
            try{
                mediaPlayer.reset()
                mediaPlayer.prepare()
                mediaPlayer.stop()
                mediaPlayer.release()
                //mediaPlayer = null TODO: CAPAZ DE ERROR NO LO SÉ RICK
            } catch(e: Exception){
                e.printStackTrace()
            }

            hiloplay.interrupt()
            posicion = 0
            mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())

            txtMinuto.text = "00:00"
            barraProgreso.setProgress(0)
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f

            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = pasarAminutos(duracionMilisegundos)

            canciones.set(0, Cancion(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"))
            canciones.set(1, Cancion(R.raw.edsheerandive, R.drawable.dive, "Dive"))
            canciones.set(2, Cancion(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"))
            canciones.set(3, Cancion(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"))
            canciones.set(4, Cancion(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"))
            canciones.set(5, Cancion(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"))
            canciones.set(6, Cancion(R.raw.edsheeranone, R.drawable.one, "One"))
            canciones.set(7, Cancion(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"))
            canciones.set(8, Cancion(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"))
            canciones.set(9, Cancion(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"))
            canciones.set(10, Cancion(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"))
            canciones.set(11, Cancion(R.raw.photographedsheeran, R.drawable.photograph, "Photograph"))

            playPausa.setBackgroundResource(R.drawable.playy)
            portada.setImageResource(canciones.get(posicion).getPortada())
            txtNombre.text = canciones.get(posicion).getNombre()
        } else{
            ejecutar = false
            posicion = 0
            mediaPlayer = MediaPlayer.create(this, canciones.get(posicion).getCancion())
            txtMinuto.text = "00:00"
            barraProgreso.setProgress(0)
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f
            hiloplay.interrupt()
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = pasarAminutos(duracionMilisegundos)

            portada.setImageResource(canciones.get(posicion).getPortada())
            txtNombre.text = canciones.get(posicion).getNombre()
        }
    }
}


