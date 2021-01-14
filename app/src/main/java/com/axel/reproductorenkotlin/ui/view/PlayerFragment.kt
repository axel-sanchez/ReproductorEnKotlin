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
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.databinding.FragmentPlayerBinding
import com.axel.reproductorenkotlin.helpers.HeadphonesDisconnectReceiver
import com.axel.reproductorenkotlin.helpers.SongHelper.songsList
import com.axel.reproductorenkotlin.ui.view.adapter.ItemViewPager
import com.axel.reproductorenkotlin.ui.view.adapter.ViewPageAdapter
import java.util.*

class PlayerFragment : Fragment() {

    lateinit var mediaPlayer: MediaPlayer
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
        var copyPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        receiver = HeadphonesDisconnectReceiver()

    }

    private var fragmentPlayerBinding: FragmentPlayerBinding? = null
    private val binding get() = fragmentPlayerBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentPlayerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPlayerBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetener.setOnClickListener { stopSong() }

        binding.btnSiguiente.setOnClickListener { binding.viewpager.currentItem = binding.viewpager.currentItem + 1 }

        binding.btnMinimizar.setOnClickListener { navigateToPlaylistFragment() }

        binding.btnAnterior.setOnClickListener { binding.viewpager.currentItem = binding.viewpager.currentItem - 1 }

        binding.btnRepeat.setOnClickListener { repeatSong() }

        binding.btnPlayPause.setOnClickListener { playPauseSong() }

        mediaPlayer = MediaPlayer.create(this.context, songsList[copyPosition].song)

        binding.txtName.text = songsList[copyPosition].name

        duracionMilisegundos = mediaPlayer.duration.toFloat() //En milisegundos
        binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
        binding.progressBar.max = duracionMilisegundos.toInt()

        binding.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
                    CoverPageFragment.newInstance(i)
                )
            )
        }
        val adapter = ViewPageAdapter(childFragmentManager, listViewPager)
        binding.viewpager.adapter = adapter

        binding.viewpager.pageMargin = -64

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                binding.txtMinute.text = millisToMinutesAndSeconds(minutoMilisegundos)
                binding.progressBar.progress = minutoMilisegundos.toInt()
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
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            ejecutar = false
            mediaPlayer.pause()
            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
        } else {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, intentFilter)
            mediaPlayer.start()
            binding.btnPlayPause.setBackgroundResource(R.drawable.pause)
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
        /*(activity as INavigationHost).navigateTo(
            PlaylistFragment.newInstance(songsList[posicion].name),
            true
        )*/
    }

    //todo: por el momento no funciona
    private fun repeatSong() {
        if (repetir == 1) {
            binding.btnRepeat.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this.context, "No repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = false
            repetir = 2
        } else {
            binding.btnRepeat.setBackgroundResource(R.drawable.repetir)
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
            binding.progressBar.progress = 0
            minutoMilisegundos = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.progressBar.max = duracionMilisegundos.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(mediaPlayer.duration.toFloat())
            mediaPlayer.start()

            binding.txtName.text = songsList[posicion].name
        } else {
            posicion--
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[posicion].name
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
            binding.progressBar.progress = 0
            minutoMilisegundos = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.progressBar.max = duracionMilisegundos.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            mediaPlayer.start()

            binding.txtName.text = songsList[posicion].name
        } else {
            posicion++
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[posicion].name
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
            binding.progressBar.progress = 0
            minutoMilisegundos = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.progressBar.max = duracionMilisegundos.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)
            mediaPlayer.start()
            ejecutar = false
            threadPlay.interrupt()
            threadPlay = ThreadPlay()
            ejecutar = true
            threadPlay.start()

            activity?.runOnUiThread {
                binding.txtName.text = songsList[posicion].name
                binding.viewpager.currentItem = binding.viewpager.currentItem + 1
            }
        } else {
            activity?.runOnUiThread {
                Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
                binding.progressBar.progress = 0
                binding.txtMinute.text = resources.getString(R.string.minuto)
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

            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f

            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)

            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
            binding.txtName.text = songsList[posicion].name
        } else {
            ejecutar = false
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            minutoMilisegundos = 0.0f
            iteradorSegundos = 0.0f
            threadPlay.interrupt()
            duracionMilisegundos = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(duracionMilisegundos)

            binding.txtName.text = songsList[posicion].name
        }
    }
}