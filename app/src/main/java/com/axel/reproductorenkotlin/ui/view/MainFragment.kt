package com.axel.reproductorenkotlin.ui.view

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Cancion
import com.axel.reproductorenkotlin.helpers.HeadphonesDisconnectReceiver
import com.axel.reproductorenkotlin.ui.view.adapter.ItemViewPager
import com.axel.reproductorenkotlin.ui.view.adapter.ViewPageAdapter
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.INavigationHost
import kotlinx.android.synthetic.main.fragment_reproductor.*
import kotlinx.android.synthetic.main.fragment_reproductor.view.*
import java.util.*

class MainFragment : ReproductorFragment() {

    lateinit var playPausa: Button
    lateinit var btnRepetir: Button
    lateinit var barraProgreso: SeekBar
    lateinit var txtMinuto: TextView
    lateinit var txtDuracion: TextView
    lateinit var txtNombre: TextView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var canciones: MutableList<Cancion>
    var repetir: Int = 2
    var minutoMilisegundos: Float = 0.0f
    var duracionMilisegundos: Float = 0.0f
    private lateinit var threadPlay: ThreadPlay
    var ejecutar = true
    var iteradorSegundos: Float = 0.0f
    var posicion: Int = 0
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiver: BroadcastReceiver
    var contador = 0
    var isFirstTime = true

    companion object {
        var copyPosicion = 0
    }

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        receiver = HeadphonesDisconnectReceiver()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reproductor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playPausa = view.findViewById(R.id.btnPlayPause)
        btnRepetir = view.findViewById(R.id.btnRepetir)
        barraProgreso = view.findViewById(R.id.seekBar)
        txtMinuto = view.findViewById(R.id.txtMinuto)
        txtDuracion = view.findViewById(R.id.txtDuracion)
        txtNombre = view.findViewById(R.id.txtNombre)
        canciones = LinkedList()

        view.btnDetener.setOnClickListener { stop() }

        view.btnSiguiente.setOnClickListener { viewpager.currentItem = viewpager.currentItem + 1 }

        view.btnMinimizar.setOnClickListener { listaDeReproduccion() }

        view.btnAnterior.setOnClickListener { viewpager.currentItem = viewpager.currentItem - 1 }

        view.btnRepetir.setOnClickListener { repetir() }

        view.btnPlayPause.setOnClickListener { playPause() }

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

        mediaPlayer = MediaPlayer.create(this.context, canciones[copyPosicion].getCancion())

        txtNombre.text = canciones[copyPosicion].getNombre()

        duracionMilisegundos = mediaPlayer.duration.toFloat() //En milisegundos
        txtDuracion.text = toMinutes(duracionMilisegundos)
        barraProgreso.max = duracionMilisegundos.toInt()

        barraProgreso.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    iteradorSegundos = progress / 1000f
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val listado: MutableList<ItemViewPager> = LinkedList()
        for (i in 0 until (canciones.size)) {
            listado.add(
                ItemViewPager(
                    "cancion $i",
                    PortadaFragment.newInstance(i, canciones)
                )
            )
        }
        val adapter = ViewPageAdapter(childFragmentManager, listado)
        viewpager.adapter = adapter

