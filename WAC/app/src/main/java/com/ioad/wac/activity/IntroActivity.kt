package com.ioad.wac.activity

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.ioad.wac.R
import com.ioad.wac.adapter.IntroAdapter
import com.ioad.wac.model.Clothes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class IntroActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var dbReference: DatabaseReference
    lateinit var storage: FirebaseStorage

    val temperature28: List<Int> = listOf(
        R.drawable.sleeveless,
        R.drawable.tshirt,
        R.drawable.short_pants,
        R.drawable.skirt
    )

    val temperature23to27: List<Int> = listOf(
        R.drawable.tshirt,
        R.drawable.shirt,
        R.drawable.short_pants,
        R.drawable.trousers
    )

    val temperature20to22: List<Int> = listOf(
        R.drawable.sweater,
        R.drawable.tshirt,
        R.drawable.trousers,
        R.drawable.jeans,
    )

    val temperature12to19: List<Int> = listOf(
        R.drawable.cardigan,
        R.drawable.sweatshirt,
        R.drawable.hoodie,
        R.drawable.jacket,
        R.drawable.trousers,
        R.drawable.jeans,
        R.drawable.starking
    )

    val temperature9to11: List<Int> = listOf(
        R.drawable.jacket,
        R.drawable.coat,
        R.drawable.hoddie_jacket,
        R.drawable.winter_sweater,
        R.drawable.trousers,
        R.drawable.jeans,
        R.drawable.starking
    )

    val temperature4to8: List<Int> = listOf(
        R.drawable.puffer_coat,
        R.drawable.winter_sweater,
        R.drawable.jeans,
        R.drawable.leggings,
    )

    val temperature3: List<Int> = listOf(
        R.drawable.padding_jacket,
        R.drawable.puffer_coat,
        R.drawable.scarf,
        R.drawable.mitten,
        R.drawable.beanie
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)



        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "empty")

        Log.e("TAG", "tokeen?? " + token)

        when(token) {
            "empty" -> {
                val vpIntro = findViewById<ViewPager2>(R.id.vp_intro)
                vpIntro.adapter = IntroAdapter(this, 2)
            }
            else -> {
                startActivity(Intent(this, MainBoardActivity2::class.java))
            }
        }



        database = FirebaseDatabase.getInstance()
        dbReference = database.reference.child("images")
        storage = FirebaseStorage.getInstance()




//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//            Log.d("TAG", token)
//
//            // Log and toast
////            val msg = getString(R.string.msg_token_fmt, token)
////            Log.d("TAG", msg)
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//        })

        requestPermission()
//        settingList()

    } // onCreate



    private fun requestPermission():Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        val permissions:Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, 0)
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            0 -> if (grantResults.isNotEmpty()) {
                var isAllGranted = true
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false
                        break
                    }
                }

                if (isAllGranted) {

                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {

                    } else {

                    }
                }
            }
        }
    }




    fun settingList() {
        val temperatureList: List<List<Int>> = listOf(
            temperature28,
            temperature23to27
        )


//        val imageUri: Uri? = resourceToUri(this, R.drawable.sleeveless)
//        Log.e("TAG", imageUri.toString())


        temperatureList.forEachIndexed { listIndex, list ->
            Log.d("TAG", "index :: " + listIndex)
            var temperature = ""
            when (listIndex) {
                0 -> temperature = "28"
                else -> temperature = "23to27"
            }
            list.forEachIndexed { index, value ->
                val imageUri: Uri? = resourceToUri(this, value)
                Log.e("TAG", "temperature : $temperature / " + imageUri.toString())
                Log.e("TAG", "name : ${resources.getResourceEntryName(value)}")
                Log.e("TAG", "index : ${index + 1}")
                CoroutineScope(Dispatchers.IO).launch {
                    uploadToFirebase(
                        imageUri!!,
                        temperature,
                        resources.getResourceEntryName(value),
                        index + 1
                    )
                }
            }
        }
    }


    suspend fun uploadToFirebase(imageUri: Uri, temperature: String, imageName: String, index: Int) {
        Log.d("TAG", "----------------------------------------------------")
        Log.d("TAG", "imageUri : " + imageUri.toString())
        Log.d("TAG", "temperature : " + temperature.toString())
        Log.d("TAG", "imageName : " + imageName.toString())
        Log.d("TAG", "index : " + index.toString())
        Log.d("TAG", "----------------------------------------------------")
        val storageRef = storage.reference
        val fileRef =
            storageRef.child("clothes/${temperature}/${imageName}.png")
        fileRef.putFile(imageUri)
            .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                    fileRef.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
                        override fun onSuccess(uri: Uri?) {
                            val clothes = Clothes(uri.toString())
//                        val modelId = dbReference.key
//                        if (modelId != null) {
                            dbReference.child(temperature).child("$index").setValue(clothes)
//                        }
                        }
                    })
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Log.e("TAG", "error")
                }
            }).await()
    }


    fun resourceToUri(context: Context, resID: Int): Uri? {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    context.resources.getResourcePackageName(resID) + '/' +
                    context.resources.getResourceTypeName(resID) + '/' +
                    context.resources.getResourceEntryName(resID)
        )
    }
}