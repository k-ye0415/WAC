package com.jin.outfitowl.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.jin.outfitowl.data.Permission
import com.jin.outfitowl.databinding.ActivitySplashBinding
import com.jin.outfitowl.util.TAG

class SplashActivity : AppCompatActivity() {
    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//        val intent = Intent(this@SplashActivity, MainActivity::class.java)
//        startActivity(intent)
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
//                val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                startActivity(intent)
            } else {
                Log.e(TAG.PERMISSION.label, "allow permission")
            }
        } else {
            Log.e(TAG.PERMISSION.label, "allow permission")
        }
    }
}