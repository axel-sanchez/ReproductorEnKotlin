package com.axel.reproductorenkotlin.ui.view

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.axel.reproductorenkotlin.viewmodel.PlayerViewModel
import java.util.*

class PlayerFragment : Fragment() {

    lateinit var mediaPlayer: MediaPlayer
    var repeat: Int = 2
    var position: Int = 0
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiver: BroadcastReceiver

    private val viewModel: PlayerViewModel by activityViewModels()

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

        setUpObserverPlayer()

        setUpListeners()

        mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)
        binding.txtName.text = songsList[position].name

        adapterViewPager()

        setOnPageChangeListener()
    }

    private fun setOnPageChangeListener() {
        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                if (position < this@PlayerFragment.position) previousSong()
                else nextSong()
            }
        })
    }

    private fun adapterViewPager() {
        viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat() //En milisegundos
        binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.time.durationMilliSeconds)
        binding.progressBar.max = viewModel.time.durationMilliSeconds.toInt()

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
    }

    private fun setUpListeners(){
        binding.btnDetener.setOnClickListener { stopSong() }

        binding.btnSiguiente.setOnClickListener {
            if(position == songsList.size -1) Toast.makeText(requireContext(), "There aren't next songs", Toast.LENGTH_SHORT).show()
            else binding.viewpager.currentItem = binding.viewpager.currentItem + 1
        }

        binding.btnMinimizar.setOnClickListener { navigateToPlaylistFragment() }

        binding.btnAnterior.setOnClickListener {
            if(position == 0) Toast.makeText(requireContext(), "There aren't previous songs", Toast.LENGTH_SHORT).show()
            else binding.viewpager.currentItem = binding.viewpager.currentItem - 1
        }

        binding.btnRepeat.setOnClickListener { repeatSong() }

        binding.btnPlayPause.setOnClickListener { playPauseSong() }

        binding.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.time.iteratorSeconds = progress / 1000f
                    mediaPlayer.seekTo(progress)
                    viewModel.time.minuteMilliSeconds = viewModel.time.iteratorSeconds * 1000
                    binding.txtMinute.text = millisToMinutesAndSeconds(viewModel.time.minuteMilliSeconds)
                    binding.progressBar.progress = viewModel.time.minuteMilliSeconds.toInt()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setUpObserverPlayer() {
        viewModel.getTimeLiveData().observe(viewLifecycleOwner, {
            if (it.requireNextSong) nextSongAuto()
            else{
                binding.txtMinute.text = millisToMinutesAndSeconds(it.minuteMilliSeconds)
                binding.progressBar.progress = it.minuteMilliSeconds.toInt()
            }
        })
    }

    private fun playPauseSong() {
        if (mediaPlayer.isPlaying) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            viewModel.canExecute = false
            mediaPlayer.pause()
            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
        } else {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, intentFilter)
            mediaPlayer.start()
            binding.btnPlayPause.setBackgroundResource(R.drawable.pause)
            viewModel.canExecute = true

            binding.txtMinute.text = millisToMinutesAndSeconds(viewModel.time.minuteMilliSeconds)
            binding.progressBar.progress = viewModel.time.minuteMilliSeconds.toInt()
            viewModel.getTime()
        }
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

    private fun previousSong() {
        if (mediaPlayer.isPlaying) {
            viewModel.time.iteratorSeconds = 0.0f

            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            position--
            mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)
            binding.progressBar.progress = 0
            viewModel.time.minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.progressBar.max = viewModel.time.durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(mediaPlayer.duration.toFloat())
            mediaPlayer.start()

            binding.txtName.text = songsList[position].name
        } else {
            position--
            mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)
            viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.time.durationMilliSeconds)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[position].name
        }
    }

    private fun nextSong() {
        if (mediaPlayer.isPlaying || viewModel.time.requireNextSong) {
            viewModel.time.iteratorSeconds = 0.0f

            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) {
                println("OCURRIÓ UN ERROR")
                e.printStackTrace()
            }

            position++
            mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)
            binding.progressBar.progress = 0
            viewModel.time.minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.progressBar.max = viewModel.time.durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.time.durationMilliSeconds)
            mediaPlayer.start()

            binding.txtName.text = songsList[position].name
        } else {
            position++
            mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)
            viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.time.durationMilliSeconds)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[position].name
        }
    }

    private fun nextSongAuto() {
        if (position < songsList.size - 1) {
            binding.viewpager.currentItem = binding.viewpager.currentItem + 1
        } else {
            activity?.runOnUiThread {
                Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
                binding.progressBar.progress = 0
                binding.txtMinute.text = resources.getString(R.string.minuto)
                viewModel.canExecute = false
                try {
                    mediaPlayer.reset()
                    mediaPlayer.prepareAsync()
                    mediaPlayer.stop()
                    mediaPlayer.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                position = songsList.size - 2
                mediaPlayer = MediaPlayer.create(context, songsList[position].song)
                viewModel.time.minuteMilliSeconds = 0.0f
            }
        }
    }

    private fun stopSong() {
        if (mediaPlayer.isPlaying) {
            viewModel.canExecute = false
            try {
                mediaPlayer.reset()
                mediaPlayer.prepareAsync()
                mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)

            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            viewModel.time.minuteMilliSeconds = 0.0f
            viewModel.time.iteratorSeconds = 0.0f

            viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.time.durationMilliSeconds)

            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
            binding.txtName.text = songsList[position].name
        } else {
            viewModel.canExecute = false
            mediaPlayer = MediaPlayer.create(this.context, songsList[position].song)
            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            viewModel.time.minuteMilliSeconds = 0.0f
            viewModel.time.iteratorSeconds = 0.0f
            viewModel.time.durationMilliSeconds = mediaPlayer.duration.toFloat()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.time.durationMilliSeconds)

            binding.txtName.text = songsList[position].name
        }
    }
}