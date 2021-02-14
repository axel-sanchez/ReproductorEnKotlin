package com.axel.reproductorenkotlin.ui.view.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Search
import com.axel.reproductorenkotlin.databinding.ItemTrackBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author Axel Sanchez
 */
class SearchedSongsAdapter(
    private val dataList: List<Search.Tracks.Item?>,
    private val scope: LifecycleCoroutineScope,
    private val spotifyClickListener: (Search.Tracks.Item?) -> Unit
) : RecyclerView.Adapter<SearchedSongsAdapter.ViewHolder>() {

    private var job: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    fun destroyJob(){
        stopSong(mediaPlayer)
        job?.cancel()
        job = null
    }

    inner class ViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Search.Tracks.Item?, spotifyClickListener: (Search.Tracks.Item?) -> Unit) {

            var isPlaying = false

            var setIsPlayingFalse = {
                isPlaying = false
            }

            song?.let {
                binding.nameSong.text = song.name
                binding.nameArtist.text = song.artists?.map { it?.name }.toString()
                binding.btnPlayPause.setOnClickListener {
                    song.preview_url?.let {
                        isPlaying = if (isPlaying) {
                            binding.btnPlayPause.setBackgroundResource(R.drawable.ic_play_transparent)
                            false
                        } else {
                            binding.btnPlayPause.setBackgroundResource(R.drawable.ic_pause_transparent)
                            true
                        }
                        itemClickPlayPause(it, binding.btnPlayPause, setIsPlayingFalse)
                    }?: kotlin.run {
                        Toast.makeText(binding.root.context, "Can't be played", Toast.LENGTH_SHORT).show()
                    }
                }

                binding.btnSpotify.setOnClickListener {
                    spotifyClickListener(song)
                }

                Glide.with(itemView)
                    .load(song.album?.images?.first()?.url)
                    .into(binding.imagen)
            }
        }
    }

    private fun itemClickPlayPause(preview_url: String, btnPlayPause: Button, setIsPlayingFalse: () -> Unit) {
        mediaPlayer?.let {
            stopSong(it)
        } ?: kotlin.run {
            job = scope.launch {
                mediaPlayer = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(preview_url)
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

    private fun stopSong(mPlayer: MediaPlayer?) {
        mPlayer?.stop()
        mediaPlayer = null
        job?.cancel()
        job = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemTrackBinding =
            ItemTrackBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], spotifyClickListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}