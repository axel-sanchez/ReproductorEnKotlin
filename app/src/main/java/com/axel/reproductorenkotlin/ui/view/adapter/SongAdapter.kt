package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Song
import com.bumptech.glide.Glide

class SongAdapter (private var mItems: List<Song>,
                   private val clickListener: (Song) -> Unit) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.imagen)
        private val name: TextView = itemView.findViewById(R.id.name_song)

        fun bind(item: Song, clickListener: (Song) -> Unit) {
            name.text = item.name

            itemView.setOnClickListener { clickListener(item) }

            Glide.with(itemView)
                .load(item.coverPage)
                .into(image)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        // set the view's size, margins, paddings and layout parameters ...
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mItems[position], clickListener)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mItems.size
}