package com.yuaihen.leancloud

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.leancloud.AVUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
                .get(MyViewModel::class.java)

        val myAdapter = MyAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = myAdapter
        }

        myViewModel.dataListLive.observe(this) {
            myAdapter.updateDataList(it)
        }

        floatingActionButton.setOnClickListener {
            val v = layoutInflater.inflate(R.layout.new_word_dialog, null)
            AlertDialog.Builder(this)
                .setView(v)
                .setTitle("添加")
                .setPositiveButton(
                    "确定"
                ) { _, _ ->
                    val newWord = v.findViewById<EditText>(R.id.editNewWord).text.toString()
                    myViewModel.addWord(newWord)
                }
                .setNegativeButton(
                    "取消"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuLogout) {
            AVUser.logOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}