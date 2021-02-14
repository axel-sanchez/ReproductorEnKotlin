package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.data.models.FeaturedPlaylistSong
import com.axel.reproductorenkotlin.databinding.ItemPlaylistBinding
import com.bumptech.glide.Glide

class ExploreAdapter(
    private val dataList: List<FeaturedPlaylistSong?>,
    private val itemClick: (FeaturedPlaylistSong) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(featuredPlaylistSong: FeaturedPlaylistSong?, itemClick: (FeaturedPlaylistSong) -> Unit) {

            featuredPlaylistSong?.let {
                binding.cardView.setOnClickListener { itemClick(featuredPlaylistSong) }

                Glide.with(binding.root)
                    .load(featuredPlaylistSong.getImages()[0].getUrl())
                    .into(binding.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemPlaylistBinding = ItemPlaylistBinding.inflate(layoutInflater, null, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], itemClick)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}