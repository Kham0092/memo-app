package com.example.memo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memo.databinding.ActivityMainBinding
import com.example.memo.databinding.LayoutCreateNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(),rvAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var noteList : ArrayList<rvModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            binding.tv.text = auth.currentUser!!.email
        }
        binding.btnLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@MainActivity,LoginPage::class.java))
        }
        val dialogBinding = LayoutCreateNotesBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()
        dialogBinding.btnSave.setOnClickListener {
            val title = dialogBinding.createTitle.text.toString()
            val content = dialogBinding.createDesc.text.toString()
            dialog.dismiss()
            addNote(title,content)
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnCreate.setOnClickListener {
            dialog.show()
        }
        loadData()
    }
    private fun addNote(title:String,content : String){
        databaseReference = FirebaseDatabase.getInstance().reference
        val currentUser = auth.currentUser
        if(currentUser!=null){
            val noteKey = databaseReference.child("users").child(currentUser.uid)
                .child("notes").push().key
            val noteItem = rvModel(title,content,noteKey)
            if(noteKey!=null){
                databaseReference.child("users").child(currentUser.uid).child("notes").child(noteKey).setValue(noteItem)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this@MainActivity,"Data added succesfully",Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            Toast.makeText(this@MainActivity,"Data Addition failed",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


    fun loadData(){
        noteList = arrayListOf()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            databaseReference = FirebaseDatabase.getInstance().reference
            val noteReference = databaseReference.child("users").child(user.uid)
                .child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noteList.clear()
                    for (mySnapshot in snapshot.children){
                        val note = mySnapshot.getValue(rvModel::class.java)
                        note?.let {
                            noteList.add(note)
                        }
                    }
                    noteList.reverse()
                    val adapter = rvAdapter(this@MainActivity,noteList,this@MainActivity)
                    binding.rv.layoutManager = LinearLayoutManager(this@MainActivity,
                        LinearLayoutManager.VERTICAL,false)
                    binding.rv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun onDelete(dataList: ArrayList<rvModel>, position: Int) {
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val cu = auth.currentUser
        cu?.let {user->
            val noteKey = dataList[position].notesKey
            if(noteKey!=null){
                databaseReference.child("users").child(user.uid).child("notes").child(noteKey).removeValue()
                noteList.clear()
            }
        }
    }

    override fun onUpdate(dataList: ArrayList<rvModel>, position: Int) {
        val dialogBinding = LayoutCreateNotesBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()
        dialog.show()
        dialogBinding.btnSave.setOnClickListener {
            dialog.dismiss()
            databaseReference = FirebaseDatabase.getInstance().reference
            auth = FirebaseAuth.getInstance()
            val cu = auth.currentUser
            cu?.let {user->
                val noteId = noteList[position].notesKey
                if(noteId!=null)
                {
                    databaseReference.child("users").child(cu.uid).child("notes").child(noteId)
                        .setValue(rvModel(dialogBinding.createTitle.text.toString(),dialogBinding.createDesc.text.toString()
                        ,noteId)).addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this@MainActivity,"Update successfull",Toast.LENGTH_SHORT).show()
                                noteList.clear()
                            }
                        }
                }
            }
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}