        viewpager.pageMargin = -64

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(!isFirstTime) contador++
                else isFirstTime = false
                if(posicion == canciones.size - 1 && position == 10 && contador == 2) {
                    Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                    contador = 0
                }
                else if(position == 0 && positionOffset == 0.0f && positionOffsetPixels == 0 && contador == 2){
                    Toast.makeText(context, "No hay canciones anteriores", Toast.LENGTH_SHORT).show()
                    contador = 0
                }
                if(contador == 3) contador = 0
                //TODO: ESTO NO ESTÁ ANDANDO BIEN
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                if (position < posicion) anterior()
                else next()
            }
        })
    }

    //Hilo para la reproducción de canciones
    inner class ThreadPlay : Thread() {
        override fun run() {
            while (iteradorSegundos < (duracionMilisegundos / 1000f) && ejecutar) {
                minutoMilisegundos = iteradorSegundos * 1000 //En milisegundos
                txtMinuto.text = toMinutes(minutoMilisegundos)
                barraProgreso.progress = minutoMilisegundos.toInt()
                iteradorSegundos++
                SystemClock.sleep(1000)
            }
            if (iteradorSegundos >= (duracionMilisegundos / 1000f)) {
                nextAuto()
            }
        }
    }

    //Método para reproducir y pausar
    private fun playPause() {
        if (mediaPlayer.isPlaying) {
            LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
            ejecutar = false
            mediaPlayer.pause()
            playPausa.setBackgroundResource(R.drawable.playy)
        } else {
            LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, intentFilter)
            mediaPlayer.start()
            playPausa.setBackgroundResource(R.drawable.pause)
            ejecutar = true
            threadPlay = ThreadPlay()
            threadPlay.start()
        }
    }

    //Método utilizado para pasar de milisegundos a minutos y segundos
    fun toMinutes(milis: Float): String {
        val minutos: Float = (milis / 1000f / 60f)
        val segundos: Float = (minutos - minutos.toInt()) * 60f
        val s: String
        val m: String

        s = if (segundos.toInt() < 10) {
            "0${segundos.toInt()}"
        } else {
            "${segundos.toInt()}"
        }

        m = if (minutos.toInt() < 10) {
            "0${minutos.toInt()}"
        } else {
            "${minutos.toInt()}"
        }

        return "$m:$s"
    }

    //Método para la lista de reproduccion
    private fun listaDeReproduccion() {
        (activity as INavigationHost).navigateTo(
            NavigationFragment.newInstance(canciones[posicion].getNombre()),
            true
        )
    }

    //Método para repetir la canción
    //todo: por el momento no funciona
    private fun repetir() {
        if (repetir == 1) {
            btnRepetir.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this.context, "No repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = false
            repetir = 2
        } else {
            btnRepetir.setBackgroundResource(R.drawable.repetir)
            Toast.makeText(this.context, "Repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = true
            repetir = 1
        }
    }

    //Método para volver a la canción anterior
    fun anterior() {
        if (mediaPlayer.isPlaying) {
            iteradorSegundos = 0.0f

            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            posicion--
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            txtMinuto.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            barraProgreso.max = duracionMilisegundos.toInt()
            txtDuracion.text = toMinutes(mediaPlayer.duration.toFloat())
            mediaPlayer.start()

            txtNombre.text = canciones[posicion].getNombre()
        } else {
            posicion--
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = toMinutes(duracionMilisegundos)
            txtMinuto.text = resources.getString(R.string.minuto)

            txtNombre.text = canciones[posicion].getNombre()
        }
    }

    //Método para ir a la siguiente canción
    fun next() {
        if (mediaPlayer.isPlaying) {
            iteradorSegundos = 0.0f

            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) {
                println("OCURRIÓ UN ERROR")
                e.printStackTrace()
            }

            posicion++
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            txtMinuto.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            barraProgreso.max = duracionMilisegundos.toInt()
            txtDuracion.text = toMinutes(duracionMilisegundos)
            mediaPlayer.start()

            txtNombre.text = canciones[posicion].getNombre()
        } else {
            posicion++
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = toMinutes(duracionMilisegundos)
            txtMinuto.text = resources.getString(R.string.minuto)

            txtNombre.text = canciones[posicion].getNombre()
        }
    }

    //Método para ir a la canción siguiente cuando termine la canción
    fun nextAuto() {
        if (posicion < canciones.size - 1) {
            iteradorSegundos = 0.0f
            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            posicion++
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            txtMinuto.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            barraProgreso.max = duracionMilisegundos.toInt()
            txtDuracion.text = toMinutes(duracionMilisegundos)
            mediaPlayer.start()
            ejecutar = false
            threadPlay.interrupt()
            threadPlay = ThreadPlay()
            ejecutar = true
            threadPlay.start()

            activity?.runOnUiThread {
                txtNombre.text = canciones[posicion].getNombre()
                viewpager.currentItem = viewpager.currentItem + 1
            }
        } else {
            activity?.runOnUiThread {
                Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                btnPlayPause.setBackgroundResource(R.drawable.playy)
                barraProgreso.progress = 0
                txtMinuto.text = resources.getString(R.string.minuto)
                ejecutar = false
                try {
                    mediaPlayer.reset()
                    mediaPlayer.prepareAsync()
                    mediaPlayer.stop()
                    mediaPlayer.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                threadPlay.interrupt()
                posicion = canciones.size - 2
                mediaPlayer = MediaPlayer.create(context, canciones[posicion].getCancion())
                minutoMilisegundos = 0.0f
            }
        }
    }

    //Método para detener la canción
    private fun stop() {
        if (mediaPlayer.isPlaying) {
            ejecutar = false
            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            threadPlay.interrupt()
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())

            txtMinuto.text = resources.getString(R.string.minuto)
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f

            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = toMinutes(duracionMilisegundos)

            playPausa.setBackgroundResource(R.drawable.playy)
            txtNombre.text = canciones[posicion].getNombre()
        } else {
            ejecutar = false
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, canciones[posicion].getCancion())
            txtMinuto.text = resources.getString(R.string.minuto)
            barraProgreso.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f
            threadPlay.interrupt()
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuracion.text = toMinutes(duracionMilisegundos)

            txtNombre.text = canciones[posicion].getNombre()
        }
    }
}