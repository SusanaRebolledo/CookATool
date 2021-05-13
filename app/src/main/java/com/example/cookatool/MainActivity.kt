package com.example.cookatool


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the FirebaseAnalytics instance for Analytics Events
        val analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Firebase integration completed")
        analytics.logEvent("InitScreen", bundle)

        // Setup
        setUp()
        session()
    }

    // Guarantee visibility on start
    override fun onStart() {
        super.onStart()
        authLayout.visibility = View.VISIBLE
    }

    // Function to keep initiated session
    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        // In initiated session case
        if (email != null && provider != null) {
            // Layout invisibility to start home directly
            authLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }

    }

    private fun setUp() {
        // Register button
        registerButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }
        // Log in button
        logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        // Google log in button
        googleButton.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            // Sign out in previous google account
            googleClient.signOut()
            // Sign in on google
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }
    // Shows authentication alert
    private fun showAlert() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Authentication failed")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Shows home when logged in
    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, LogIn::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Google authentication
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                // Google authentication in Firebase
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }
}