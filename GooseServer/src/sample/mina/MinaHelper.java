package sample.mina;

import com.google.gson.Gson;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author DeMon
 * Created on 2020/5/19.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class MinaHelper {
    private static class Helper {
        private static MinaHelper INSTANCE = new MinaHelper();
    }

    private Gson gson;

    private MinaHelper() {
        //构造函数
        gson = new Gson();
    }

    public static MinaHelper getInstance() {
        return Helper.INSTANCE;
    }

    private IoAcceptor acceptor;

    public void start(int port, MinaCallback callback) {
        if (acceptor != null && acceptor.isActive()) {
            return;
        }
        try {
            // 创建一个非阻塞的server端的Socket
            acceptor = new NioSocketAcceptor();
            // 设置过滤器（使用mina提供的文本换行符编解码器）
            DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
            filterChain.addLast("codec", new ProtocolCodecFilter(new MsgCodecFactory()));
            acceptor.addListener(new MinaListener(callback));
            // 为接收器设置管理服务
            acceptor.setHandler(new ServiceHandler(callback));
            // 设置读取数据的换从区大小
            acceptor.getSessionConfig().setReadBufferSize(2048);
            // 读写通道10秒内无操作进入空闲状态
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
            // 绑定端口
            acceptor.bind(new InetSocketAddress(port));
        } catch (Exception e) {
            e.printStackTrace();
            callback.bindException();
        }
    }

    public boolean isConnected() {
        return acceptor != null && acceptor.isActive();
    }

    public void stop() {
        if (acceptor != null && acceptor.isActive()) {
            acceptor.dispose();
            acceptor = null;
        }
    }


    public IoAcceptor getAcceptor() {
        return acceptor;
    }

    String gooseContent = "81 00 80 42 88 B8 00 33 00 90 00 00 00 00 61 81 85 80 08 67 6F 63 62 52 65 66 31 81 05 00 00 00 27 10 82 07 64 61 74 53 65 74 31 83 05 67 6F 49 44 31 " +
            "84 08 4E F2 85 E1 F7 CE D9 00 85 05 00 00 00 00 01 86 05 00 00 00 00 01 87 01 00 88 05 00 00 00 00 01 89 01 00 8A 05 00 00 00 00 09 AB 36 83 01 00 84 03 03 00 00 91" +
            " 08 00 00 00 00 00 00 00 00 83 01 00 84 03 03 00 00 91 08 00 00 00 00 00 00 00 00 83 01 00 84 03 03 00 00 91 08 00 00 00 00 00 00 00 00";

    public void sendGoose() {
        if (acceptor == null || acceptor.isDisposed()) {
            return;
        }
        Map<Long, IoSession> sessionMap = acceptor.getManagedSessions();
        for (String s : ServiceHandler.userMap.keySet()) {
            IoSession session = sessionMap.get(ServiceHandler.userMap.get(s));
            if (session != null && session.isConnected()) {
                String msg = s + " " + MacHelper.getInstance().getSelfMacAddress() + " " + gooseContent;
                session.write(gson.toJson(new Message(msg)));
            }
        }


    }

}
