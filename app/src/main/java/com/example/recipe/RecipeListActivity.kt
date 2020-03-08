package com.example.recipe

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recipe_list.*
import kotlinx.android.synthetic.main.content_recipe_list.*


class RecipeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.layout_dialog, null)
            val addRecipe = dialogView.findViewById<EditText>(R.id.add_recipe)
            dialog.setView(dialogView)
            dialog.setMessage("Enter a name for your recipe")
            dialog.setPositiveButton("OK") { DialogInterface, Int ->
                val new = addRecipe.text.toString()
                if (new.isEmpty()) {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setMessage("Please enter a name").show()
                    hideKeyboardFrom(this, dialogView)
                    return@setPositiveButton
                }
                RecipeManager.addNewRecipe(new)
                val intent = Intent(this, RecipeActivity::class.java)
                intent.putExtra("new_recipe", new)
                startActivity(intent)
            }
            dialog.setNegativeButton("Cancel") { DialogInterface, Int ->
                hideKeyboardFrom(this, dialogView)
            }
            dialog.setCancelable(false)
            dialog.show()
            keyRequestFocus(addRecipe, this)
        }

        list_Recipes.layoutManager = LinearLayoutManager(this)

        list_Recipes.adapter = RecipeListRecyclerAdapter(this, RecipeManager.recipes)

        /*listRecipes.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            RecipeManager.recipes)

        listRecipes.setOnItemClickListener{parent, view, position, id ->
            val intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra("recipe_position", position)
            startActivity(intent)
        }*/
    }

    override fun onResume() {
        super.onResume()
        list_Recipes.adapter?.notifyDataSetChanged()
    }

}
