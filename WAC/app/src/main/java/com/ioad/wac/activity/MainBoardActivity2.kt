package com.ioad.wac.activity

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ioad.wac.ApiObject
import com.ioad.wac.ITEM
import com.ioad.wac.R
import com.ioad.wac.WEATHER
import com.ioad.wac.adapter.MainClothesAdapter
import com.ioad.wac.model.Clothes
import com.ioad.wac.model.Weather
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainBoardActivity2 : AppCompatActivity() {

    lateinit var rvClothes: RecyclerView
    lateinit var rvAccessories: RecyclerView
    lateinit var glide: RequestManager
    lateinit var database: FirebaseDatabase
    lateinit var dbReference: DatabaseReference
    val fireStore = FirebaseFirestore.getInstance()
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val pathRef = storageRef.child("clothes")
    val listRef = pathRef.listAll()
    var list = ArrayList<Clothes>()

    lateinit var tvNowTemp: TextView
    lateinit var tvRainPercent: TextView
    lateinit var tvRainMM: TextView
    lateinit var ivMainBg: ImageView
    lateinit var tvNowFcstTime: TextView


    private var base_date = ""  // 발표 일자
    private var base_time = ""      // 발표 시각
    private var nx = "60"               // 예보지점 X 좌표
    private var ny = "125"              // 예보지점 Y 좌표
    private var nowTemp: String = ""
    private var nowSky:String = ""
    private var nowRainPercent:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_board)


        val scope = GlobalScope

        tvNowTemp = findViewById(R.id.tv_now_temp)
        tvRainPercent = findViewById(R.id.tv_rain_percent)
        tvRainMM = findViewById(R.id.tv_rain_mm)
        ivMainBg = findViewById(R.id.iv_main_bg)
        tvNowFcstTime = findViewById(R.id.tv_now_fcst)

        database = FirebaseDatabase.getInstance()
        dbReference = database.reference.child("images")
        glide = Glide.with(this)

        rvClothes = findViewById(R.id.rv_clothes)
        rvClothes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvAccessories = findViewById(R.id.rv_accessories)
        rvAccessories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        setWeather(nx, ny)
    }


    fun getImages(context: Context, nowTemp: String) {
        var tempInt = nowTemp.toInt()
        var temp: String = ""
        when (tempInt) {
            in 28..1000 -> temp = "28"
            in 23..27 -> temp = "23to27"
            else -> temp = "error"
        }

        dbReference.child(temp).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                var uri:Uri? = null
                value.forEachIndexed { position, data ->
                    val data = data.children
                    data.forEach { it ->
                        uri = Uri.parse(it.value.toString())
                    }
                    val clothes = Clothes(uri)
                    list.add(position, clothes)
                }
                Log.d("TAG", "list size :: " + list.size)
                list.forEachIndexed { index, clothes ->
                    Log.e("TAG", "list :: index - ${index} / uri - ${list.get(index).imageUri}")
                }
                rvClothes.adapter = MainClothesAdapter(
                    list,
                    LayoutInflater.from(this@MainBoardActivity2),
                    glide
                )

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    // 날씨 가져와서 설정하기
    fun setWeather(nx: String, ny: String) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("mm", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        base_time = getBaseTime(timeH, timeM)
        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && base_time == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = ApiObject.retrofitService.GetWeather(60, 1, "JSON", base_date, base_time, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val list: List<ITEM> = response.body()!!.response.body.items.item

                    // 최신 온도, 강수확률, 강수량 구하기
                    val lastTime = list.get(list.size - 1).fcstTime
                    list.forEach { item ->
                        if (lastTime.equals(item.fcstTime)) {
                            when (item.category) {
                                "PCP" -> {
                                    tvRainMM.text = item.fcstValue
                                }
                                "SKY" -> {
                                    setSkyImage(item.fcstValue)
                                }
                                "POP" -> {
                                    tvRainPercent.text = item.fcstValue
                                    nowRainPercent = item.fcstValue
                                }
                                "TMP" -> {
                                    tvNowTemp.text = item.fcstValue
                                    nowTemp = item.fcstValue
                                }
                                else -> Log.d("TAG", "넌 누구니? ${item.category} :: " + item.fcstValue)
                            }
                        }
                    }

                    tvNowFcstTime.text =
                        "현재시각 - ${timeH} : ${timeM} / 측정시각 - ${lastTime.substring(0, 2)} : 00"
                    getImages(this@MainBoardActivity2, nowTemp)
                    getAccessories(nowSky, nowRainPercent)
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }

        })
    }


    private fun getAccessories(sky:String, rainPercent:String) {
        if (sky.equals("1")) {

        }
    }


    // baseTime 설정하기
    private fun getBaseTime(h: String, m: String): String {
        var result = ""
        Log.e("TAG", "hour :: " + h)
        Log.e("TAG", "mimute :: " + m)

        when (h) {
            in "00".."02" -> result = "2000"    // 00~02
            in "03".."05" -> result = "2300"    // 03~05
            in "06".."08" -> result = "0200"    // 06~08
            in "09".."11" -> result = "0500"    // 09~11
            in "12".."14" -> result = "0800"    // 12~14
            in "15".."17" -> result = "1100"    // 15~17
            in "18".."20" -> result = "1400"    // 18~20
            else -> result = "1700"
        }

        return result
    }


    fun setSkyImage(sky: String) {
        when (sky) {
            "1" -> { // 맑음
                glide.load(resourceToUri(this, R.drawable.clear_sky)).centerCrop().into(ivMainBg)
                nowSky = "1"
            }
            "3" -> { // 구름
                glide.load(resourceToUri(this, R.drawable.cloud)).centerCrop().into(ivMainBg)
                nowSky = "3"
            }
            else -> { // 흐림
                glide.load(resourceToUri(this, R.drawable.dark_cloud)).centerCrop().into(ivMainBg)
                nowSky = "4"
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