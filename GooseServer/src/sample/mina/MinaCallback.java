package sample.mina;

/**
 * @author DeMon
 * Created on 2020/5/19.
 * E-mail 757454343@qq.com
 * Desc:
 */
public interface MinaCallback {

    void bindException(); //启动错误

    void serviceActivated(); //服务器启动

    void serviceDeactivated(); //服务器关闭

    void messageReceived(Message msg); //收到消息

    void sessionClosed(String msg);

    void exceptionCaught(String msg);
}
