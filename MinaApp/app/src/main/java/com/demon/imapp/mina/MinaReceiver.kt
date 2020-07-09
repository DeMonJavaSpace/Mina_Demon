package com.demon.imapp.mina

import android.util.Log
import com.demon.imapp.data.Const
import com.demon.imapp.data.Message
import com.demon.imapp.util.TAG
import com.jeremyliao.liveeventbus.LiveEventBus
import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IoSession

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
class MinaReceiver : IoHandlerAdapter() {

    override fun messageSent(session: IoSession?, message: Any?) {
        super.messageSent(session, message)
        Log.i(TAG, "messageSent $message")
        LiveEventBus.get(Const.MSG_CONNECT).post(Message(message))
    }

    override fun messageReceived(session: IoSession?, message: Any?) {
        super.messageReceived(session, message)
        Log.i(TAG, "messageReceived $message")
        LiveEventBus.get(Const.MSG_CONNECT).post(Message(message))
    }
}