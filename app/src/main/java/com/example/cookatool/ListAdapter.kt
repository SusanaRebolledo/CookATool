package com.example.cookatool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookatool.RealtimeDatabase.ListbaseModel
import kotlinx.android.synthetic.main.list_recipes_layout.view.*

class ListAdapter(var list:ArrayList<ListbaseModel>) :RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipes = itemView.tv_recipes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_recipes_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recipes.text= list[position].recipes
    }

}