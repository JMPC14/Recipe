package com.example.recipe

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class RecipeActivity : AppCompatActivity() {

    private val recipeIngredientsLayoutManager by lazy { LinearLayoutManager(this) }

    private val recipeIngredientsRecyclerAdapter by lazy {
        RecipeIngredientsRecyclerAdapter(
            this,
            RecipeManager.recipes[recipePosition]
        )
    }

    private var recipePosition = -1
    private var newRecipePosition = ""
    var array = listOf("g", "kg", "ml", "L", "Other")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val compat = findViewById<TextView>(R.id.recipe_Compatibility)
        val check = findViewById<ImageView>(R.id.ingredient_Check)

        var listOfIngredients = mutableListOf<String>()
        for (i: IngredientInfo in RecipeManager.ingredients) {
            listOfIngredients.add(i.name)
        }

        fun compareIngredients(recipe: RecipeInfo) {
            var recipeIngredients: List<String> = recipe.ingredients
            var haveIngredients = mutableListOf<String>()
            for (string in recipeIngredients) {
                if (listOfIngredients.contains(string)) {
                    haveIngredients.add(string)
                }
            }
            if (haveIngredients.isEmpty()) {
                compat.text = "You have none of these ingredients"
            }
            else {
                var newIngredients = ""
                for (i in haveIngredients) {
                    newIngredients = newIngredients.plus(i)
                }
                compat.text = "You have $newIngredients"
            }
        }

        fab_addIngredient.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val newDialog = dialog.create()
            val dialogView = layoutInflater.inflate(R.layout.layout_dialog, null)
            val addIngredient = dialogView.findViewById<EditText>(R.id.add_recipe)
            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
            val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
            newDialog.setView(dialogView)
            newDialog.setMessage("Name of ingredient")
            okButton.setOnClickListener {
                val newName = addIngredient.text.toString()
                if (newName.isEmpty()) {
                    Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
                }
                else if (newName.isNotEmpty()) {
                    RecipeManager.recipes[recipePosition].ingredients.add(newName)
                    hideKeyboardFrom(this, dialogView)
                    newDialog.cancel()
                    val dialog = AlertDialog.Builder(this)
                    val newDialog = dialog.create()
                    val dialogView = layoutInflater.inflate(R.layout.layout_dialog_quantity, null)
                    val addQuantity = dialogView.findViewById<EditText>(R.id.add_quantity)
                    val okButton = dialogView.findViewById<Button>(R.id.ok_button)
                    val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)

                    val spinner = dialogView.findViewById<Spinner>(R.id.quantity_type)
                    val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = spinnerAdapter

                    newDialog.setView(dialogView)
                    newDialog.setMessage("Quantity of ingredient")
                    okButton.setOnClickListener {
                        val newQuantity = addQuantity.text.toString()
                        if (newQuantity.isEmpty()) {
                            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_LONG)
                                .show()
                        }
                        else if (newQuantity.isNotEmpty()) {
                            val add = when (val selected = spinner.selectedItem.toString()) {
                                array[0], array[1], array[2], array [3] -> selected
                                else -> ""
                            }
                            if (add != "" && (newQuantity.toFloatOrNull() == null || newQuantity.toFloat() < 0)) {
                                Toast.makeText(
                                    this,
                                    "Please enter a valid quantity",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else {
                                RecipeManager.recipes[recipePosition].quantity.add("${newQuantity}${add}")
                                hideKeyboardFrom(this, dialogView)
                                newDialog.cancel()
                            }
                        }
                    }
                    cancelButton.setOnClickListener {
                        hideKeyboardFrom(this, dialogView)
                        newDialog.cancel()
                        val cancel = RecipeManager.recipes[recipePosition].ingredients.lastIndex
                        RecipeManager.recipes[recipePosition].ingredients.removeAt(cancel)
                    }
                    newDialog.setCancelable(false)
                    newDialog.show()
                    keyRequestFocus(addQuantity, this)
                }
            }
            cancelButton.setOnClickListener {
                hideKeyboardFrom(this, dialogView)
                newDialog.cancel()
            }
            newDialog.setCancelable(false)
            newDialog.show()
            displayRecipe()
            keyRequestFocus(addIngredient, this)
        }

        delete_Recipe.setOnClickListener {
            RecipeManager.recipes.removeAt(recipePosition)
            finish()
        }

        edit_Recipe.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val newDialog = dialog.create()
            val dialogView = layoutInflater.inflate(R.layout.layout_dialog, null)
            val editRecipe = dialogView.findViewById<EditText>(R.id.add_recipe)
            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
            val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
            newDialog.setView(dialogView)
            newDialog.setMessage("Enter a name for your recipe")
            okButton.setOnClickListener {
                val newName = editRecipe.text.toString()
                if (newName.isEmpty()) {
                    Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
                } else if (newName.isNotEmpty()) {
                    RecipeManager.recipes[recipePosition].title = newName
                    hideKeyboardFrom(this, dialogView)
                    newDialog.cancel()
                    displayRecipe()
                }
            }
            cancelButton.setOnClickListener {
                hideKeyboardFrom(this, dialogView)
                newDialog.cancel()
            }
            newDialog.setCancelable(false)
            newDialog.show()
            keyRequestFocus(editRecipe, this)
        }

        recipePosition = intent.getIntExtra("recipe_position", -1)

        if (recipePosition != -1) {
            displayRecipe()
        } else {
            newRecipePosition = intent.getStringExtra("new_recipe")
            recipe_Title.text = newRecipePosition
            recipePosition = RecipeManager.recipes.lastIndex
        }

        compareIngredients(RecipeManager.recipes[recipePosition])

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.items, menu)
        menu!!.findItem(R.id.action_share).isVisible = true
        return true
    }

    fun displayRecipe() {
        val recipe = RecipeManager.recipes[recipePosition]
        recipe_Title.text = recipe.title
        list_recipe_ingredients.layoutManager = recipeIngredientsLayoutManager
        list_recipe_ingredients.adapter = recipeIngredientsRecyclerAdapter
    }

    override fun onResume() {
        super.onResume()
        displayRecipe()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareRecipe()
                true
            }
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareRecipe() {
        val recipe = RecipeManager.recipes[recipePosition]
        val title = recipe.title
        val ingredients = recipe.ingredients
        val quantities = recipe.quantity
        var message = "Here is my recipe for $title:"
        for (i in ingredients) {
            val test = ingredients.indexOf(i)
            val ingredient = ingredients[test]
            val quantity = quantities[test]
            message = message.plus("\n$ingredient - $quantity")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type="text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}