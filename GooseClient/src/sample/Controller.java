package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.mina.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, MinaCallback {
    @FXML
    private ListView<Message> listView;
    @FXML
    private TextField tfIp;
    @FXML
    private TextField tfPort;
    @FXML
    private TextField tfMsg;
    @FXML
    private TextField tfMac;
    @FXML
    private TextField tfVirtualMac;
    @FXML
    private Label tvIp;
    @FXML
    private Label tvMac;
    ObservableList<Message> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tvIp.setText("本机IP地址：" + MacHelper.getInstance().getSelfIPAddress());
        tvMac.setText("本机Mac地址：" + MacHelper.getInstance().getSelfMacAddress());
        listView.setItems(items);
        listView.setCellFactory((ListView<Message> l) -> new ColorRectCell());
    }


    public void btnConnect(ActionEvent actionEvent) {
        String portStr = tfPort.getText();
        String ipStr = tfIp.getText();
        if (ipStr.length() == 0 || ipStr.length() > 15) {
            items.add(new Message("错误的IP地址"));
            return;
        }
        if (portStr.length() == 0 || portStr.length() > 5 || !Utils.isNumeric(portStr)) {
            items.add(new Message("错误的端口号"));
            return;
        }
        String vMacStr = tfVirtualMac.getText();
        if (vMacStr.length() != 0) {
            MacHelper.getInstance().setSelfMacAddress(vMacStr);
        } else {
            MacHelper.getInstance().getLocalMac();
        }
        MinaHelper.getInstance().start(ipStr, Integer.parseInt(portStr), this);

    }

    public void btnDiscount(ActionEvent actionEvent) {
        MinaHelper.getInstance().stop();
    }

    public void btnSend(ActionEvent actionEvent) {
        if (!MinaHelper.getInstance().isConnected()) {
            items.add(new Message("请先连接到服务器！"));
            return;
        }
        String msgStr = tfMsg.getText();
        String macStr = tfMac.getText();
        if (msgStr.length() == 0) {
            items.add(new Message("发送内容不能为空！"));
            return;
        }

        if (macStr.length() == 0) {
            MinaHelper.getInstance().sendMsg(msgStr);
        } else {
            MinaHelper.getInstance().sendMsg(new Message(msgStr, macStr));
        }
    }

    @Override
    public void bindException() {
        updateList(new Message("连接失败，请检查服务器是否启动，IP端口号是否正确！"));
    }

    @Override
    public void sessionClosed() {
        updateList(new Message("服务器断开连接！"));
    }

    @Override
    public void messageReceived(Message mm) {
        updateList(mm);
    }


    private void updateList(Message mm) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                items.add(mm);
            }
        });

    }


    static class ColorRectCell extends ListCell<Message> {
        @Override
        public void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && item != null) {
                Text text = new Text();
                text.setText(item.toString());
                setGraphic(text);
            } else {
                setGraphic(null);
            }
        }
    }
}
