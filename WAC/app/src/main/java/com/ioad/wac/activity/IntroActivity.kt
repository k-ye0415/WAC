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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.ioad.wac.R
import com.ioad.wac.adapter.IntroAdapter
import com.ioad.wac.model.Clothes
import com.ioad.wac.model.ClothesImages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class IntroActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var dbReference: DatabaseReference
    lateinit var storage: FirebaseStorage
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    val clothesImages = ClothesImages()

    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100

    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "empty")

        Log.e("TAG", "tokeen?? " + token)

        when (token) {
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

//        requestPermission()
        checkRunTimePermission()
//        settingList()
        insertClothesImages()

    } // onCreate


    fun insertClothesImages() {
//        val temperatureList: List<List<Int>> = listOf(
//            clothesImages.temperature,
////            temperature23to27
//        )

        val tempList = listOf<Int>(
                        R.drawable.sleeveless,
            R.drawable.tshirt,
            R.drawable.short_pants,
            R.drawable.skirt
        )
        firestore?.collection("ClothesImages")?.document("28")?.set(tempList)

    }



    fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG)
                    .show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String?>,
        grandResults: IntArray
    ) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults)
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result: Int in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                //위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[2]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[3]
                    )
                ) {
                    Toast.makeText(
                        this,
                        "권한설정이 거부되었습니다. 앱을 다시 실행하여 권한을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "권한설정이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun settingList() {
        val temperatureList: List<List<Int>> = listOf(
            clothesImages.temperature,
//            temperature23to27
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


    suspend fun uploadToFirebase(
        imageUri: Uri,
        temperature: String,
        imageName: String,
        index: Int
    ) {
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