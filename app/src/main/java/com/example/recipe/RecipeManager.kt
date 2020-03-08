package com.example.recipe

object RecipeManager {
    var recipes = ArrayList<RecipeInfo>()
    var ingredients = ArrayList<IngredientInfo>()

    init {
        initialiseRecipes()
        initialiseIngredients()
    }

    private fun addRecipe(title: String, ingredients: MutableList<String>, quantity: MutableList<String>) {
        val recipe = RecipeInfo(title, ingredients, quantity)
        recipes.add(recipe)
    }

    fun addNewRecipe(title: String) {
        addRecipe(title, mutableListOf(), mutableListOf())
    }

    private fun addIngredient(name: String, quantity: String) {
        val ingredient = IngredientInfo(name, quantity)
        ingredients.add(ingredient)
    }

    fun addNewIngredient(name: String) {
        addIngredient(name, "")
    }

    private fun initialiseIngredients() {
        var name = "Cabbage"
        var quantity = "2kg"
        ingredients.add(IngredientInfo(name, quantity))

        name = "Beef"
        quantity = "4kg"
        ingredients.add(IngredientInfo(name, quantity))

        name = "Chicken Breasts"
        quantity = "750g"
        ingredients.add(IngredientInfo(name, quantity))
    }

    private fun initialiseRecipes() {
        var ingredients = mutableListOf("Bacon", "Spaghetti", "Eggs")
        var quantity = mutableListOf("500g", "400g", "300g")
        var recipe = RecipeInfo("Carbonara", ingredients, quantity)
        recipes.add(recipe)

        ingredients = mutableListOf("Mince", "Onion", "Tomatoes", "Spaghetti")
        quantity = mutableListOf("500g", "400g", "300g", "200g")
        recipe = RecipeInfo("Bolognese", ingredients, quantity)
        recipes.add(recipe)

        ingredients = mutableListOf("Chicken Thighs", "Soy Sauce", "Broccoli", "Peppers", "Mushrooms")
        quantity = mutableListOf("500g", "400g", "300g", "200g", "100g")
        recipe = RecipeInfo("Stir Fry", ingredients, quantity)
        recipes.add(recipe)

        ingredients = mutableListOf("Chicken Breasts", "Tortillas", "Peppers", "Onions", "Salad")
        quantity = mutableListOf("500g", "400g", "300g", "200g", "100g")
        recipe = RecipeInfo("Fajitas", ingredients, quantity)
        recipes.add(recipe)
    }
}