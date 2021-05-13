package com.example.cookatool

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookatool.RealtimeDatabase.ListbaseModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_search_results.*

class RecipesList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        // Setup
        val bundle = intent.extras
        var email = bundle?.getString("email")
        getDate(email ?: "")

    }

    private fun getDate(email: String) {
        var none = true
        db.collection("recipes").get().addOnCompleteListener(object:
            OnCompleteListener<QuerySnapshot> {
            override fun onComplete(p0: Task<QuerySnapshot>) {
                var list = ArrayList<ListbaseModel>()
                if (p0.isSuccessful) {
                    for (data in p0.result!!) {
                        if (data.get("user").toString() == email) {
                            none = false
                            list.add(
                                ListbaseModel(
                                    data.id as String,
                                )
                            )
                        }
                    }
                    if (none) {
                        showAlert()
                    } else {
                        var adapter = ListAdapter(list)
                        recyclerView.adapter = adapter

                    }
                }
            }
        })
    }

    // Shows non-existent alert
    private fun showAlert() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("You have no recipes")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}