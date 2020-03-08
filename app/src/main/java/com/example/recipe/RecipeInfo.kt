package com.example.recipe

data class RecipeInfo (var title: String, var ingredients: MutableList<String>, var quantity: MutableList<String>) {
    override fun toString(): String {
        return title
    }
}

data class IngredientInfo (var name: String, var quantity: String) {
    override fun toString(): String {
        return name
    }
}