package com.ioad.wac.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ioad.wac.LocationDB
import com.ioad.wac.R
import com.ioad.wac.adapter.LocationAdapter
import com.ioad.wac.databinding.ActivityChangeLocationBinding
import com.ioad.wac.model.Location
import jxl.Workbook
import jxl.read.biff.BiffException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class ChangeLocationActivity : AppCompatActivity() {

    lateinit var etLocation: EditText
    lateinit var btnSearch: Button
    lateinit var rvLocation: RecyclerView

    lateinit var db: LocationDB

    var nx = ""
    var ny = ""
    var locationList = ArrayList<String>()
    var location = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location)

        db = LocationDB.getInstance(this)!!

        etLocation = findViewById(R.id.et_location)
        btnSearch = findViewById(R.id.btn_search)
        rvLocation = findViewById(R.id.rv_location)

        etLocation.doAfterTextChanged {
            location = it.toString().trim()
        }
        
        btnSearch.setOnClickListener {
            readExcel(location)

            addLocation()

        }
    }


    private fun addLocation() {
        var locationData = Location(location, false)
        CoroutineScope(Dispatchers.IO).launch {
            db.locationDAO().insertLocation(locationData)
        }
    }


    fun readExcel(localName: String?) {
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
                            locationList.add(contents)
                        }
                        row++
                    }
                }
            }

            rvLocation.adapter = LocationAdapter(locationList, LayoutInflater.from(this), this)

        } catch (e: IOException) {
            Log.i("READ_EXCEL1", e.message!!)
            e.printStackTrace()
        } catch (e: BiffException) {
            Log.i("READ_EXCEL1", e.message!!)
            e.printStackTrace()
        }
        // x, y = String형 전역변수
        Log.i("격자값", "x = " + nx + "  y = " + ny)
    }
}