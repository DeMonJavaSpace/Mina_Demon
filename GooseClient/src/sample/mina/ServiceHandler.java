package sample.mina;

import com.google.gson.Gson;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;


/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class ServiceHandler extends IoHandlerAdapter {
    private MinaCallback callback;

    public ServiceHandler(MinaCallback callback) {
        this.callback = callback;
    }

    private Gson gson = new Gson();

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {     // 从端口接受消息，会响应此方法来对消息进行处理
        super.messageReceived(session, message);
        String msg = message.toString();
        System.out.println("[收到消息]#" + session.getId() + ":" + msg);
        Message mm = gson.fromJson(msg, Message.class);
        callback.messageReceived(mm);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception { // 向客服端发送消息后会调用此方法
        super.messageSent(session, message);
        //session.close(true);//加上这句话实现短连接的效果，向客户端成功发送数据后断开连接
        System.out.println("[发送消息]#" + session.getId() + ":" + message.toString());
        String msg = message.toString();
        Message mm = gson.fromJson(msg, Message.class);
        callback.messageReceived(mm);
    }

    // 关闭与客户端的连接时会调用此方法
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        System.out.println("[客户端断开连接]#" + session.getId());
        callback.sessionClosed();
    }

    // 服务器与客户端创建连接
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        System.out.println("[客户端创建连接]#" + session.getId());
    }

    // 服务器与客户端连接打开
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        System.out.println("[客户端连接打开]#" + session.getId());
        session.write(gson.toJson(new Message("连接到服务器！")));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        System.out.println("[发送异常]#" + session.getId());
    }
}