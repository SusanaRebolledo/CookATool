package com.example.cookatool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search_recipe.*

class SearchRecipe : AppCompatActivity() {

    // Constant for Data Base
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Setup
        val bundle = intent.extras
        var email = bundle?.getString("email")
        setUp(email ?: "")
    }

    private fun setUp(email : String) {
        searchButton.setOnClickListener {
            val docRef = db.collection("recipes")
            val recipeListIntent = Intent(applicationContext, SearchResults::class.java).apply {
                putExtra("results", ingredientsEditText.text.toString())
            }
            startActivity(recipeListIntent)

        }
    }
}