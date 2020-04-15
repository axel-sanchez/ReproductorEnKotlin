package com.axel.reproductorenkotlin.ui.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Cancion
import com.bumptech.glide.Glide

class CancionAdapter (private var activity: Activity,
                      private var mItems: MutableList<Cancion>,
                      private val clickListener: (Cancion) -> Unit) : RecyclerView.Adapter<CancionAdapter.ViewHolder>() {


    fun setItems( newItems : MutableList<Cancion>){
        mItems = newItems
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.imagen)
        private val name: TextView = itemView.findViewById(R.id.name_song)

        fun bind(position : Int, item: Cancion, clickListener: (Cancion) -> Unit) {
            name.text = item.getNombre()

            itemView.setOnClickListener { clickListener(item) }

            image.setImageResource(item.getPortada())

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cancion, parent, false)
        // set the view's size, margins, paddings and layout parameters ...
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position,mItems[position], clickListener)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mItems.size
}