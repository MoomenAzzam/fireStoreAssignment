package com.example.firestore

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firestore.databinding.ActivityAddContactBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddContact : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore

        binding.button.setOnClickListener {
            if (binding.name.text != null && binding.number.text != null
                && binding.address.text != null
            ) {
                // show Dialog code
                progressDialog = ProgressDialog(this)
                progressDialog!!.setMessage("loading")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()

                var name = binding.name.text.toString();
                var number = binding.number.text.toString();
                var address = binding.address.text.toString();

                val user = hashMapOf(
                    "name" to "$name",
                    "number" to "$number",
                    "address" to "$address"
                )

                db.collection("users")
                    .add(user)
                    .addOnSuccessListener {
                        Log.e("TAG", "Added Successfully")

                        binding.name.text?.clear()
                        binding.number.text?.clear()
                        binding.address.text?.clear()

                        hideProgressDialog()
                        val i = Intent(this,MainActivity::class.java)
                        startActivity(i)
                    }
                    .addOnFailureListener { error ->
                        Log.w("TAG", "Error $error")

                        hideProgressDialog()
                    }

            }
        }

    }

    private fun hideProgressDialog() {
        if (progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }
}