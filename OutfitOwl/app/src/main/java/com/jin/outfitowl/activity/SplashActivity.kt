package com.jin.outfitowl.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jin.outfitowl.R
import com.jin.outfitowl.data.Permission
import com.jin.outfitowl.databinding.ActivitySplashBinding
import com.jin.outfitowl.manager.LocationManager

class SplashActivity : AppCompatActivity() {
    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_splash)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        checkPermission()
    }

    private fun checkPermission() {
        val permissions = Permission.checkBuildPermission(Build.VERSION.SDK_INT)
        val deniedPermissions = permissions.filterNot { Permission.checkPermission(this, it) }
        if (deniedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, deniedPermissions.toTypedArray(),
                Permission.REQUEST_CODE_PERMISSION.number
            )
            return
        }
        binding.progress.visibility = View.GONE
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permission.REQUEST_CODE_PERMISSION.number) {
            val allPermissionGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionGranted) {
                binding.progress.visibility = View.GONE
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
//                LocationManager.getCurrentLocation(this@SplashActivity)
            } else {
                Log.e("TAG", "allow permission")
            }
        } else {
            Log.e("TAG", "allow permission")
        }
    }
}