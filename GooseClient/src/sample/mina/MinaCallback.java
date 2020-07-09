package sample.mina;

/**
 * @author DeMon
 * Created on 2020/5/19.
 * E-mail 757454343@qq.com
 * Desc:
 */
public interface MinaCallback {

    void bindException(); //启动错误

    void sessionClosed(); //服务器关闭


    void messageReceived(Message mm); //收到消息

}
