package sample.mina;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author DeMon
 * Created on 2020/5/19.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class MacHelper {
    private String otherAddress = "";
    private String selfIPAddress = "";
    private String selfMacAddress = "";

    private static class Helper {
        private static MacHelper INSTANCE = new MacHelper();
    }

    private MacHelper() {
        //构造函数
        getLocalMac();
    }

    public static MacHelper getInstance() {
        return Helper.INSTANCE;
    }

    public void getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            selfIPAddress = ia.getHostAddress();
            //获取网卡，获取地址
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(" ");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            selfMacAddress = sb.toString().toUpperCase();
            System.out.println("本机MAC地址:" + selfMacAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSelfMacAddress() {
        return selfMacAddress;
    }

    public void setSelfMacAddress(String selfMacAddress) {
        this.selfMacAddress = selfMacAddress;
    }

    public String getSelfIPAddress() {
        return selfIPAddress;
    }

    public void setSelfIPAddress(String selfIPAddress) {
        this.selfIPAddress = selfIPAddress;
    }

    public String getOtherAddress() {
        return otherAddress;
    }

    public void setOtherAddress(String otherAddress) {
        this.otherAddress = otherAddress;
    }
}
