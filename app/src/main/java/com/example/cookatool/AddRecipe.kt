package com.example.cookatool

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_add_recipe.*

class AddRecipe : AppCompatActivity() {

    // Constant for Data Base
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Setup
        val bundle = intent.extras
        var email = bundle?.getString("email")
        setUp(email ?: "")
    }

    private fun setUp(email : String) {
        uploadRecipeButton.setOnClickListener{
           //Add recipe to recipes colleciton
            val recipes = db.collection("recipes")
            val dataR = hashMapOf(
                "ingredients" to ingredientEditText.text.toString(),
                    "steps" to stepsEditText.text.toString(),
                    "tags" to tagsEditText.text.toString(),
                    "time" to timeEditText.text.toString(),
                    "people" to peopleEditText.text.toString(),
                    "likes" to "0",
                    "user" to email
            )
            recipes.document(recipeNameEditText.text.toString()).set(dataR)

        }
    }
}