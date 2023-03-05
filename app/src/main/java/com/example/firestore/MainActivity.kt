package com.example.firestore

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestore.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myhomework.contactslistfirebase.adapter.ContactsAdapter
import com.myhomework.contactslistfirebase.model.Contact

class MainActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("loading")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

        val contactsArr = ArrayList<Contact>()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                Log.e(TAG, "result: $result")
                for (document in result) {
                    contactsArr.add(
                        Contact(
                            document.id,
                            document.getString("name")!!,
                            document.getString("number")!!,
                            document.getString("address")!!
                        )
                    )
                }

                val adapter = ContactsAdapter(contactsArr)
                binding.recyclerview.layoutManager = LinearLayoutManager(this)
                binding.recyclerview.adapter = adapter

                hideProgressDialog()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()
                Log.w("TAG", "Error getting documents.", exception)
            }

        binding.add.setOnClickListener {
            val i = Intent(this,AddContact::class.java)
            startActivity(i)
        }
    }

    private fun hideProgressDialog() {
        if (progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }
}