package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.data.models.Song
import com.axel.reproductorenkotlin.databinding.ItemSongBinding
import com.bumptech.glide.Glide

class SongAdapter (private var mItems: List<Song>,
                   private val clickListener: (Song) -> Unit) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Song, clickListener: (Song) -> Unit) {
            binding.nameSong.text = item.name

            itemView.setOnClickListener { clickListener(item) }

            Glide.with(itemView)
                .load(item.coverPage)
                .into(binding.imagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemSongBinding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mItems[position], clickListener)

    override fun getItemCount() = mItems.size
}