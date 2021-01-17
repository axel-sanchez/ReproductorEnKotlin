package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.UserPlaylists
import com.bumptech.glide.Glide

class LibraryAdapter(
    private val dataList: List<UserPlaylists.Item?>,
    private val itemClick: (UserPlaylists.Item?) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.image)
        private var cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bind(playlist: UserPlaylists.Item?, itemClick: (UserPlaylists.Item?) -> Unit) {

            playlist?.let {
                cardView.setOnClickListener { itemClick(playlist) }

                Glide.with(itemView)
                    .load(playlist.images?.first()?.url)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, null, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], itemClick)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}