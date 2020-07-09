package com.demon.imapp.mina

import com.demon.imapp.data.Const
import com.demon.imapp.data.Message
import com.demon.imapp.util.SPUtil
import com.demon.imapp.util.toJson
import kotlinx.coroutines.*
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.keepalive.KeepAliveFilter
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler
import org.apache.mina.transport.socket.nio.NioSocketConnector
import java.net.InetSocketAddress

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
object MinaHelper {
    private val coroutineScopeIO = CoroutineScope((SupervisorJob() + Dispatchers.IO))

    private const val timeout = 10 * 1000L

    private var session: IoSession? = null

    /**
     * 连接job
     */
    private var connectJob: Job? = null

    fun connect() {
        val ip = SPUtil.getInstance().get(Const.IP, "") as String
        val port = SPUtil.getInstance().get(Const.PORT, "") as String
        val name = SPUtil.getInstance().get(Const.NAME, "") as String
        connectJob = coroutineScopeIO.launch {
            val connector = NioSocketConnector()
            //设置链接超时时间
            connector.connectTimeoutMillis = timeout
            //添加包编码过滤器 ，处理编码结束符
            connector.filterChain.addLast("codec", ProtocolCodecFilter(MsgCodecFactory()))
            connector.handler = MinaReceiver()
            connector.addListener(MinaListener())
         /*   //设置心跳包
            val heartFilter = KeepAliveFilter(KeepAliveFactory(), IdleStatus.READER_IDLE, KeepAliveRequestTimeoutHandler.CLOSE)
            heartFilter.isForwardEvent = true
            //发送一个心跳包
            heartFilter.requestInterval = 10
            //心跳包超时时间 10s
            heartFilter.requestTimeout = 10
            connector.filterChain.addLast("heartbeat", heartFilter)*/
            runCatching {
                val future = connector.connect(InetSocketAddress(ip, port.toInt()))
                // 等待连接创建完成
                future.awaitUninterruptibly()
                //获得session
                session = future.session
            }.onFailure {
                connector.dispose()
            }
        }
    }

    fun getSessionId(): Long? {
        return session?.id
    }

    fun disConnect() {
        connectJob?.cancel()
        session?.close(true)
    }


    fun isConnected(): Boolean {
        session?.run {
            return isConnected
        }
        return false
    }

    fun sendMsg(msg: String) {
        sendMsg(Message(msg))
    }

    fun sendMsg(msg: Message) {
        session?.write(msg.toJson())
    }


}