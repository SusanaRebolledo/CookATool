package com.example.cookatool

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.cookatool.RealtimeDatabase.DatabaseModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_search_results.*

class SearchResults : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        // Setup
        val bundle = intent.extras
        var results = bundle?.getString("results")
        getDate(results ?: "")
    }

    private fun getDate(results : String) {
        var none = true
        db.collection("recipes").get().addOnCompleteListener(object:OnCompleteListener<QuerySnapshot>{
            override fun onComplete(p0: Task<QuerySnapshot>) {
                var list = ArrayList<DatabaseModel>()
                if (p0.isSuccessful) {
                    for (data in p0.result!!) {
                        if (data.get("ingredients").toString().contains(results)) {
                            none = false
                            list.add(
                                DatabaseModel(
                                    data.id,
                                    data.get("ingredients") as String,
                                    data.get("likes") as String,
                                    data.get("people") as String,
                                    data.get("steps") as String,
                                    data.get("tags") as String,
                                    data.get("time") as String,
                                    data.get("user") as String
                                )
                            )
                        }
                    }
                    if (none) {
                        showAlert()
                    } else {
                        var adapter = DataAdapter(list)
                        recyclerView.adapter = adapter

                    }
                }
            }
        })
    }

    // Shows search alert
    private fun showAlert() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No matching search")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


