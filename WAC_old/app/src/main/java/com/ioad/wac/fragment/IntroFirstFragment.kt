package com.ioad.wac.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ioad.wac.R
import com.ioad.wac.activity.MainBoardActivity2

class IntroFirstFragment:Fragment() {

    var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro_frist, container, false)
        val ivCuriosity = view.findViewById<ImageView>(R.id.iv_curiosity)
        val ivFirstBg = view.findViewById<ImageView>(R.id.iv_first_bg)
        activity?.let {
            Glide.with(it).load(R.drawable.four_seasons).centerCrop().into(ivFirstBg)
            Glide.with(it).load(R.drawable.curiosity).into(ivCuriosity)
        }

        //https://www.pinterest.co.kr/pin/161707442853887964/
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            Log.d("TAG", token)

        })



        val btnIntro = view.findViewById<TextView>(R.id.btn_intro).setOnClickListener {
            val sharedPreferences = activity?.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putString("TOKEN", token)
            editor.commit()
            startActivity(Intent(activity, MainBoardActivity2::class.java))
        }

    }
}