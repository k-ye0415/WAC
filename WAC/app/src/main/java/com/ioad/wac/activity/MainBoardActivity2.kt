package com.ioad.wac.activity

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ioad.wac.*
import com.ioad.wac.R
import com.ioad.wac.adapter.AccessoriesAdapter
import com.ioad.wac.adapter.MainClothesAdapter
import com.ioad.wac.model.Accessories
import com.ioad.wac.model.Clothes
import jxl.Workbook
import jxl.read.biff.BiffException
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.os.Build
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import kotlin.concurrent.thread


class MainBoardActivity2 : AppCompatActivity() {

    lateinit var glide: RequestManager
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null
    var clotheslist = ArrayList<Clothes>()
    var accessoriesList = ArrayList<Accessories>()
    lateinit var clothes: Clothes

    lateinit var rvClothes: RecyclerView
    lateinit var rvAccessories: RecyclerView
    lateinit var tvNowTemp: TextView
    lateinit var tvRainPercent: TextView
    lateinit var ivMainBg: ImageView
    lateinit var tvLocation: TextView
    lateinit var tvChangeLocation: TextView
    lateinit var tvNowTemp2: TextView
    lateinit var tvRainPercent2: TextView
    lateinit var tvRainPercent3: TextView
    lateinit var tvTodayClothes: TextView
    lateinit var tvAccessories: TextView
    lateinit var pbLoading: ProgressBar
    lateinit var llProgress: LinearLayout

    private var gpsTracker: GpsTracker? = null

