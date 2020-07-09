package com.demon.imapp.data

import android.annotation.SuppressLint
import com.demon.imapp.util.SPUtil
import com.demon.imapp.util.gson
import java.text.SimpleDateFormat

/**
 * @author DeMon
 * Created on 2020/5/14.
 * E-mail 757454343@qq.com
 * Desc:
 */
class Message {
    var type: Int = 0
    var name: String = ""
    var content: String = ""
    var time: Long = 0L
    var to: String = ""

    constructor(content: Any?) {
        val msg = gson.fromJson(content.toString(), Message::class.java)
        this.type = msg.type
        this.name = msg.name
        this.content = msg.content
        this.time = msg.time
    }

    constructor(content: String) {
        this.content = content
        this.name = SPUtil.getInstance().get(Const.NAME, "") as String
        this.to = SPUtil.getInstance().get(Const.FRIEND, "") as String
        this.time = System.currentTimeMillis()
    }

    constructor(name: String, content: String) {
        this.name = name
        this.content = content
        this.time = System.currentTimeMillis()
    }

    constructor(type: Int) {
        this.type = type
        this.content = "[初始服务器注册]"
        this.name = SPUtil.getInstance().get(Const.NAME, "") as String
        this.time = System.currentTimeMillis()
        this.to = "[服务器]"
    }


    @SuppressLint("SimpleDateFormat")
    fun getTimeStr(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(this.time)
    }

    override fun toString(): String {
        return "[${getTimeStr()}]-$name：$content"
    }
}