package com.ioad.wac.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ioad.wac.R

class MainBoardActivity : AppCompatActivity() {

    lateinit var rvClothes: RecyclerView
    lateinit var glide: RequestManager
    lateinit var database: FirebaseDatabase
    lateinit var dbReference: DatabaseReference
    val fireStore = FirebaseFirestore.getInstance()
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val pathRef = storageRef.child("clothes")
    val listRef = pathRef.listAll()
    var list = ArrayList<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_board)

        val storageRef = storage.reference
        val pathRef = storageRef.child("clothes")
        val listRef = pathRef.listAll()
        glide = Glide.with(this)


        rvClothes = findViewById(R.id.rv_clothes)
        rvClothes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)


    }

}