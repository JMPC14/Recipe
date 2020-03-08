package com.example.recipe

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeListRecyclerAdapter(private val context: Context, private val recipes: List<RecipeInfo>) : RecyclerView.Adapter<RecipeListRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle = itemView.findViewById<TextView?>(R.id.recipe_Title)
        var recipePosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, RecipeActivity::class.java)
                intent.putExtra("recipe_position", recipePosition)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_recipe_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeTitle?.text = recipe.title
        holder.recipePosition = position
    }
}