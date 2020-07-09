package sample.mina;

import com.google.gson.Gson;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Collection;
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

    public static Map<String, Long> userMap = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {     // 从端口接受消息，会响应此方法来对消息进行处理
        super.messageReceived(session, message);
        String msg = message.toString();
        System.out.println("[服务器收到消息]#" + session.getId() + ":" + msg);
        Message mm = gson.fromJson(msg, Message.class);
        callback.messageReceived(mm);
        switch (mm.getType()) {
            case 0:
                Long id = userMap.get(mm.getTo());
                if (id != null) {
                    // 拿到所有的客户端Session
                    IoSession sessions = session.getService().getManagedSessions().get(id);
                    // to客户端发送数据
                    if (sessions != null && sessions.isConnected()) {
                        sessions.write(message);
                    }
                }
                break;
            case 1:
                userMap.put(mm.getName(), session.getId());
                break;
        }


    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception { // 向客服端发送消息后会调用此方法
        super.messageSent(session, message);
        //session.close(true);//加上这句话实现短连接的效果，向客户端成功发送数据后断开连接
        System.out.println("[服务器发送消息]#" + session.getId() + ":" + message.toString());
    }

    // 关闭与客户端的连接时会调用此方法
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        System.out.println("[服务器与客户端断开连接]#" + session.getId());
        for (String s : userMap.keySet()) {
            if (session.getId() == userMap.get(s)) {
                callback.sessionClosed("[" + s + "]断开连接！");
                userMap.remove(s);
            }
        }
    }

    // 服务器与客户端创建连接
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        System.out.println("[服务器与客户端创建连接]#" + session.getId());
    }

    // 服务器与客户端连接打开
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        System.out.println("[服务器与客户端连接打开]#" + session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        System.out.println("[服务器进入空闲状态]#" + session.getId() + ":" + status.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        System.out.println("[服务器发送异常]#" + session.getId());
        callback.exceptionCaught("服务器发送消息异常！");
    }
}