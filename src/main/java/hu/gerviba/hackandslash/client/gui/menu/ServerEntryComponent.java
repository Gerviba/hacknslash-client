package hu.gerviba.hackandslash.client.gui.menu;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import org.springframework.web.client.RestTemplate;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hacknslash.client.auth.pojo.ServerListInfoResponse;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerEntryComponent implements CustomComponent {

    private static RestTemplate restTemplate = new RestTemplate();

    private final String ip;
    
    private GridPane server;
    private Text serverName;
    private Text serverMotd;
    private Text serverUsage;
    private volatile ServerListInfoResponse response;
    
    public ServerEntryComponent(String ip) {
        this.ip = HacknslashApplication.respolveHost(ip);
    }

    @Override
    public Pane toPane() {
        server = new GridPane();
        server.setMinHeight(128);
        server.setMaxHeight(128);
        server.setMinWidth(500);
        server.setMinWidth(800);
        server.setPrefWidth(780);
        //TODO: Add default background (color?)
        server.setBackground(new Background(new BackgroundImage(new Image("/testbg-3.png", 800, 128, true, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
        server.getStyleClass().add("server-bg");
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(85);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(15);
        col2.setHalignment(HPos.RIGHT);
        server.getColumnConstraints().addAll(col1, col2);
        server.setPadding(new Insets(10, 20, 20, 20));
        server.setVgap(10);
        
        serverName = new Text("");
        serverName.getStyleClass().add("server-name");
        server.add(serverName, 0, 0);
        serverMotd = new Text("");
        serverMotd.getStyleClass().add("server-motd");
        server.add(serverMotd, 0, 1, 2, 1);
        serverUsage = new Text("");
        serverUsage.getStyleClass().add("server-usage");
        serverUsage.setTextAlignment(TextAlignment.RIGHT);
        server.add(serverUsage, 1, 0);
        
        HacknslashApplication.ASYNC.execute(() -> {
            response = null;
            try {
                response = restTemplate.getForObject(ip + "/api/connect/info",
                        ServerListInfoResponse.class);
            } catch (Exception e) {
                serverName.setText("Failed to connect");
                serverMotd.setText(ip.replaceAll("http[s]?\\:\\/\\/", ""));
                serverUsage.setText("");
                log.error("Failed to connect to server " + ip);
            }
            
            if (response != null) {
                log.info(response.toString());
                serverName.setText(response.getName());
                serverMotd.setText(response.getMotd());
                serverUsage.setText(response.getUsers() + "/" + response.getMaxUsers());
                
                server.setBackground(
                        new Background(new BackgroundImage(
                                new Image(new ByteArrayInputStream(Base64.getDecoder()
                                        .decode(response.getIcon().getBytes())),
                                        800, 128, true, true),
                                BackgroundRepeat.NO_REPEAT, 
                                BackgroundRepeat.NO_REPEAT, 
                                BackgroundPosition.DEFAULT,
                                BackgroundSize.DEFAULT)));
                server.applyCss();
            }
        }); 

        server.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                new ConnectingWindow(ip, HacknslashApplication.getInstance().getUser().getSessionId())
                        .setThisToCurrentWindow();
            }
        });
        
        return server;
    }


}
