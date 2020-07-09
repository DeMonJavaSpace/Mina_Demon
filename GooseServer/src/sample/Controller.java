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
import sample.mina.MacHelper;
import sample.mina.Message;
import sample.mina.MinaCallback;
import sample.mina.MinaHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, MinaCallback {
    @FXML
    private ListView<Message> listView;
    @FXML
    private TextField tfPort;
    @FXML
    private Label tvIp;
    @FXML
    private Label tvMac;
    ObservableList<Message> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tvIp.setText("服务器IP地址：" + MacHelper.getInstance().getSelfIPAddress());
        tvMac.setText("服务器Mac地址：" + MacHelper.getInstance().getSelfMacAddress());
        listView.setItems(items);
        listView.setCellFactory((ListView<Message> l) -> new ColorRectCell());
    }

    public void startClick(ActionEvent actionEvent) {
        String portStr = tfPort.getText();
        if (portStr.length() == 0 || portStr.length() > 5 || !Utils.isNumeric(portStr)) {
            items.add(new Message("错误的端口号"));
        } else {
            MinaHelper.getInstance().start(Integer.parseInt(portStr), this);
        }
    }

    public void closeClick(ActionEvent actionEvent) {
        MinaHelper.getInstance().stop();
    }

    public void sendClick(ActionEvent actionEvent) {
        if (!MinaHelper.getInstance().isConnected()){
            updateList("服务器未启动！");
            return;
        }
        MinaHelper.getInstance().sendGoose();
        updateList("发送Goose报文到客户端！");
    }

    @Override
    public void bindException() {
        items.add(new Message("服务器启动错误，请检测端口号是否被占用！"));
    }

    @Override
    public void serviceActivated() {
        updateList("服务器启动！");
    }

    @Override
    public void serviceDeactivated() {
        updateList("服务器关闭！");
    }

    @Override
    public void messageReceived(Message msg) {
        updateList(msg);
    }

    @Override
    public void sessionClosed(String msg) {
        updateList(msg);
    }

    @Override
    public void exceptionCaught(String msg) {
        updateList(msg);
    }

    private void updateList(Message msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                items.add(msg);
            }
        });

    }

    private void updateList(String ss) {
        updateList(new Message(ss));
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
