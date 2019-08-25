package com.axel.reproductorenkotlin.ui.view

import android.arch.lifecycle.ReportFragment
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Cancion
import com.axel.reproductorenkotlin.ui.view.adapter.ViewPageAdapter
import com.axel.reproductorenkotlin.ui.view.adapter.itemViewPager
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.INavigationHost
import kotlinx.android.synthetic.main.fragment_reproductor.*
import kotlinx.android.synthetic.main.fragment_reproductor.view.*
import java.lang.Exception
import java.util.*
import android.R



class MainFragment: ReproductorFragment() {

    lateinit var playPausa: Button
    lateinit var btnRepetir: Button
    //lateinit var portada: ImageView
    lateinit var barraProgreso: SeekBar
    lateinit var txtMinuto: TextView
    lateinit var txtDuracion: TextView
    lateinit var txtNombre: TextView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var canciones: MutableList<Cancion>
    var repetir: Int = 2
    var minutoMilisegundos: Float = 0.0f
    var duracionMilisegundos: Float = 0.0f
    lateinit var hiloplay: HiloPlay
    var ejecutar = true
    var iteradorSegundos: Float = 0.0f
    companion object {
        var posicion = 0
    }

    override fun OnBackPressFragment(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reproductor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playPausa = view.findViewById(R.id.btnPlayPause)
        btnRepetir = view.findViewById(R.id.btnRepetir)
        //portada = view.findViewById(R.id.imageView)
        barraProgreso = view.findViewById(R.id.seekBar)
        txtMinuto = view.findViewById(R.id.txtMinuto)
        txtDuracion = view.findViewById(R.id.txtDuracion)
        txtNombre = view.findViewById(R.id.txtNombre)
        canciones = LinkedList()


        view.btnDetener.setOnClickListener{
            detener()
        }

        view.btnSiguiente.setOnClickListener {
            siguiente()
        }

        view.btnMinimizar.setOnClickListener {
            listaDeReproduccion()
        }

        view.btnAnterior.setOnClickListener {
            anterior()
        }

        view.btnRepetir.setOnClickListener {
            repetir()
        }

        view.btnPlayPause.setOnClickListener {
            playPause()
        }

        canciones.add(
            Cancion(
                R.raw.edsheerancastleonthehill,
                R.drawable.castleonthehill,
                "Castle on the hill"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheerandive,
                R.drawable.dive,
                "Dive"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheerangalwaygirl,
                R.drawable.galwaygirl,
                "Galway girl"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheerangivemelove,
                R.drawable.givemelove,
                "Give me love"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheeranhowwouldyoufeel,
                R.drawable.howwouldyoufeel,
                "How would you feel"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheeranlegohouse,
                R.drawable.legohouse,
                "Lego house"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheeranone,
                R.drawable.one,
                "One"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheeranperfect,
                R.drawable.perfect,
                "Perfect"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheerantheateam,
                R.drawable.theateam,
                "The a team"
            )
        )
        canciones.add(
            Cancion(
                R.raw.edsheeranthinkingoutloud,
                R.drawable.thinkingoutloud,
                "Thinking out loud"
            )
        )
        canciones.add(
            Cancion(
                R.raw.happieredsheeranlyric,
                R.drawable.happier,
                "Happier"
            )
        )
        canciones.add(
            Cancion(
                R.raw.photographedsheeran,
                R.drawable.photograph,
                "Photograph"
            )
        )
        mediaPlayer = MediaPlayer.create(this.context, canciones.get(MainFragment.posicion).getCancion())

        txtNombre.text = canciones.get(MainFragment.posicion).getNombre()

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

        var listado: MutableList<itemViewPager> = LinkedList()
        for(i in 0 until (canciones.size-1)){
            listado.add(itemViewPager(
                    "cancion ${i}",
                    PortadaFragment.newInstance(i, canciones)
                )
            )
        }
        val adapter = ViewPageAdapter(childFragmentManager, listado)
        viewpager.adapter = adapter


        //TODO: TENGO QUE AGREGAR FUNCIONES DE CAMBIAR CANCION AL VIEWPAGER
        viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {

            }

        })

        /*navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.itemIconTintList = null*/

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
                siguienteAutomatico()
            }
        }
    }

    //Método para reproducir y pausar
    fun playPause(){
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
    fun listaDeReproduccion(){
        //todo: terminar
        (activity as INavigationHost).navigateTo(NavigationFragment.newInstance(canciones[0].getNombre()), true)
    }

    //Método para repetir la canción
    //todo: por el momento no funciona
    fun repetir(){
        if(repetir == 1){
            btnRepetir.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this.context, "No repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.setLooping(false)
            repetir = 2
        } else{
            btnRepetir.setBackgroundResource(R.drawable.repetir)
            Toast.makeText(this.context, "Repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.setLooping(true)
            repetir = 1
        }
    }

    //Método para volver a la canción anterior
    fun anterior(){
        if(posicion >= 1){
            if(mediaPlayer.isPlaying()){
                iteradorSegundos = 0.0f

                try{
                    mediaPlayer.reset()
                    mediaPlayer.prepare()
                    mediaPlayer.stop()
                    mediaPlayer.release()

                } catch (e: Exception){
                    e.printStackTrace()
                }

                canciones.set(0,
                    Cancion(
                        R.raw.edsheerancastleonthehill,
                        R.drawable.castleonthehill,
                        "Castle on the hill"
                    )
                )
                canciones.set(1,
                    Cancion(
                        R.raw.edsheerandive,
                        R.drawable.dive,
                        "Dive"
                    )
                )
                canciones.set(2,
                    Cancion(
                        R.raw.edsheerangalwaygirl,
                        R.drawable.galwaygirl,
                        "Galway girl"
                    )
                )
                canciones.set(3,
                    Cancion(
                        R.raw.edsheerangivemelove,
                        R.drawable.givemelove,
                        "Give me love"
                    )
                )
                canciones.set(4,
                    Cancion(
                        R.raw.edsheeranhowwouldyoufeel,
                        R.drawable.howwouldyoufeel,
                        "How would you feel"
                    )
                )
                canciones.set(5,
                    Cancion(
                        R.raw.edsheeranlegohouse,
                        R.drawable.legohouse,
                        "Lego house"
                    )
                )
                canciones.set(6,
                    Cancion(
                        R.raw.edsheeranone,
                        R.drawable.one,
                        "One"
                    )
                )
                canciones.set(7,
                    Cancion(
                        R.raw.edsheeranperfect,
                        R.drawable.perfect,
                        "Perfect"
                    )
                )
                canciones.set(8,
                    Cancion(
                        R.raw.edsheerantheateam,
                        R.drawable.theateam,
                        "The a team"
                    )
                )
                canciones.set(9,
                    Cancion(
                        R.raw.edsheeranthinkingoutloud,
                        R.drawable.thinkingoutloud,
                        "Thinking out loud"
                    )
                )
                canciones.set(10,
                    Cancion(
                        R.raw.happieredsheeranlyric,
                        R.drawable.happier,
                        "Happier"
                    )
                )
                canciones.set(11,
                    Cancion(
                        R.raw.photographedsheeran,
                        R.drawable.photograph,
                        "Photograph"
                    )
                )

                posicion--
                mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())
                barraProgreso.setProgress(0)
                minutoMilisegundos = 0.0f
                txtMinuto.text = "00:00"
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                barraProgreso.max = duracionMilisegundos.toInt()
                txtDuracion.text = pasarAminutos(mediaPlayer.duration.toFloat())
                mediaPlayer.start()

                //portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            } else{
                posicion--
                mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                txtDuracion.text = pasarAminutos(duracionMilisegundos)

                //portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            }
        } else{
            Toast.makeText(this.context, "No hay canciones anteriores", Toast.LENGTH_SHORT).show()
        }
    }

    //Método para ir a la siguiente canción
    fun siguiente(){
        if(posicion < canciones.size -1){
            if(mediaPlayer.isPlaying){
                iteradorSegundos = 0.0f

                try{
                    mediaPlayer.reset()
                    mediaPlayer.prepare()
                    mediaPlayer.stop()
                    mediaPlayer.release()
                } catch (e: Exception){
                    e.printStackTrace()
                }

                canciones.set(0,
                    Cancion(
                        R.raw.edsheerancastleonthehill,
                        R.drawable.castleonthehill,
                        "Castle on the hill"
                    )
                )
                canciones.set(1,
                    Cancion(
                        R.raw.edsheerandive,
                        R.drawable.dive,
                        "Dive"
                    )
                )
                canciones.set(2,
                    Cancion(
                        R.raw.edsheerangalwaygirl,
                        R.drawable.galwaygirl,
                        "Galway girl"
                    )
                )
                canciones.set(3,
                    Cancion(
                        R.raw.edsheerangivemelove,
                        R.drawable.givemelove,
                        "Give me love"
                    )
                )
                canciones.set(4,
                    Cancion(
                        R.raw.edsheeranhowwouldyoufeel,
                        R.drawable.howwouldyoufeel,
                        "How would you feel"
                    )
                )
                canciones.set(5,
                    Cancion(
                        R.raw.edsheeranlegohouse,
                        R.drawable.legohouse,
                        "Lego house"
                    )
                )
                canciones.set(6,
                    Cancion(
                        R.raw.edsheeranone,
                        R.drawable.one,
                        "One"
                    )
                )
                canciones.set(7,
                    Cancion(
                        R.raw.edsheeranperfect,
                        R.drawable.perfect,
                        "Perfect"
                    )
                )
                canciones.set(8,
                    Cancion(
                        R.raw.edsheerantheateam,
                        R.drawable.theateam,
                        "The a team"
                    )
                )
                canciones.set(9,
                    Cancion(
                        R.raw.edsheeranthinkingoutloud,
                        R.drawable.thinkingoutloud,
                        "Thinking out loud"
                    )
                )
                canciones.set(10,
                    Cancion(
                        R.raw.happieredsheeranlyric,
                        R.drawable.happier,
                        "Happier"
                    )
                )
                canciones.set(11,
                    Cancion(
                        R.raw.photographedsheeran,
                        R.drawable.photograph,
                        "Photograph"
                    )
                )

                posicion++
                mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())
                barraProgreso.setProgress(0)
                minutoMilisegundos = 0.0f
                txtMinuto.text = "00:00"
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                barraProgreso.max = duracionMilisegundos.toInt()
                txtDuracion.text = pasarAminutos(duracionMilisegundos)
                mediaPlayer.start()

                //portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            } else{
                posicion++
                mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())
                duracionMilisegundos = mediaPlayer.duration.toFloat()
                txtDuracion.text = pasarAminutos(duracionMilisegundos)

                //portada.setImageResource(canciones.get(posicion).getPortada())
                txtNombre.text = canciones.get(posicion).getNombre()
            }
        } else{
            Toast.makeText(this.context, "No hay más canciones", Toast.LENGTH_SHORT).show()
        }
    }

    //Método para ir a la canción siguiente cuando termine la canción
    fun siguienteAutomatico(){
        if(posicion < canciones.size - 1){
            iteradorSegundos = 0.0f
            try {
                mediaPlayer.reset()
                mediaPlayer.prepare()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch(e: Exception){
                e.printStackTrace()
            }

            canciones.set(0,
                Cancion(
                    R.raw.edsheerancastleonthehill,
                    R.drawable.castleonthehill,
                    "Castle on the hill"
                )
            )
            canciones.set(1,
                Cancion(
                    R.raw.edsheerandive,
                    R.drawable.dive,
                    "Dive"
                )
            )
            canciones.set(2,
                Cancion(
                    R.raw.edsheerangalwaygirl,
                    R.drawable.galwaygirl,
                    "Galway girl"
                )
            )
            canciones.set(3,
                Cancion(
                    R.raw.edsheerangivemelove,
                    R.drawable.givemelove,
                    "Give me love"
                )
            )
            canciones.set(4,
                Cancion(
                    R.raw.edsheeranhowwouldyoufeel,
                    R.drawable.howwouldyoufeel,
                    "How would you feel"
                )
            )
            canciones.set(5,
                Cancion(
                    R.raw.edsheeranlegohouse,
                    R.drawable.legohouse,
                    "Lego house"
                )
            )
            canciones.set(6,
                Cancion(
                    R.raw.edsheeranone,
                    R.drawable.one,
                    "One"
                )
            )
            canciones.set(7,
                Cancion(
                    R.raw.edsheeranperfect,
                    R.drawable.perfect,
                    "Perfect"
                )
            )
            canciones.set(8,
                Cancion(
                    R.raw.edsheerantheateam,
                    R.drawable.theateam,
                    "The a team"
                )
            )
            canciones.set(9,
                Cancion(
                    R.raw.edsheeranthinkingoutloud,
                    R.drawable.thinkingoutloud,
                    "Thinking out loud"
                )
            )
            canciones.set(10,
                Cancion(
                    R.raw.happieredsheeranlyric,
                    R.drawable.happier,
                    "Happier"
                )
            )
            canciones.set(11,
                Cancion(
                    R.raw.photographedsheeran,
                    R.drawable.photograph,
                    "Photograph"
                )
            )

            posicion++
            mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())
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

            activity?.runOnUiThread(object : Runnable {
                override fun run() {
                    //portada.setImageResource(canciones.get(posicion).getPortada())
                    txtNombre.text = canciones.get(posicion).getNombre()
                }
            })
        } else{
            activity?.runOnUiThread(object: Runnable{
                override fun run() {
                    Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                    btnPlayPause.setBackgroundResource(R.drawable.playy)
                    barraProgreso.progress = 0
                    txtMinuto.text = "00:00"
                    ejecutar = false
                    try{
                        mediaPlayer.reset()
                        mediaPlayer.prepare()
                        mediaPlayer.stop()
                        mediaPlayer.release()
                    } catch(e: Exception){
                        e.printStackTrace()
                    }
                    hiloplay.interrupt()
                    posicion = canciones.size - 2
                    mediaPlayer = MediaPlayer.create(context, canciones.get(posicion).getCancion())
                    minutoMilisegundos = 0.0f
                }
            })
        }
    }

    //Método para detener la canción
    fun detener(){
        if(mediaPlayer.isPlaying){
            ejecutar = false
            try{
                mediaPlayer.reset()
                mediaPlayer.prepare()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch(e: Exception){
                e.printStackTrace()
            }

            hiloplay.interrupt()
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())

            txtMinuto.text = "00:00"
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f

            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = pasarAminutos(duracionMilisegundos)

            canciones.set(0,
                Cancion(
                    R.raw.edsheerancastleonthehill,
                    R.drawable.castleonthehill,
                    "Castle on the hill"
                )
            )
            canciones.set(1,
                Cancion(
                    R.raw.edsheerandive,
                    R.drawable.dive,
                    "Dive"
                )
            )
            canciones.set(2,
                Cancion(
                    R.raw.edsheerangalwaygirl,
                    R.drawable.galwaygirl,
                    "Galway girl"
                )
            )
            canciones.set(3,
                Cancion(
                    R.raw.edsheerangivemelove,
                    R.drawable.givemelove,
                    "Give me love"
                )
            )
            canciones.set(4,
                Cancion(
                    R.raw.edsheeranhowwouldyoufeel,
                    R.drawable.howwouldyoufeel,
                    "How would you feel"
                )
            )
            canciones.set(5,
                Cancion(
                    R.raw.edsheeranlegohouse,
                    R.drawable.legohouse,
                    "Lego house"
                )
            )
            canciones.set(6,
                Cancion(
                    R.raw.edsheeranone,
                    R.drawable.one,
                    "One"
                )
            )
            canciones.set(7,
                Cancion(
                    R.raw.edsheeranperfect,
                    R.drawable.perfect,
                    "Perfect"
                )
            )
            canciones.set(8,
                Cancion(
                    R.raw.edsheerantheateam,
                    R.drawable.theateam,
                    "The a team"
                )
            )
            canciones.set(9,
                Cancion(
                    R.raw.edsheeranthinkingoutloud,
                    R.drawable.thinkingoutloud,
                    "Thinking out loud"
                )
            )
            canciones.set(10,
                Cancion(
                    R.raw.happieredsheeranlyric,
                    R.drawable.happier,
                    "Happier"
                )
            )
            canciones.set(11,
                Cancion(
                    R.raw.photographedsheeran,
                    R.drawable.photograph,
                    "Photograph"
                )
            )

            playPausa.setBackgroundResource(R.drawable.playy)
            //portada.setImageResource(canciones.get(posicion).getPortada())
            txtNombre.text = canciones.get(posicion).getNombre()
        } else{
            ejecutar = false
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, canciones.get(posicion).getCancion())
            txtMinuto.text = "00:00"
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f
            hiloplay.interrupt()
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = pasarAminutos(duracionMilisegundos)

            //portada.setImageResource(canciones.get(posicion).getPortada())
            txtNombre.text = canciones.get(posicion).getNombre()
        }
    }

}