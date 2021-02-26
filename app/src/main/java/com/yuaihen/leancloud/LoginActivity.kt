package com.yuaihen.leancloud

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.leancloud.AVUser
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editUserName.addTextChangedListener(watcher)
        editPassword.addTextChangedListener(watcher)
        progressBar.visibility = View.INVISIBLE
        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        btnLogin.setOnClickListener {
            val name = editUserName.text?.trim().toString()
            val pwd = editPassword.text?.trim().toString()
            if (name.isEmpty() or pwd.isEmpty()) return@setOnClickListener
            AVUser.logIn(name, pwd).subscribe(object : Observer<AVUser> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AVUser) {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "登录失败 ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.d("hello", "onError: ${e.message}")
                    progressBar.visibility = View.INVISIBLE
                }

                override fun onComplete() {
                }
            })
        }
    }


    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val t1 = editUserName.text.toString().trim().isNotEmpty()
            val t2 = editPassword.text.toString().trim().isNotEmpty()
            btnLogin.isEnabled = t1 and t2
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
}