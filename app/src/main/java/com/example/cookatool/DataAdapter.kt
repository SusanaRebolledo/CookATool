package com.example.cookatool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookatool.RealtimeDatabase.DatabaseModel
import kotlinx.android.synthetic.main.rv_layout.view.*

class DataAdapter(var list:ArrayList<DatabaseModel>) :RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipe = itemView.tv_recipe
        var ingredients = itemView.tv_ingredients
        var likes = itemView.tv_likes
        var people = itemView.tv_people
        var steps = itemView.tv_steps
        var tags = itemView.tv_tags
        var time = itemView.tv_time
        var user = itemView.tv_user
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recipe.text=list[position].recipe
        holder.ingredients.text=list[position].ingredients
        holder.likes.text=list[position].likes
        holder.people.text=list[position].people
        holder.steps.text=list[position].steps
        holder.tags.text=list[position].tags
        holder.time.text=list[position].time
        holder.user.text=list[position].user
    }

}