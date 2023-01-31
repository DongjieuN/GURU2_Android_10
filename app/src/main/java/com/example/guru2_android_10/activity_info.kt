package com.example.guru2_android_10

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class activity_info : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        val info_disease = findViewById<TextView>(R.id.disease)
        val info_place = findViewById<TextView>(R.id.place)
        val info_number1 = findViewById<TextView>(R.id.number1)
        val info_number2 = findViewById<TextView>(R.id.number2)
        val info_home = findViewById<TextView>(R.id.home)

        //수정 화면에서 입력한 내용을 받아와 텍스트뷰를 세팅한다.
        info_disease.text = intent.getStringExtra("disease")
        info_place.text = intent.getStringExtra("place")
        info_number1.text = intent.getStringExtra("number1")
        info_number2.text = intent.getStringExtra("number2")
        info_home.text = intent.getStringExtra("home")

        val main_button = findViewById<Button>(R.id.mainbt)

        //하단에 존재하는 버튼을 누르면 메인화면으로 돌아간다.
        main_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}

