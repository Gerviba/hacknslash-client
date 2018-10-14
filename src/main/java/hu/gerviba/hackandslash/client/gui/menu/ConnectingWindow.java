package hu.gerviba.hackandslash.client.gui.menu;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.connection.WebSocketConnectionThread;
import hu.gerviba.hackandslash.client.gui.CustomWindow;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectingWindow extends CustomWindow {

    private String ip;
    private String sessionId;
    
    private Text connectingText;
    
    public ConnectingWindow(String ip, String sessionId) {
        super(false);
        this.ip = ip;
        this.sessionId = sessionId;
        init();
    }

    @Override
    protected void init() {
        BorderPane body = new BorderPane();
        body.setPrefSize(900, 600);
        body.setCenter(getConnectionComponent());
        
        initScene(body);
        
        Platform.runLater(() -> {
            try {
                WebSocketConnectionThread connection = new WebSocketConnectionThread(ip, sessionId, 
                        () -> connectingText.setText("Connected"));
                HacknslashApplication.getInstance().setConnection(connection);
                connection.start();
                
            } catch (Exception e) {
                log.error("Error in connection", e);
            }
        }); 
    }

    private Pane getConnectionComponent() {
        GridPane comp = new GridPane();
        comp.getStyleClass().add("connecting");
        comp.setAlignment(Pos.CENTER);
        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.CENTER);
        comp.getColumnConstraints().add(col);
        comp.setVgap(20);
        
        connectingText = new Text("Connecting to server");
        connectingText.getStyleClass().add("text");
        comp.add(connectingText, 0, 0);
        
        Button cancel = new Button("CANCEL");
        comp.add(cancel, 0, 1);
        
        return comp;
    }

    private void initScene(BorderPane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(400);
        stage.setMinWidth(820);
    }

}
