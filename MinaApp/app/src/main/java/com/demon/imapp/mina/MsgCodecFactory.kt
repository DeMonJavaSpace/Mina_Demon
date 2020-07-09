package com.demon.imapp.mina

import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.*

class MsgCodecFactory : ProtocolCodecFactory {
    companion object {
        const val TAG = "MsgCodecFactory"
        /**
         * 断包粘包分隔符
         */
        val delimiter = "<->".toByteArray(Charsets.UTF_8)
    }

    private var encoder = ClientMessageEncoder()
    private var decoder = ClientMessageDecoder()

    override fun getEncoder(session: IoSession): ProtocolEncoder {
        return encoder
    }

    override fun getDecoder(session: IoSession): ProtocolDecoder {
        return decoder
    }

    internal class ClientMessageDecoder : CumulativeProtocolDecoder() {

        private val buff = IoBuffer.allocate(320).setAutoExpand(true)
        private val codingDelimiterBuff = ByteArray(delimiter.size)

        public override fun doDecode(iosession: IoSession, iobuffer: IoBuffer,
                                     out: ProtocolDecoderOutput): Boolean {
            var complete = false

            while (iobuffer.hasRemaining()) {
                val b = iobuffer.get()
                buff.put(b)

                /**
                 * 解决 粘包问题，与服务器协商用 [SocketIM.Value_Coding_Delimiter] 分割符
                 * 由于是字符串，所以要保存最后读取的12位字节作为对比用途
                 * 每次先做一个队列出入，就是把第一位丢掉，新读的byte补到最后位，
                 * 然后对比是否与分割符的byte[]一致
                 * 读取得到分割符后结束本次编码
                 */
                for (i in 1 until codingDelimiterBuff.size) {
                    codingDelimiterBuff[i - 1] = codingDelimiterBuff[i]
                }
                codingDelimiterBuff[codingDelimiterBuff.size - 1] = b


                if (codingDelimiterBuff.contentEquals(delimiter)) {
                    complete = true
                    break
                }
            }

            if (complete) {
                buff.flip()
                val bytes = ByteArray(buff.limit() - delimiter.size)
                buff.get(bytes)
                val message = String(bytes, charset("UTF-8"))
                buff.clear()
                out.write(message)
            }

            return complete
        }
    }

    class ClientMessageEncoder : ProtocolEncoderAdapter() {

        @Throws(Exception::class)
        override fun encode(iosession: IoSession, message: Any, out: ProtocolEncoderOutput) {
            val buff = IoBuffer.allocate(320).setAutoExpand(true)
            buff.put(message.toString().toByteArray(charset("UTF-8")))
            buff.put(delimiter)
            buff.flip()
            out.write(buff)
        }
    }
}