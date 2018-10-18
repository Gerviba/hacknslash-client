package hu.gerviba.hackandslash.client.gui.menu;

import java.util.ArrayList;
import java.util.List;

import hu.gerviba.hackandslash.client.gui.CustomWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ServerListWindow extends CustomWindow {
    
    GridPane serverList;
    
    private final String[] promotedServers;
    private List<ServerEntryComponent> serverComponents = new ArrayList<>();
    
    public ServerListWindow(String[] servers) {
        super(false);
        promotedServers = servers == null ? new String[] {} : servers;
        init();
    }

    @Override
    protected void init() {
        BorderPane body = new BorderPane();
        body.setPrefSize(900, 600);

        body.setTop(getHeaderComponent());
        body.setCenter(getServerListComponent());
        body.setBottom(getFooterComponent());
        
        initScene(body);
    }

    private ScrollPane getServerListComponent() {
        ScrollPane serversPane = new ScrollPane();
        serversPane.setFitToWidth(true);
        serversPane.getStyleClass().add("server-list-scroll");
        serversPane.setPadding(Insets.EMPTY);
        serversPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        serversPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        serversPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        serverList = new GridPane();
        serverList.setAlignment(Pos.CENTER);
        serverList.setPadding(Insets.EMPTY);
        serverList.getStyleClass().add("server-list");
        serverList.setMinHeight(256);

        for (String ip : promotedServers) {
            ServerEntryComponent server = new ServerEntryComponent(ip);
            serverList.add(server.toPane(), 0, serverComponents.size());
            serverComponents.add(server);
        }
        
        serversPane.setContent(serverList);
        return serversPane;
    }

    private void initScene(Pane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(400);
        stage.setMinWidth(820);
    }
    
    private GridPane getHeaderComponent() {
        GridPane header = new GridPane();
        header.getStyleClass().add("header");
        Text title = new Text("SERVER LIST");
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("game-name");
        header.setAlignment(Pos.CENTER);
        header.setMinHeight(80);
        header.add(title, 0, 0);
        return header;
    }

    private GridPane getFooterComponent() {
        GridPane footer = new GridPane();
        footer.getStyleClass().add("footer");
        footer.setAlignment(Pos.CENTER);
        footer.setMinHeight(80);
        return footer;
    }

}
