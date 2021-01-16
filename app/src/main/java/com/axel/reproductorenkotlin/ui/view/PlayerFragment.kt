package com.axel.reproductorenkotlin.ui.view

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.media.AudioManager
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

    var repeat: Int = 2
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiver: BroadcastReceiver

    private val viewModel: PlayerViewModel by activityViewModels(
        factoryProducer = { PlayerViewModel.PlayerViewModelFactory(requireActivity().applicationContext) }
    )

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

        binding.txtName.text = songsList[viewModel.getPosition()].name

        if (viewModel.mediaPlayerIsPlaying()) {
            binding.btnPlayPause.setBackgroundResource(R.drawable.pause)
        } else{
            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
        }

        adapterViewPager()

        setOnPageChangeListener()
    }

    private fun setOnPageChangeListener() {
        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                if (position < viewModel.getPosition()) previousSong()
                else nextSong()
            }
        })
    }

    private fun adapterViewPager() {
        viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
        binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getTime().durationMilliSeconds)
        binding.progressBar.max = viewModel.getTime().durationMilliSeconds.toInt()

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
            if(viewModel.getPosition() == songsList.size -1) Toast.makeText(requireContext(), "There aren't next songs", Toast.LENGTH_SHORT).show()
            else binding.viewpager.currentItem = binding.viewpager.currentItem + 1
        }

        binding.btnMinimizar.setOnClickListener { navigateToPlaylistFragment() }

        binding.btnAnterior.setOnClickListener {
            if(viewModel.getPosition() == 0) Toast.makeText(requireContext(), "There aren't previous songs", Toast.LENGTH_SHORT).show()
            else binding.viewpager.currentItem = binding.viewpager.currentItem - 1
        }

        binding.btnRepeat.setOnClickListener { repeatSong() }

        binding.btnPlayPause.setOnClickListener { playPauseSong() }

        binding.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.getTime().iteratorSeconds = progress / 1000f
                    viewModel.setNewProgress(progress)
                    viewModel.getTime().minuteMilliSeconds = viewModel.getTime().iteratorSeconds * 1000
                    binding.txtMinute.text = millisToMinutesAndSeconds(viewModel.getTime().minuteMilliSeconds)
                    binding.progressBar.progress = viewModel.getTime().minuteMilliSeconds.toInt()
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
        if (viewModel.mediaPlayerIsPlaying()) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            viewModel.setCanExecute(false)
            viewModel.pauseSong()
            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
        } else {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, intentFilter)
            viewModel.playSong()
            binding.btnPlayPause.setBackgroundResource(R.drawable.pause)
            viewModel.setCanExecute(true)

            binding.txtMinute.text = millisToMinutesAndSeconds(viewModel.getTime().minuteMilliSeconds)
            binding.progressBar.progress = viewModel.getTime().minuteMilliSeconds.toInt()
            viewModel.runSong()
        }
    }

    private fun navigateToPlaylistFragment() {
        findNavController().popBackStack()
    }

    //todo: por el momento no funciona
    private fun repeatSong() {
        repeat = if (repeat == 1) {
            binding.btnRepeat.setBackgroundResource(R.drawable.repeticion)
            Toast.makeText(this.context, "No repetir", Toast.LENGTH_SHORT).show()
            viewModel.setMediaPlayerLooping(false)
            2
        } else {
            binding.btnRepeat.setBackgroundResource(R.drawable.repetir)
            Toast.makeText(this.context, "Repetir", Toast.LENGTH_SHORT).show()
            viewModel.setMediaPlayerLooping(true)
            1
        }
    }

    private fun previousSong() {
        if (viewModel.mediaPlayerIsPlaying()) {
            viewModel.getTime().iteratorSeconds = 0.0f

            viewModel.resetAndPrepareMediaPlayer()

            viewModel.setPreviousPosition()
            viewModel.initializeMediaPlayer()
            binding.progressBar.progress = 0
            viewModel.getTime().minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
            binding.progressBar.max = viewModel.getTime().durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getSongDuration())
            viewModel.playSong()

            binding.txtName.text = songsList[viewModel.getPosition()].name
        } else {
            viewModel.setPreviousPosition()
            viewModel.initializeMediaPlayer()
            viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getTime().durationMilliSeconds)
            binding.txtMinute.text = resources.getString(R.string.minuto)

            binding.txtName.text = songsList[viewModel.getPosition()].name
        }
    }

    private fun nextSong() {
        if (viewModel.mediaPlayerIsPlaying() || viewModel.getTime().requireNextSong) {
            viewModel.getTime().iteratorSeconds = 0.0f

            viewModel.resetAndPrepareMediaPlayer()

            viewModel.setNextPosition()
            viewModel.initializeMediaPlayer()
            binding.progressBar.progress = 0
            viewModel.getTime().minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
            binding.progressBar.max = viewModel.getTime().durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getTime().durationMilliSeconds)
            viewModel.playSong()

            binding.txtName.text = songsList[viewModel.getPosition()].name
        } else {
            viewModel.setNextPosition()
            viewModel.initializeMediaPlayer()
            binding.progressBar.progress = 0
            viewModel.getTime().minuteMilliSeconds = 0.0f
            binding.txtMinute.text = resources.getString(R.string.minuto)
            viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
            binding.progressBar.max = viewModel.getTime().durationMilliSeconds.toInt()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getTime().durationMilliSeconds)

            binding.txtName.text = songsList[viewModel.getPosition()].name
        }
    }

    private fun nextSongAuto() {
        if (viewModel.getPosition() < songsList.size - 1) {
            binding.viewpager.currentItem = binding.viewpager.currentItem + 1
        } else {
            activity?.runOnUiThread {
                Toast.makeText(context, "No hay más canciones", Toast.LENGTH_SHORT).show()
                binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
                binding.progressBar.progress = 0
                binding.txtMinute.text = resources.getString(R.string.minuto)
                viewModel.setCanExecute(false)

                viewModel.resetAndPrepareMediaPlayer()

                viewModel.setPosition(songsList.size - 2)
                viewModel.initializeMediaPlayer()
                viewModel.getTime().minuteMilliSeconds = 0.0f
            }
        }
    }

    private fun stopSong() {
        if (viewModel.mediaPlayerIsPlaying()) {
            viewModel.setCanExecute(false)

            viewModel.resetAndPrepareMediaPlayer()

            viewModel.initializeMediaPlayer()

            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            viewModel.getTime().minuteMilliSeconds = 0.0f
            viewModel.getTime().iteratorSeconds = 0.0f

            viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getTime().durationMilliSeconds)

            binding.btnPlayPause.setBackgroundResource(R.drawable.playy)
            binding.txtName.text = songsList[viewModel.getPosition()].name
        } else {
            viewModel.setCanExecute(false)
            viewModel.initializeMediaPlayer()
            binding.txtMinute.text = resources.getString(R.string.minuto)
            binding.progressBar.progress = 0
            viewModel.getTime().minuteMilliSeconds = 0.0f
            viewModel.getTime().iteratorSeconds = 0.0f
            viewModel.getTime().durationMilliSeconds = viewModel.getSongDuration()
            binding.txtDuration.text = millisToMinutesAndSeconds(viewModel.getTime().durationMilliSeconds)

            binding.txtName.text = songsList[viewModel.getPosition()].name
        }
    }
}