package sample.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class MsgCodecFactory implements ProtocolCodecFactory {

    private byte[] delimiter = "<->".getBytes(StandardCharsets.UTF_8);

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return new ClientMessageEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return new ClientMessageDecoder();
    }

    class ClientMessageDecoder extends CumulativeProtocolDecoder {
        IoBuffer buff = IoBuffer.allocate(320).setAutoExpand(true);
        byte[] codingDelimiterBuff = new byte[delimiter.length];

        @Override
        protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
            boolean complete = false;
            while (ioBuffer.hasRemaining()) {
                byte b = ioBuffer.get();
                buff.put(b);
                for (int i = 1; i < codingDelimiterBuff.length; i++) {
                    codingDelimiterBuff[i - 1] = codingDelimiterBuff[i];
                }
                codingDelimiterBuff[codingDelimiterBuff.length - 1] = b;
                if (Arrays.equals(codingDelimiterBuff, delimiter)) {
                    complete = true;
                    break;
                }
            }
            if (complete) {
                buff.flip();
                byte[] bytes = new byte[buff.limit() - delimiter.length];
                buff.get(bytes);
                String msg = new String(bytes, StandardCharsets.UTF_8);
                buff.clear();
                out.write(msg);
            }

            return false;
        }
    }

    class ClientMessageEncoder extends ProtocolEncoderAdapter {

        @Override
        public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
            IoBuffer buff = IoBuffer.allocate(320).setAutoExpand(true);
            buff.put(message.toString().getBytes(StandardCharsets.UTF_8));
            buff.put(delimiter);
            buff.flip();
            out.write(buff);
        }
    }
}
