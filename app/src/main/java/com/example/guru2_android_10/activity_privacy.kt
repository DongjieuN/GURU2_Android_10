package com.example.guru2_android_10

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.guru2_android_10.R.*


class activity_privacy : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_privacy)

        val p_register = findViewById<Button>(R.id.privacy_register)
        val cancel = findViewById<Button>(R.id.privacy_cancel)

        //가입 버튼을 누르면 토스트 메시지를 띄움과 동시에 로그인 화면으로 이동한다.
        p_register.setOnClickListener{
            Toast.makeText(this.getApplicationContext(), "가입되었습니다. 로그인 해 주세요.", Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this, activity_login::class.java)
            startActivity(intent2)
        }

        //뒤로가기 버튼을 누르면 현재 액티비티를 종료한다.
        cancel.setOnClickListener {
            finish();
        }
    }
}