package com.axel.reproductorenkotlin.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.ItemSong
import com.bumptech.glide.Glide

class ExploreAdapter(
    private val dataList: List<ItemSong?>,
    private val itemClick: (ItemSong) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.image)
        private var cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bind(itemSong: ItemSong?, itemClick: (ItemSong) -> Unit) {

            itemSong?.let {
                cardView.setOnClickListener { itemClick(itemSong) }

                Glide.with(itemView)
                    .load(itemSong.getImages()[0].getUrl())
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