package com.ioad.wac

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class TestPicture {

    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    suspend fun getPicture() {
        return withContext(Dispatchers.IO) {
            val uri  = async { (getImage()) }.await()
        }
    }

    suspend fun getImage() = coroutineScope {
        Log.e("TAG", "start get picture")
        var doc: StorageReference? = null
        storage.reference.child("clothes").listAll().addOnSuccessListener {
            doc = it.items[0]
            it.items
        }
        doc?.let {
            it.downloadUrl.addOnSuccessListener {
                Log.e("TAG", it.toString())
            }
        }
        Log.e("TAG", "finish get picture")

    }


}