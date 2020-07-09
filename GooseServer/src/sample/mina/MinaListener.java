package sample.mina;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import sample.Controller;

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class MinaListener implements IoServiceListener {
    private MinaCallback callback;

    public MinaListener(MinaCallback callback) {
        this.callback = callback;
    }

    @Override
    public void serviceActivated(IoService ioService) throws Exception {
        System.out.println("[serviceActivated]");
        callback.serviceActivated();
    }

    @Override
    public void serviceIdle(IoService ioService, IdleStatus idleStatus) throws Exception {
        System.out.println("[serviceIdle]");
    }

    @Override
    public void serviceDeactivated(IoService ioService) throws Exception {
        System.out.println("[serviceDeactivated]");
        callback.serviceDeactivated();
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        System.out.println("[sessionCreated]");
    }

    @Override
    public void sessionDestroyed(IoSession ioSession) throws Exception {
        System.out.println("[sessionDestroyed]");
    }
}
