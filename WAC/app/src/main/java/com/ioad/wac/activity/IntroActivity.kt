package com.ioad.wac.activity

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ioad.wac.R
import com.ioad.wac.adapter.IntroAdapter
import com.ioad.wac.model.Clothes
import com.ioad.wac.model.ClothesImages

class IntroActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null

    val clothesImages = ClothesImages()

    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100

    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            Log.d("TAG", token)

        })


        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "empty")

        when (token) {
            "empty" -> {
                val vpIntro = findViewById<ViewPager2>(R.id.vp_intro)
                vpIntro.adapter = IntroAdapter(this, 2)
            }
            else -> {
                startActivity(Intent(this, MainBoardActivity2::class.java))
            }
        }

        checkPermission()
        insertClothesImages()


    } // onCreate


    fun insertClothesImages() {

        val token = token.replace("/", "_")
        Log.e("TAG", token)
        val temperatureList: List<Map<String, Int>> = listOf(
            clothesImages.temperature28,
            clothesImages.temperature23to27,
            clothesImages.temperature20to22,
            clothesImages.temperature12to19,
            clothesImages.temperature9to11,
            clothesImages.temperature4to8,
            clothesImages.temperature3
        )

        temperatureList.forEachIndexed { listIndex, list ->
            Log.d("TAG", "index :: " + listIndex)
            var temperature = ""
            when (listIndex) {
                0 -> temperature = "28"
                1 -> temperature = "23to27"
                2 -> temperature = "20to22"
                3 -> temperature = "12to19"
                4 -> temperature = "9to11"
                5 -> temperature = "4to8"
                else -> temperature = "3"
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                var index = 1
                list.forEach { name, value ->
                    val name = name
                    val imageUri: Uri? = resourceToUri(this, value)
                    val clothes = Clothes(imageUri, name)
                    firestore?.collection(temperature)?.document((index).toString())?.set(clothes)
                    index++
                }
            }
        }

    }


    fun checkPermission() {
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
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG)
                    .show()
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
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
            var isCheckResult = true
            for (result: Int in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isCheckResult = false
                    break
                }
            }
            if (isCheckResult) {
            } else {
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

    fun resourceToUri(context: Context, resID: Int): Uri? {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    context.resources.getResourcePackageName(resID) + '/' +
                    context.resources.getResourceTypeName(resID) + '/' +
                    context.resources.getResourceEntryName(resID)
        )
    }
}