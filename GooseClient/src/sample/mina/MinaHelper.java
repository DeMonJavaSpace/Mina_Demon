package sample.mina;

import com.google.gson.Gson;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

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

    private IoSession session;

    public void start(String ip, int port, MinaCallback callback) {
        if (session != null && session.isConnected()) {
            return;
        }
        try {
            // 创建一个非阻塞的server端的Socket
            NioSocketConnector connector = new NioSocketConnector();
            // 设置过滤器（使用mina提供的文本换行符编解码器）
            DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
            filterChain.addLast("codec", new ProtocolCodecFilter(new MsgCodecFactory()));
            // 为接收器设置管理服务
            connector.setHandler(new ServiceHandler(callback));
            ConnectFuture future = connector.connect(new InetSocketAddress(ip, port));
            future.awaitUninterruptibly();
            session = future.getSession();
        } catch (Exception e) {
            e.printStackTrace();
            callback.bindException();
        }
    }

    public void stop() {
        if (session != null && session.isConnected()) {
            session.close(true);
            session = null;
        }
    }

    public boolean isConnected(){
        return session != null && session.isConnected();
    }


    public void sendMsg(String msg) {
        sendMsg(new Message(msg));
    }


    public void sendMsg(Message msg) {
        if (session == null || !session.isConnected()) {
            return;
        }
        session.write(gson.toJson(msg));
    }

}
