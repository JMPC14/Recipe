package com.example.recipe

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ingredient_list.view.*

class IngredientsRecyclerAdapter(private val context: Context, private val ingredients: List<IngredientInfo>) : RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName = itemView.findViewById<TextView?>(R.id.ingredient_Name)
        val ingredientQuantity = itemView.findViewById<TextView?>(R.id.ingredient_Quantity)
        var ingredientPosition = 0
        var array = listOf("g", "kg", "ml", "L", "Other")

        init{
            itemView.ingredient_Name.setOnLongClickListener {
                val pop = PopupMenu(itemView.context,it)
                pop.inflate(R.menu.ingredients_menu)
                if (RecipeManager.ingredients[ingredientPosition].quantity.isEmpty()) {
                    pop.menu.findItem(R.id.Add_Quantity).isVisible = true
                }
                pop.setOnMenuItemClickListener { item ->
                    when(item.itemId)
                    {
                        R.id.Edit -> {
                            val dialog = AlertDialog.Builder(context)
                            val newDialog = dialog.create()
                            val dialogView = layoutInflater.inflate(R.layout.layout_dialog, null)
                            val newNameText = dialogView.findViewById<EditText>(R.id.add_recipe)
                            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
                            val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
                            newDialog.setMessage("Edit name")
                            newDialog.setView(dialogView)
                            okButton.setOnClickListener {
                                val newName = newNameText.text.toString()
                                if (newName.isEmpty()) {
                                    Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG).show()
                                }
                                else if (newName.isNotEmpty()) {
                                    RecipeManager.ingredients[ingredientPosition].name = newName
                                    notifyDataSetChanged()
                                    hideKeyboardFrom(context, dialogView)
                                    newDialog.cancel()
                                }
                            }
                            cancelButton.setOnClickListener {
                                hideKeyboardFrom(context, dialogView)
                                newDialog.cancel()
                            }
                            newDialog.setCancelable(false)
                            newDialog.show()
                            keyRequestFocus(newNameText, context)
                        }

                        R.id.Remove -> {
                            RecipeManager.ingredients.removeAt(ingredientPosition)
                            notifyDataSetChanged()
                        }

                        R.id.Add_Quantity -> {
                            val dialog = AlertDialog.Builder(context)
                            val newDialog = dialog.create()
                            val dialogView = layoutInflater.inflate(R.layout.layout_dialog_quantity, null)
                            val editQuantity = dialogView.findViewById<EditText>(R.id.add_quantity)
                            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
                            val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)

                            val spinner = dialogView.findViewById<Spinner>(R.id.quantity_type)
                            val spinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, array)
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = spinnerAdapter

                            newDialog.setView(dialogView)
                            newDialog.setMessage("Edit ingredient quantity")
                            okButton.setOnClickListener {
                                val newQuantity = editQuantity.text.toString()
                                if (newQuantity.isEmpty()) {
                                    Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_LONG).show()
                                }
                                else if (newQuantity.isNotEmpty()) {
                                    val add = when (val selected = spinner.selectedItem.toString()) {
                                        array[0], array[1], array[2], array [3] -> selected
                                        else -> ""
                                    }
                                    if (add != "" && (newQuantity.toFloatOrNull() == null || newQuantity.toFloat() < 0)) {
                                        Toast.makeText(
                                            context,
                                            "Please enter a valid quantity",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else {
                                        RecipeManager.ingredients[ingredientPosition].quantity =
                                            "${newQuantity}${add}"
                                        notifyDataSetChanged()
                                        hideKeyboardFrom(context, dialogView)
                                        newDialog.cancel()
                                    }
                                }
                            }
                            cancelButton.setOnClickListener {
                                hideKeyboardFrom(context, dialogView)
                                newDialog.cancel()
                            }
                            newDialog.setCancelable(false)
                            newDialog.show()
                            keyRequestFocus(editQuantity, context)
                        }
                        R.id.Buy -> {
                            val ingredient = RecipeManager.ingredients[ingredientPosition].name
                            val quantity = RecipeManager.ingredients[ingredientPosition].quantity
                            val url = "https://www.google.co.uk/search?q=${ingredient} ${quantity}&tbm=shop"
                            val builder = CustomTabsIntent.Builder()
                            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))

                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(context, Uri.parse(url))
                        }
                    }
                    true
                }
                pop.show()
                true
            }
            itemView.ingredient_Quantity.setOnLongClickListener {
                val pop = PopupMenu(itemView.context,it)
                pop.inflate(R.menu.quantity_menu)
                pop.setOnMenuItemClickListener { item ->
                    when(item.itemId)
                    {
                        R.id.Edit2 -> {
                            val dialog = AlertDialog.Builder(context)
                            val newDialog = dialog.create()
                            val dialogView = layoutInflater.inflate(R.layout.layout_dialog_quantity, null)
                            val newQuantityText = dialogView.findViewById<EditText>(R.id.add_quantity)
                            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
                            val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)

                            val spinner = dialogView.findViewById<Spinner>(R.id.quantity_type)
                            val spinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, array)
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = spinnerAdapter

                            newDialog.setView(dialogView)
                            newDialog.setMessage("Edit quantity")
                            okButton.setOnClickListener {
                                val newQuantity = newQuantityText.text.toString()
                                if (newQuantity.isEmpty()) {
                                    Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_LONG).show()
                                }
                                else if (newQuantity.isNotEmpty()) {
                                    val add = when (val selected = spinner.selectedItem.toString()) {
                                        array[0], array[1], array[2], array [3] -> selected
                                        else -> ""
                                    }
                                    if (add != "" && (newQuantity.toFloatOrNull() == null || newQuantity.toFloat() < 0)) {
                                        Toast.makeText(
                                            context,
                                            "Please enter a valid quantity",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else {
                                        RecipeManager.ingredients[ingredientPosition].quantity =
                                            "${newQuantity}${add}"
                                        notifyDataSetChanged()
                                        hideKeyboardFrom(context, dialogView)
                                        newDialog.cancel()
                                    }
                                }
                            }
                            cancelButton.setOnClickListener {
                                hideKeyboardFrom(context, dialogView)
                                newDialog.cancel()
                            }
                            newDialog.setCancelable(false)
                            newDialog.show()
                            keyRequestFocus(newQuantityText, context)
                        }

                        R.id.Remove2 -> {
                            RecipeManager.ingredients[ingredientPosition].quantity = ""
                            notifyDataSetChanged()
                        }
                    }
                    true
                }
                pop.show()
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_ingredient_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = ingredients.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName?.text = ingredient.name
        holder.ingredientQuantity?.text = ingredient.quantity
        holder.ingredientPosition = position
    }

}