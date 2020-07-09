package com.demon.imapp.mina

import android.util.Log
import com.demon.imapp.data.Const
import com.demon.imapp.data.Message
import com.demon.imapp.data.MsgType
import com.demon.imapp.util.TAG
import com.jeremyliao.liveeventbus.LiveEventBus
import org.apache.mina.core.service.IoService
import org.apache.mina.core.service.IoServiceListener
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
class MinaListener : IoServiceListener {


    override fun sessionDestroyed(session: IoSession?) {
        Log.i(TAG,"sessionDestroyed")
        LiveEventBus.get(Const.MSG_CONNECT).post(Message("系统通知", "断开连接"))
    }

    override fun serviceActivated(service: IoService?) {
        Log.i(TAG,"serviceActivated")
    }

    override fun serviceDeactivated(service: IoService?) {
        Log.i(TAG,"serviceDeactivated")
    }

    override fun sessionCreated(session: IoSession?) {
        Log.i(TAG,"sessionCreated")
        LiveEventBus.get(Const.MSG_CONNECT).post(Message("系统通知", "连接成功"))
        //连接成功，上报name，id
        MinaHelper.sendMsg(Message(MsgType.MSG_REG.ordinal))
    }

    override fun serviceIdle(service: IoService?, status: IdleStatus?) {
        Log.i(TAG,"serviceIdle")
    }
}