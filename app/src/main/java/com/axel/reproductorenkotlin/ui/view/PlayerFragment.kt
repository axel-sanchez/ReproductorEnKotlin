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
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.databinding.FragmentPlayerBinding
import com.axel.reproductorenkotlin.helpers.HeadphonesDisconnectReceiver
import com.axel.reproductorenkotlin.helpers.PlayerHelper.millisToMinutesAndSeconds
import com.axel.reproductorenkotlin.helpers.SongHelper.songsList
import com.axel.reproductorenkotlin.ui.view.adapter.ItemViewPager
import com.axel.reproductorenkotlin.ui.view.adapter.ViewPageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PlayerFragment : Fragment() {

    lateinit var mediaPlayer: MediaPlayer
    var repeat: Int = 2
    var minuteMilliSeconds: Float = 0.0f
    var durationMilliSeconds: Float = 0.0f
    var ejecutar = true
    var iteratorSeconds: Float = 0.0f
    var posicion: Int = 0
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiver: BroadcastReceiver
    var contador = 0
    var isFirstTime = true

    private var canChangeView = true

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

        durationMilliSeconds = mediaPlayer.duration.toFloat() //En milisegundos
        binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)
        binding.progressBar.max = durationMilliSeconds.toInt()

        binding.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    iteratorSeconds = progress / 1000f
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
            CoroutineScope(Dispatchers.IO).launch{
                //TODO: DEBERÍA DE IMPLEMENTAR UN VIEW MODEL
                while (iteratorSeconds < (durationMilliSeconds / 1000f) && ejecutar) {
                    minuteMilliSeconds = iteratorSeconds * 1000 //En milisegundos
                    if(canChangeView){
                        binding.txtMinute.text = millisToMinutesAndSeconds(minuteMilliSeconds)
                        binding.progressBar.progress = minuteMilliSeconds.toInt()
                    }
                    iteratorSeconds++
                    SystemClock.sleep(1000)
                }
                if (iteratorSeconds >= (durationMilliSeconds / 1000f)) {
                    nextSongAuto()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        canChangeView = false
    }

    override fun onResume() {
        super.onResume()
        canChangeView = true
    }

    private fun navigateToPlaylistFragment() {
        findNavController().popBackStack()
    }

    //todo: por el momento no funciona
    private fun repeatSong() {
        if (repeat == 1) {
            binding.btnRepeat.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this.context, "No repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = false
            repeat = 2
        } else {
            binding.btnRepeat.setBackgroundResource(R.drawable.repetir)
            Toast.makeText(this.context, "Repetir", Toast.LENGTH_SHORT).show()
            mediaPlayer.isLooping = true
            repeat = 1
        }
    }

    fun previousSong() {
        if (mediaPlayer.isPlaying) {
            iteratorSeconds = 0.0f

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
            minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.progressBar.max = durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(mediaPlayer.duration.toFloat())
            mediaPlayer.start()

            binding.txtName.text = songsList[posicion].name
        } else {
            posicion--
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[posicion].name
        }
    }

    fun nextSong() {
        if (mediaPlayer.isPlaying) {
            iteratorSeconds = 0.0f

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
            minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.progressBar.max = durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)
            mediaPlayer.start()

            binding.txtName.text = songsList[posicion].name
        } else {
            posicion++
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[posicion].name
        }
    }

    private fun nextSongAuto() {
        if (posicion < songsList.size - 1) {
            iteratorSeconds = 0.0f
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
            minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.progressBar.max = durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)
            mediaPlayer.start()

            activity?.runOnUiThread {
                if(canChangeView){
                    binding.txtName.text = songsList[posicion].name
                    binding.viewpager.currentItem = binding.viewpager.currentItem + 1
                }
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
                posicion = songsList.size - 2
                mediaPlayer = MediaPlayer.create(context, songsList[posicion].song)
                minuteMilliSeconds = 0.0f
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

            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)

            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            minuteMilliSeconds = 0.0f
            iteratorSeconds = 0.0f

            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)

            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
            binding.txtName.text = songsList[posicion].name
        } else {
            ejecutar = false
            posicion = 0
            mediaPlayer = MediaPlayer.create(this.context, songsList[posicion].song)
            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            minuteMilliSeconds = 0.0f
            iteratorSeconds = 0.0f
            durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(durationMilliSeconds)

            binding.txtName.text = songsList[posicion].name
        }
    }
}