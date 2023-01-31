package com.example.guru2_android_10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val register = findViewById<Button>(R.id.register)

        register.setOnClickListener{
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        }

        val id = findViewById<EditText>(R.id.login_id)
        val pw = findViewById<EditText>(R.id.login_pw)

        var message: String = ""

        val login = findViewById<Button>(R.id.login)
        login.isEnabled = false

        //id 입력란에 값이 입력 되었는지 확인하는 부분
        id.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = id.text.toString()
                login.isEnabled = message.isBlank()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        //pw 입력란에 값이 입력 되었는지 확인하는 부분
        pw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = pw.text.toString()
                login.isEnabled = message.isNotEmpty()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        //id, pw 입력란에 값이 모두 입력되었을 경우 로그인 버튼이 활성화 된다.

        //로그인 버튼을 누르면 토스트 메시지를 띄움과 함께 메인 화면으로 넘어간다.
        login.setOnClickListener {
            Toast.makeText(this.getApplicationContext(), "로그인했습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
}