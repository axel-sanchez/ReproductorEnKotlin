package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.axel.reproductorenkotlin.databinding.ItemPlaylistBinding
import com.bumptech.glide.Glide

class LibraryAdapter(
    private val dataList: List<UserPlaylists.Item?>,
    private val itemClick: (UserPlaylists.Item?) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: UserPlaylists.Item?, itemClick: (UserPlaylists.Item?) -> Unit) {

            playlist?.let {
                binding.cardView.setOnClickListener { itemClick(playlist) }

                Glide.with(binding.root)
                    .load(playlist.images?.first()?.url)
                    .into(binding.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryAdapter.ViewHolder {
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