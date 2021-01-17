package com.axel.reproductorenkotlin.ui.view.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.databinding.ItemTrackBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author Axel Sanchez
 */
class TrackAdapter(
    private val dataList: List<PlaylistSongs.Item.Track?>,
    private val scope: LifecycleCoroutineScope,
    private val itemClick: (PlaylistSongs.Item.Track?) -> Unit
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    private var job: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    inner class ViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: PlaylistSongs.Item.Track?, itemClick: (PlaylistSongs.Item.Track?) -> Unit) {

            var isPlaying = false

            var setIsPlayingFalse = {
                isPlaying = false
            }

            song?.let {
                binding.nameSong.text = song.name
                binding.nameArtist.text = song.artists?.map { it?.name }.toString()
                binding.btnPlayPause.setOnClickListener {
                    isPlaying = if(isPlaying){
                        binding.btnPlayPause.setBackgroundResource(R.drawable.ic_play_transparent)
                        false
                    } else{
                        binding.btnPlayPause.setBackgroundResource(R.drawable.ic_pause_transparent)
                        true
                    }
                    itemClickPlayPause(song, binding.btnPlayPause, setIsPlayingFalse)
                }

                Glide.with(itemView)
                    .load(song.album?.images?.first()?.url)
                    .into(binding.imagen)
            }
        }
    }

    private fun itemClickPlayPause(song: PlaylistSongs.Item.Track, btnPlayPause: Button, setIsPlayingFalse: () -> Unit) {
        mediaPlayer?.let {
            it.stop()
            mediaPlayer = null
            job?.cancel()
            job = null
        }?: kotlin.run {
            job = scope.launch {
                val url = song.preview_url
                mediaPlayer = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(url)
                    prepare() // might take long! (for buffering, etc)
                    start()
                    setOnCompletionListener {
                        it.stop()
                        mediaPlayer = null
                        job?.cancel()
                        job = null
                        btnPlayPause.setBackgroundResource(R.drawable.ic_play_transparent)
                        setIsPlayingFalse()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemTrackBinding = ItemTrackBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], itemClick)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}