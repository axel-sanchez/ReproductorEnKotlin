package com.axel.reproductorenkotlin.ui.view

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
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
import com.axel.reproductorenkotlin.data.models.Song
import com.axel.reproductorenkotlin.helpers.HeadphonesDisconnectReceiver
import com.axel.reproductorenkotlin.ui.view.adapter.ItemViewPager
import com.axel.reproductorenkotlin.ui.view.adapter.ViewPageAdapter
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.INavigationHost
import kotlinx.android.synthetic.main.fragment_reproductor.*
import kotlinx.android.synthetic.main.fragment_reproductor.view.*
import java.util.*

class MainFragment : ReproductorFragment() {

    lateinit var btnPlayPause: Button
    lateinit var btnRepeat: Button
    lateinit var progressBar: SeekBar
    lateinit var txtMinute: TextView
    lateinit var txtDuration: TextView
    lateinit var txtName: TextView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var songsList: MutableList<Song>
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

        btnPlayPause = view.findViewById(R.id.btnPlayPause)
        btnRepeat = view.findViewById(R.id.btnRepetir)
        progressBar = view.findViewById(R.id.seekBar)
        txtMinute = view.findViewById(R.id.txtMinuto)
        txtDuration = view.findViewById(R.id.txtDuracion)
        txtName = view.findViewById(R.id.txtNombre)
        songsList = LinkedList()

        view.btnDetener.setOnClickListener { stopSong() }

        view.btnSiguiente.setOnClickListener { viewpager.currentItem = viewpager.currentItem + 1 }

        view.btnMinimizar.setOnClickListener { navigateToPlaylistFragment() }

        view.btnAnterior.setOnClickListener { viewpager.currentItem = viewpager.currentItem - 1 }

        view.btnRepetir.setOnClickListener { repeatSong() }

        view.btnPlayPause.setOnClickListener { playPauseSong() }

