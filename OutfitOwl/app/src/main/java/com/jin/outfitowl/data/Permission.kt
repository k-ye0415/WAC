package com.jin.outfitowl.data

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

enum class Permission(val number: Int) {
    REQUEST_CODE_PERMISSION(1002);

    companion object {

        fun checkBuildPermission(version: Int): List<String> {
            var permissions = ArrayList<String>()
            val defaultPermission = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
//            if (version >= Build.VERSION_CODES.Q) {
//                permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
//            }
//            when (version) {
//                Build.VERSION_CODES.S -> {
//                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
//                    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
//                    permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
//                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    permissions.addAll(defaultPermission)
//                }
//
//                Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, 35 -> { // VANILLA_ICE_CREAM version 임시 적용.
//                    permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
//                    permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
//                    permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
//                    permissions.add(Manifest.permission.POST_NOTIFICATIONS)
//                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
//                    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
//                    permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
//                    permissions.addAll(defaultPermission)
//                }
//
//                else -> {
//                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    permissions.addAll(defaultPermission)
//                }
//            }
            return defaultPermission
        }

        fun checkPermission(activity: Activity, permission: String): Boolean {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }
    }
}