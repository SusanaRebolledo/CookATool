package com.example.cookatool

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

enum class ProviderType {
    BASIC,
    GOOGLE
}

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Set up
        val bundle = intent.extras
        var email = bundle?.getString("email")
        var provider = bundle?.getString("provider")
        setUp(email ?: "", provider ?: "")

        // Save data authentication
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

    }

    // Functions
    private fun setUp(email: String, provider: String) {
        title = "Home"

        // Show email and provider
        emailTextView.text = email
        providerTextView.text = provider

        // Set button to go back
        logOutButton.setOnClickListener{
            // Delete authentication data
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        // Button to add recipe
        addRecipeButton.setOnClickListener{
            val addRecipeIntent = Intent(this, AddRecipe::class.java).apply {
                putExtra("email", email)
            }
            startActivity(addRecipeIntent)
        }

        // Button search recipe
        searchRecipeButton.setOnClickListener{
            val searchRecipeIntent = Intent(this, SearchRecipe::class.java).apply {
            }
            startActivity(searchRecipeIntent)
        }

        // Button rate recipe
        recipeListButton.setOnClickListener {
            val rateRecipeIntent = Intent(this, RecipesList::class.java).apply {
                putExtra("email", email)
            }
            startActivity(rateRecipeIntent)
        }
    }

}