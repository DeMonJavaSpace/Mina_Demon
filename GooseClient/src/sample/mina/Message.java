package sample.mina;

import java.text.SimpleDateFormat;

/**
 * @author DeMon
 * Created on 2020/5/15.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class Message {
    private int type = 0;
    private String name;
    private String content;
    private long time;
    private String to;


    public Message(String content) {
        this.type = 1;
        this.content = content;
        this.name = MacHelper.getInstance().getSelfMacAddress();
        this.time = System.currentTimeMillis();
    }

    public Message(String content, String to) {
        this.type = 0;
        this.content = content;
        this.name = MacHelper.getInstance().getSelfMacAddress();
        this.time = System.currentTimeMillis();
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String obj = "";
        if (!MacHelper.getInstance().getSelfMacAddress().equals(this.name)) {
            obj = "[接收]";
        }
        return "[" + sdf.format(this.time) + "]" + obj + "[" + this.name + "]：" + this.content;
    }
}