    private var base_date = ""  // 발표 일자
    private var base_time = ""      // 발표 시각
    private var nx = ""               // 예보지점 X 좌표
    private var ny = ""              // 예보지점 Y 좌표
    private var nowTemp: String = ""
    private var nowSky: String = ""
    private var nowRainPercent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main_board)

        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView)

        windowInsetsController?.isAppearanceLightNavigationBars = true
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        tvNowTemp = findViewById(R.id.tv_now_temp)
        tvRainPercent = findViewById(R.id.tv_rain_percent)
        ivMainBg = findViewById(R.id.iv_main_bg)
        tvLocation = findViewById(R.id.tv_location)
        tvChangeLocation = findViewById(R.id.tv_change_location)
        tvNowTemp2 = findViewById(R.id.tv_now_temp_2)
        tvRainPercent2 = findViewById(R.id.tv_rain_percent_2)
        tvRainPercent3 = findViewById(R.id.tv_rain_percent_3)
        tvTodayClothes = findViewById(R.id.tv_today_clothes)
        tvAccessories = findViewById(R.id.tv_accessories)
        pbLoading = findViewById(R.id.pb_loading)
        llProgress = findViewById(R.id.ll_progress)

        progressBarInit()

        glide = Glide.with(this)

        rvClothes = findViewById(R.id.rv_clothes)
        rvClothes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvAccessories = findViewById(R.id.rv_accessories)
        rvAccessories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        tvChangeLocation.setOnClickListener {
            startActivity(Intent(this, ChangeLocationActivity::class.java))
        }


        setAllView()

    }

    override fun onResume() {
        super.onResume()


        val intent = intent
        val intentLocation = intent.getStringExtra("SEARCH_LOCATION")
        if (intentLocation != null) {
            showProgress(true)
            thread(start = true) {
                Thread.sleep(1000)

                runOnUiThread {
                    showProgress(false)
                    val local = intentLocation.toString().split(" ")
                    Log.e("TAG", "intent " + local.toString())
                    val location = local[2]
                    tvLocation.text = "${local[2]}"
                    readExcel(location)
                }
            }
        }


    }

    fun setAllView() {
        showProgress(true)

        thread(start = true) {
            Thread.sleep(1000)

            runOnUiThread {
                showProgress(false)
                // 위치정보 가져오기
                getLocation()

                // 날씨 정보 가져오기
                setWeather(nx, ny)
            }
        }
    }

    fun progressBarInit() {
        showProgress(false)
    }

    fun showProgress(isShow: Boolean) {
        if (isShow) {
            pbLoading.visibility = View.VISIBLE
            llProgress.visibility = View.VISIBLE
        } else {
            pbLoading.visibility = View.GONE
            llProgress.visibility = View.GONE
        }
    }

    private fun getLocation() {
        gpsTracker = GpsTracker(this)

        val latitude = gpsTracker!!.latitude
        val longitude = gpsTracker!!.longitude

        val address = getCurrentAddress(latitude, longitude)
        Log.e("TAG", address.toString())
        val local = address.toString().split(" ")
        Log.e("TAG", local.toString())
        val location = local[2]
        tvLocation.text = "${local[3]}"
        readExcel(location)

    }

    fun getCurrentAddress(latitude: Double, longitude: Double): String? {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address = addresses[0]
        return address.getAddressLine(0).toString() + "\n"
    }

    fun readExcel(localName: String?) {
        Log.e("TAG", "readExcel localName :: " + localName)
        try {
            val inputStream: InputStream = baseContext.resources.assets.open("local_name.xls")
            val wb: Workbook = Workbook.getWorkbook(inputStream)
            if (wb != null) {
                val sheet = wb.getSheet(0) // 시트 불러오기
                if (sheet != null) {
                    val colTotal = sheet.columns // 전체 컬럼
                    val rowIndexStart = 1 // row 인덱스 시작
                    val rowTotal = sheet.getColumn(colTotal - 1).size
                    var row = rowIndexStart
                    while (row < rowTotal) {
                        val area = sheet.getCell(0, row).contents
                        val zone = sheet.getCell(1, row).contents
                        val local = sheet.getCell(2, row).contents
                        val contents = area + " " + zone + " " + local
                        if (contents.contains(localName!!)) {
                            nx = sheet.getCell(3, row).contents
                            ny = sheet.getCell(4, row).contents
                            row = rowTotal
                        }
                        row++
                    }
                }
            }
        } catch (e: IOException) {
            Log.i("READ_EXCEL1", e.message!!)
            e.printStackTrace()
        } catch (e: BiffException) {
            Log.i("READ_EXCEL1", e.message!!)
            e.printStackTrace()
        }
        // x, y = String형 전역변수
        Log.i("TAG", "x = " + nx + "  y = " + ny)
    }


    fun setClothesListImages(context: Context, nowTemp: String) {
        var tempInt = nowTemp.toInt()
        var temp: String = ""
        when (tempInt) {
            in 28..1000 -> temp = "28"
            in 23..27 -> temp = "23to27"
            in 20..22 -> temp = "20to22"
            in 12..19 -> temp = "12to19"
            in 9..11 -> temp = "9to11"
            in 4..8 -> temp = "4to8"
            in -50..3 -> temp = "3"
            else -> temp = "error"
        }
        var uri: Uri? = null
        var name: String? = null
        firestore?.collection(temp)?.get()?.addOnSuccessListener { data ->
            clotheslist.clear()
            data.forEach { value ->
                uri = Uri.parse(value["imageUri"] as String)
                name = value["imageName"] as String
                clothes = Clothes(uri, name)
                clotheslist.add(clothes)
            }

            rvClothes.adapter = MainClothesAdapter(
                clotheslist,
                LayoutInflater.from(this@MainBoardActivity2),
                glide
            )
        }

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
//        val call = ApiObject.retrofitService.GetWeather(60, 1, "JSON", base_date, "0800", nx, ny)

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
                                else -> Log.d("TAG", "${item.category} :: " + item.fcstValue)
                            }
                        }
                    }
                    setClothesListImages(this@MainBoardActivity2, nowTemp)
                    setAccessoriesImages(nowSky, nowRainPercent)
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }

        })
    }


    private fun setAccessoriesImages(sky: String, rainPercent: String) {
        val rainPercentInt = rainPercent.toInt()
        val rainList: List<Int> = listOf(
            R.drawable.umbrella,
            R.drawable.boots
        )

        val rainListName: List<String> = listOf(
            "우산",
            "레인부츠"
        )
        if (sky.equals("1")) {
            val accessories = Accessories(resourceToUri(this, R.drawable.sunglasses), "선글라스")
            accessoriesList.add(accessories)
        }

        if (rainPercentInt >= 60) {
            rainList.forEachIndexed { index, it ->
                val accessories = Accessories(resourceToUri(this, it), rainListName.get(index))
                accessoriesList.add(accessories)
            }
        }

        rvAccessories.adapter = AccessoriesAdapter(
            accessoriesList,
            LayoutInflater.from(this),
            glide
        )

    }


    // baseTime 설정하기
    private fun getBaseTime(h: String, m: String): String {
        var result = ""

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
        val cal = Calendar.getInstance()
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각

        when (timeH.toInt()) {
            in 6..18 -> {
                Log.e("TAG", "Morning and DayTime")
                when (sky) {
                    "1" -> { // 맑음
                        glide.load(resourceToUri(this, R.drawable.clear_sky)).centerCrop()
                            .into(ivMainBg)
                        nowSky = "1"
                        tvLocation.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvNowTemp.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvRainPercent.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvNowTemp2.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvRainPercent2.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvRainPercent3.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvTodayClothes.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvAccessories.setTextColor(ContextCompat.getColor(this, R.color.black))
                    }
                    "3" -> { // 구름
                        glide.load(resourceToUri(this, R.drawable.cloud)).centerCrop()
                            .into(ivMainBg)
                        nowSky = "3"
                        tvLocation.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvNowTemp.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvRainPercent.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvNowTemp2.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvRainPercent2.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvRainPercent3.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvTodayClothes.setTextColor(ContextCompat.getColor(this, R.color.black))
                        tvAccessories.setTextColor(ContextCompat.getColor(this, R.color.black))
                    }
                    else -> { // 흐림
                        glide.load(resourceToUri(this, R.drawable.dark_cloud)).centerCrop()
                            .into(ivMainBg)
                        nowSky = "4"
                    }
                }
            }
            else -> {
                Log.e("TAG", "Evening or Night")
                when (sky) {
                    "1" -> { // 맑음
                        glide.load(resourceToUri(this, R.drawable.clear_sky_night)).centerCrop()
                            .into(ivMainBg)
                        nowSky = "1"
                    }
                    "3" -> { // 구름
                        glide.load(resourceToUri(this, R.drawable.cloud_night)).centerCrop()
                            .into(ivMainBg)
                        nowSky = "3"
                    }
                    else -> { // 흐림
                        glide.load(resourceToUri(this, R.drawable.dark_cloud_night)).centerCrop()
                            .into(ivMainBg)
                        nowSky = "4"
                    }
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