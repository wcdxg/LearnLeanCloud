package com.yuaihen.leancloud

import android.app.Application
import cn.leancloud.AVOSCloud
import cn.leancloud.AVObject

/**
 * Created by Yuaihen.
 * on 2021/2/25
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 提供 this、App ID、绑定的自定义 API 域名作为参数
        AVOSCloud.initialize(
            this,
            "aJFima9qQkPJLwOu3BiLqOhV-gzGzoHsz",
            "VgqgVP8w7BA8R0FnCeTIPTF8",
            "https://ajfima9q.lc-cn-n1-shared.com"
        )

    }
}