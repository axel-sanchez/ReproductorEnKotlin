package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.databinding.ItemPlaylistBinding
import com.bumptech.glide.Glide

class ExploreAdapter(
    private val dataList: List<ItemSong?>,
    private val itemClick: (ItemSong) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemSong: ItemSong?, itemClick: (ItemSong) -> Unit) {

            itemSong?.let {
                binding.cardView.setOnClickListener { itemClick(itemSong) }

                Glide.with(binding.root)
                    .load(itemSong.getImages()[0].getUrl())
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