        songsList.add(Song(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"))
        songsList.add(Song(R.raw.edsheerandive, R.drawable.dive, "Dive"))
        songsList.add(Song(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"))
        songsList.add(Song(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"))
        songsList.add(Song(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"))
        songsList.add(Song(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"))
        songsList.add(Song(R.raw.edsheeranone, R.drawable.one, "One"))
        songsList.add(Song(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"))
        songsList.add(Song(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"))
        songsList.add(Song(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"))
        songsList.add(Song(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"))
        songsList.add(Song(R.raw.photographedsheeran, R.drawable.photograph, "Photograph"))

        mediaPlayer = MediaPlayer.create(this.context, songsList[copyPosicion].song)

        txtName.text = songsList[copyPosicion].name

        duracionMilisegundos = mediaPlayer.duration.toFloat() //En milisegundos
        txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
        progressBar.max = duracionMilisegundos.toInt()

        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    iteradorSegundos = progress / 1000f
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val listViewPager: MutableList<ItemViewPager> = LinkedList()
        for (i in 0 until (songsList.size)) {
            listViewPager.add(
                ItemViewPager(
                    "cancion $i",
                    PortadaFragment.newInstance(i, songsList)
                )
            )
        }
        val adapter = ViewPageAdapter(childFragmentManager, listViewPager)
        viewpager.adapter = adapter

        viewpager.pageMargin = -64

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(!isFirstTime) contador++
                else isFirstTime = false
                if(posicion == songsList.size - 1 && position == 10 && contador == 2) {
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
                if (position < posicion) previousSong()
                else nextSong()
            }
        })
    }

    //Hilo para la reproducción de canciones
    inner class ThreadPlay : Thread() {
        override fun run() {
            while (iteradorSegundos < (duracionMilisegundos / 1000f) && ejecutar) {
                minutoMilisegundos = iteradorSegundos * 1000 //En milisegundos
                txtMinute.text = millisToMinutesAndSeconds(minutoMilisegundos)
                progressBar.progress = minutoMilisegundos.toInt()
                iteradorSegundos++
                SystemClock.sleep(1000)
            }
            if (iteradorSegundos >= (duracionMilisegundos / 1000f)) {
                nextSongAuto()
            }
        }
    }

    private fun playPauseSong() {
        if (mediaPlayer.isPlaying) {
            LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
            ejecutar = false
            mediaPlayer.pause()
            btnPlayPause.setBackgroundResource(R.drawable.playy)
        } else {
            LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, intentFilter)
            mediaPlayer.start()
            btnPlayPause.setBackgroundResource(R.drawable.pause)
            ejecutar = true
            threadPlay = ThreadPlay()
            threadPlay.start()
        }
    }

    fun millisToMinutesAndSeconds(millis: Float): String {
        val minutes: Float = (millis / 1000f / 60f)
        val seconds: Float = (minutes - minutes.toInt()) * 60f
        val secondString: String
        val minuteString: String

        secondString = if (seconds.toInt() < 10) {
            "0${seconds.toInt()}"
        } else {
            "${seconds.toInt()}"
        }

        minuteString = if (minutes.toInt() < 10) {
            "0${minutes.toInt()}"
        } else {
            "${minutes.toInt()}"
        }

        return "$minuteString:$secondString"
    }

    private fun navigateToPlaylistFragment() {
        (activity as INavigationHost).navigateTo(
            NavigationFragment.newInstance(songsList[posicion].name),
            true
        )
    }

    //todo: por el momento no funciona
    private fun repeatSong() {
        if (repetir == 1) {
            btnRepeat.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this.context, "No repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = false
            repetir = 2
        } else {
            btnRepeat.setBackgroundResource(R.drawable.repetir)
            Toast.makeText(this.context, "Repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = true
            repetir = 1
        }
    }

    fun previousSong() {
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
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            progressBar.progress = 0
            minutoMilisegundos = 0.0f
            txtMinute.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            progressBar.max = duracionMilisegundos.toInt()
            txtDuration.text = millisToMinutesAndSeconds(mediaPlayer.duration.toFloat())
            mediaPlayer.start()

            txtName.text = songsList[posicion].name
        } else {
            posicion--
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            txtMinute.text = resources.getString(R.string.minuto)

            txtName.text = songsList[posicion].name
        }
    }

    fun nextSong() {
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
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            progressBar.progress = 0
            minutoMilisegundos = 0.0f
            txtMinute.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            progressBar.max = duracionMilisegundos.toInt()
            txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            mediaPlayer.start()

            txtName.text = songsList[posicion].name
        } else {
            posicion++
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            txtMinute.text = resources.getString(R.string.minuto)

            txtName.text = songsList[posicion].name
        }
    }

    fun nextSongAuto() {
        if (posicion < songsList.size - 1) {
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
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            progressBar.progress = 0
            minutoMilisegundos = 0.0f
            txtMinute.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            progressBar.max = duracionMilisegundos.toInt()
            txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            mediaPlayer.start()
            ejecutar = false
            threadPlay.interrupt()
            threadPlay = ThreadPlay()
            ejecutar = true
            threadPlay.start()

            activity?.runOnUiThread {
                txtName.text = songsList[posicion].name
                viewpager.currentItem = viewpager.currentItem + 1
            }
        } else {
            activity?.runOnUiThread {
                Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                btnPlayPause.setBackgroundResource(R.drawable.playy)
                progressBar.progress = 0
                txtMinute.text = resources.getString(R.string.minuto)
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
                posicion = songsList.size - 2
                mediaPlayer = MediaPlayer.create(context, songsList[posicion].song)
                minutoMilisegundos = 0.0f
            }
        }
    }

    private fun stopSong() {
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
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)

            txtMinute.text = resources.getString(R.string.minuto)
            progressBar.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f

            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)

            btnPlayPause.setBackgroundResource(R.drawable.playy)
            txtName.text = songsList[posicion].name
        } else {
            ejecutar = false
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            txtMinute.text = resources.getString(R.string.minuto)
            progressBar.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f
            threadPlay.interrupt()
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)

            txtName.text = songsList[posicion].name
        }
    }
}