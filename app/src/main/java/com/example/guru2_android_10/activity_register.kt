package com.example.guru2_android_10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val next = findViewById<Button>(R.id.register_nextbutton)
        val cancel = findViewById<Button>(R.id.register_cancel)

        val register_id = findViewById<EditText>(R.id.register_id)
        val register_pw = findViewById<EditText>(R.id.register_pw)

        var message1: String = ""
        var message2: String = ""

        //가입 화면에서 id 입력란에 값이 입력 되었는지 확인하는 부분
        register_id.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message1 = register_id.text.toString()
                next.isEnabled = message1.isBlank()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        //가입 화면에서 pw 입력란에 값이 입력 되었는지 확인하는 부분
        register_pw.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message2 = register_pw.text.toString()
                next.isEnabled = message2.isNotEmpty()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        //id, pw 모두 입력 되었다면 다음 화면으로 넘어가는 버튼을 활성화한다.

        //다음 화면으로 넘어가는 버튼을 누르면 회원 정보를 입력하는 화면으로 넘어간다.
        next.setOnClickListener{
            val intent = Intent(this, activity_privacy::class.java)
            startActivity(intent)
        }

        //뒤로가기 버튼을 누르면 현재 액티비티를 종료한다.
        cancel.setOnClickListener {
            finish();
        }
    }
}