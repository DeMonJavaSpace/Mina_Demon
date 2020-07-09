package com.demon.imapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demon.imapp.data.Const
import com.demon.imapp.util.SPUtil
import com.demon.imapp.util.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ip = SPUtil.getInstance().get(Const.IP, "") as String
        val port = SPUtil.getInstance().get(Const.PORT, "") as String
        val name = SPUtil.getInstance().get(Const.NAME, "") as String
        val friend = SPUtil.getInstance().get(Const.FRIEND, "") as String
        etIp.setText(ip)
        etPort.setText(port)
        etName.setText(name)
        etTo.setText(friend)

        btnStart.setOnClickListener {
            val ipStr = etIp.text.toString().trim()
            if (ipStr.isEmpty()) {
                toast("IP地址不能为空！")
                return@setOnClickListener
            }
            val portStr = etPort.text.toString().trim()
            if (portStr.isEmpty()) {
                toast("端口号不能为空！")
                return@setOnClickListener
            }
            val nameStr = etName.text.toString().trim()
            if (nameStr.isEmpty()) {
                toast("用户名不能为空！")
                return@setOnClickListener
            }
            val toStr = etTo.text.toString().trim()
            if (toStr.isEmpty()) {
                toast("聊天好友不能为空！")
                return@setOnClickListener
            }
            SPUtil.getInstance().put(Const.IP, ipStr)
            SPUtil.getInstance().put(Const.PORT, portStr)
            SPUtil.getInstance().put(Const.NAME, nameStr)
            SPUtil.getInstance().put(Const.FRIEND, toStr)

            startActivity(Intent(this, SocketActivity::class.java))
            finish()
        }
    }
}
