package com.myhomework.contactslistfirebase.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestore.databinding.ContactCardBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myhomework.contactslistfirebase.model.Contact
import kotlin.collections.ArrayList

class ContactsAdapter(var data: ArrayList<Contact>): RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    lateinit var context: Context
    private val db = Firebase.firestore

    class MyViewHolder(val cardViewBinding: ContactCardBinding): RecyclerView.ViewHolder(cardViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding : ContactCardBinding
                = ContactCardBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardViewBinding.apply {
            name.text = data[position].name
            number.text = data[position].number
            address.text = data[position].address
        }


        holder.cardViewBinding.delete.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Delete Contact")
                setMessage("Are you sure!!")
                setPositiveButton("Yse"){
                        _, _ ->
                    db.collection("users").document(data[position].id)
                        .delete()
                        .addOnSuccessListener {
                            Log.e("TAG", "Deleted Successfully")
                            data.removeAt(position)
                            notifyDataSetChanged()
                        }.addOnFailureListener { error ->
                            Log.e("TAG", error.message.toString())
                        }
                }
                setCancelable(true)
                setNegativeButton("No"){ dialogInterface: DialogInterface, _ ->
                    dialogInterface.cancel()
                }
                create()
                show()
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

}