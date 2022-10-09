package com.bubududu.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val data: ArrayList<Items>, private val onItemClicked: (position: Int) -> Unit): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //ViewHolder class
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //Variable for items inside Card
        var itemName: TextView
        var itemQuantity: TextView
        var itemDelete: ImageView
        //Initialize Variable
        init {
            itemName = itemView.findViewById(R.id.text_name)
            itemQuantity = itemView.findViewById(R.id.text_quantity)
            itemDelete = itemView.findViewById(R.id.button_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Passing Values
        holder.itemName.setText(data.get(position).name)
        holder.itemQuantity.setText(data.get(position).quantity)

        holder.itemDelete.setOnClickListener{
            onItemClicked(position)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

}