package com.demon.imapp.mina

import android.util.Log
import com.demon.imapp.util.TAG
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
class KeepAliveFactory : KeepAliveMessageFactory {

    override fun isRequest(ioSession: IoSession?, p: Any?): Boolean {
        //Log.i(TAG, "isRequest" + p.toString())
        return false
    }

    override fun getRequest(ioSession: IoSession?): Any? {
        return "[客户端心跳]"
    }

    override fun isResponse(ioSession: IoSession?, p: Any?): Boolean {
        Log.i(TAG, "isResponse" + p.toString())
        return true
    }


    override fun getResponse(ioSession: IoSession?, p: Any?): Any? {
        //Log.i(TAG, "getResponse" + p.toString())
        return null
    }
}