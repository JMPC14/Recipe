package com.example.recipe

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_items.*
import kotlinx.android.synthetic.main.content_items.*

class ItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val recipeLayoutManager by lazy { LinearLayoutManager(this) }

    private val recipeListRecyclerAdapter by lazy { RecipeListRecyclerAdapter(this, RecipeManager.recipes) }

    private val ingredientsLayoutManager by lazy { LinearLayoutManager(this) }

    private val ingredientsRecyclerAdapter by lazy { IngredientsRecyclerAdapter(this, RecipeManager.ingredients) }

    private val viewModel by lazy { ViewModelProvider(this)[ItemsActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }

        val array = listOf("g", "kg", "ml", "L", "Other")

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            if (nav_view.menu.findItem(R.id.nav_recipes).isChecked) {
                val dialog = AlertDialog.Builder(this)
                val newDialog = dialog.create()
                val dialogView = layoutInflater.inflate(R.layout.layout_dialog, null)
                val addRecipe = dialogView.findViewById<EditText>(R.id.add_recipe)
                val okButton = dialogView.findViewById<Button>(R.id.ok_button)
                val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
                newDialog.setView(dialogView)
                newDialog.setMessage("Enter a name for your recipe")
                okButton.setOnClickListener {
                    val newName = addRecipe.text.toString()
                    if (newName.isEmpty()) {
                        Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
                    }
                    else if (newName.isNotEmpty()) {
                        RecipeManager.addNewRecipe(newName)
                        val intent = Intent(this, RecipeActivity::class.java)
                        intent.putExtra("new_recipe", newName)
                        hideKeyboardFrom(this, dialogView)
                        newDialog.cancel()
                        startActivity(intent)
                    }
                }
                cancelButton.setOnClickListener {
                    hideKeyboardFrom(this, dialogView)
                    newDialog.cancel()
                }
                newDialog.setCancelable(false)
                newDialog.show()
                keyRequestFocus(addRecipe, this)
            }
            if (nav_view.menu.findItem(R.id.nav_ingredients).isChecked) {
                val dialog = AlertDialog.Builder(this)
                val newDialog = dialog.create()
                val dialogView = layoutInflater.inflate(R.layout.layout_dialog, null)
                val addIngredient = dialogView.findViewById<EditText>(R.id.add_recipe)
                val okButton = dialogView.findViewById<Button>(R.id.ok_button)
                val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
                newDialog.setView(dialogView)
                newDialog.setMessage("Enter a name for your ingredient")
                okButton.setOnClickListener {
                    val newName = addIngredient.text.toString()
                    if (newName.isEmpty()) {
                        Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
                    }
                    else if (newName.isNotEmpty()) {
                        RecipeManager.addNewIngredient(newName)
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
                        newDialog.setMessage("Enter ingredient quantity")
                        okButton.setOnClickListener {
                            val newQuantity = addQuantity.text.toString()
                            if (newQuantity.isEmpty()) {
                                Toast.makeText(this, "Please enter a quantity",Toast.LENGTH_LONG).show()
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
                                    val test = RecipeManager.ingredients.lastIndex
                                    RecipeManager.ingredients[test].quantity = ("${newQuantity}${add}")
                                    hideKeyboardFrom(this, dialogView)
                                    newDialog.cancel()
                                }
                            }
                            displayIngredients()
                        }
                        cancelButton.setOnClickListener {
                            val remove = RecipeManager.ingredients.lastIndex
                            RecipeManager.ingredients.removeAt(remove)
                            hideKeyboardFrom(this, dialogView)
                            newDialog.cancel()
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
                keyRequestFocus(addIngredient, this)
            }
        }

        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_recipes, R.id.nav_ingredients, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    private fun displayRecipes() {
        list_Items.layoutManager = recipeLayoutManager
        list_Items.adapter = recipeListRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_recipes).isChecked = true
    }

    private fun displayIngredients() {
        list_Items.layoutManager = ingredientsLayoutManager
        list_Items.adapter = ingredientsRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_ingredients).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        list_Items.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
        {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_recipes,
            R.id.nav_ingredients -> {
                handleDisplaySelection(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleDisplaySelection(itemId: Int) {
        when (itemId) {
            R.id.nav_recipes -> {
                displayRecipes()
            }
            R.id.nav_ingredients -> {
                displayIngredients()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null) {
            viewModel.saveState(outState)
        }
    }
}
