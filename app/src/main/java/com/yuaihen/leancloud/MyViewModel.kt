package com.yuaihen.leancloud

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.leancloud.AVException
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.AVUser
import cn.leancloud.livequery.AVLiveQuery
import cn.leancloud.livequery.AVLiveQueryEventHandler
import cn.leancloud.livequery.AVLiveQuerySubscribeCallback
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Yuaihen.
 * on 2021/2/26
 */
class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val _dataListLive = MutableLiveData<List<AVObject>>()
    val dataListLive = _dataListLive

    init {
        val query = AVQuery<AVObject>("Word")
        query.whereEqualTo("user", AVUser.currentUser())
        query.findInBackground().subscribe(object : Observer<List<AVObject>> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<AVObject>) {
                _dataListLive.value = t
            }

            override fun onError(e: Throwable) {
                Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {
            }

        })

        val liveQuery = AVLiveQuery.initWithQuery(query)
        liveQuery.subscribeInBackground(object : AVLiveQuerySubscribeCallback() {
            override fun done(e: AVException?) {
                //订阅成功
            }

        })
        liveQuery.setEventHandler(object : AVLiveQueryEventHandler() {
            override fun onObjectCreated(avObject: AVObject) {
                super.onObjectCreated(avObject)
                val t = _dataListLive.value?.toMutableList()
                t?.add(avObject)
                _dataListLive.value = t
            }

            override fun onObjectDeleted(objectId: String?) {
                super.onObjectDeleted(objectId)
                val t = _dataListLive.value?.toMutableList()
                val ob = t?.find {
                    it.get("objectId") == objectId
                }
                t?.remove(ob)
                _dataListLive.value = t
            }

            override fun onObjectUpdated(avObject: AVObject, updateKeyList: MutableList<String>) {
                super.onObjectUpdated(avObject, updateKeyList)
                val ob = _dataListLive.value?.find {
                    it.get("objectId") == avObject.get("objectId")
                }
                updateKeyList.forEach {
                    ob?.put(it, avObject.get(it))
                }
                _dataListLive.value = _dataListLive.value
            }
        })
    }

    fun addWord(newWord: String) {
        AVObject("Word").apply {
            put("word", newWord)
            put("user", AVUser.getCurrentUser())
            saveInBackground().subscribe(object : Observer<AVObject> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AVObject) {
                    Toast.makeText(getApplication(), "添加成功", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(getApplication(), "${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }
            })
        }
    }


}