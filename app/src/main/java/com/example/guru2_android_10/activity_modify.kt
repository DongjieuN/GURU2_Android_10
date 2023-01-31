package com.example.guru2_android_10

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class activity_modify : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        val button = findViewById<Button>(R.id.modi_button)
        val disease = findViewById<EditText>(R.id.modi_disease)
        val place = findViewById<EditText>(R.id.modi_place)
        val num1 = findViewById<EditText>(R.id.modi_num1)
        val num2 = findViewById<EditText>(R.id.modi_num2)
        val home = findViewById<EditText>(R.id.modi_home)

        val intent = Intent(this, activity_info::class.java)


        button.setOnClickListener{
            //사용자가 정보 입력을 마쳤을 때, 각 정보 입력값을 회원 정보를 보여주는 화면에 전달한다.
            intent.putExtra("disease",disease.text.toString())
            intent.putExtra("place",place.text.toString())
            intent.putExtra("number1",num1.text.toString())
            intent.putExtra("number2",num2.text.toString())
            intent.putExtra("home",home.text.toString())

            //토스트 메시지를 띄우고 수정된 회원 정보를 보여주는 화면으로 넘어간다.
            Toast.makeText(this.getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

}