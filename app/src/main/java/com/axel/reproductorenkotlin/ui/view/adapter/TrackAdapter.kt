package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.data.models.PlaylistSongs
import com.axel.reproductorenkotlin.databinding.ItemSongBinding
import com.bumptech.glide.Glide

/**
 * @author Axel Sanchez
 */
class TrackAdapter(
    private val dataList: List<PlaylistSongs.Item.Track?>,
    private val itemClick: (PlaylistSongs.Item.Track?) -> Unit
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: PlaylistSongs.Item.Track?, itemClick: (PlaylistSongs.Item.Track?) -> Unit) {


            song?.let {
                binding.nameSong.text = song.name
                binding.cardView.setOnClickListener { itemClick(song) }

                Glide.with(itemView)
                    .load(song.album?.images?.first()?.url)
                    .into(binding.imagen)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemSongBinding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], itemClick)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}