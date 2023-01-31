package com.example.guru2_android_10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


class activity_loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        //로딩 화면 이후 어느 화면으로 넘어갈지 지정. 로그인 화면으로 넘어간다.
        Handler().postDelayed({
            val intent = Intent(this, activity_login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)

    }

    //몇 초 간 로딩화면 띄울지 지정. 2초동안 로딩 화면을 띄운다.
    companion object {
        private const val DURATION : Long = 2000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}