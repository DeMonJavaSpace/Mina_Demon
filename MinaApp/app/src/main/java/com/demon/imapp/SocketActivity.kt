package com.demon.imapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.demon.imapp.data.Const
import com.demon.imapp.data.Message
import com.demon.imapp.mina.MinaHelper
import com.demon.imapp.util.SPUtil
import com.demon.imapp.util.hideSoft
import com.demon.imapp.util.toast
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_socket.*

class SocketActivity : AppCompatActivity() {

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)

        val ip = SPUtil.getInstance().get(Const.IP, "") as String
        val port = SPUtil.getInstance().get(Const.PORT, "") as String

        tvIp.text = "当前IP地址：$ip:$port"


        btnSend.setOnClickListener {
            if (!MinaHelper.isConnected()) {
                toast("Socket未连接！")
                return@setOnClickListener
            }
            val text = etSend.text.toString().trim()
            if (text.isNullOrEmpty()) {
                toast("发送内容不能为空！")
                return@setOnClickListener
            }
            etSend.hideSoft(this)
            etSend.setText("")
            MinaHelper.sendMsg(text)
        }
        btnConnect.setOnClickListener {
            if (!MinaHelper.isConnected()) {
                MinaHelper.connect()
            }
        }

        btnDisConnect.setOnClickListener {
            MinaHelper.disConnect()
        }

        btnChange.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnClose.setOnClickListener { finish() }


        LiveEventBus.get(Const.MSG_CONNECT, Message::class.java).observe(this, Observer { t -> tvShow.append(t.toString() + "\n") })
    }


    override fun onDestroy() {
        super.onDestroy()
        MinaHelper.disConnect()
    }

}
