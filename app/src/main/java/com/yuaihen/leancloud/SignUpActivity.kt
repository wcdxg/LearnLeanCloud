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
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editSignUpUserName.addTextChangedListener(watcher)
        editSignUpPassword.addTextChangedListener(watcher)
        progressBar2.visibility = View.INVISIBLE

        btnSignUpConfirm.setOnClickListener {
            val name = editSignUpUserName.text?.trim().toString()
            val pwd = editSignUpPassword.text?.trim().toString()
            if (name.isEmpty() or pwd.isEmpty()) return@setOnClickListener
            AVUser().apply {
                progressBar2.visibility = View.VISIBLE
                username = name
                password = pwd
                signUpInBackground().subscribe(object : Observer<AVUser> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: AVUser) {
                        Toast.makeText(this@SignUpActivity, "注册成功", Toast.LENGTH_SHORT).show()
                        AVUser.logIn(name, pwd).subscribe(object : Observer<AVUser> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(t: AVUser) {
                                startActivity(
                                    Intent(
                                        this@SignUpActivity,
                                        MainActivity::class.java
                                    ).also {
                                        it.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    })
                                finish()
                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "登录失败 ${e.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                Log.d("hello", "onError: ${e.message}")
                                progressBar2.visibility = View.INVISIBLE
                            }

                            override fun onComplete() {
                            }
                        })
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(this@SignUpActivity, "注册失败 ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("hello", "onError: ${e.message}")
                        progressBar2.visibility = View.INVISIBLE
                    }

                    override fun onComplete() {
                    }
                })
            }

        }
    }


    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val t1 = editSignUpUserName.text.toString().trim().isNotEmpty()
            val t2 = editSignUpPassword.text.toString().trim().isNotEmpty()
            btnSignUpConfirm.isEnabled = t1 and t2
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
}