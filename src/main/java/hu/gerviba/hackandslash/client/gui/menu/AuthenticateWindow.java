package hu.gerviba.hackandslash.client.gui.menu;

import hu.gerviba.hackandslash.client.gui.CustomWindow;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * The authentication window
 * @author Gergely Szabó
 */
public class AuthenticateWindow extends CustomWindow {

    LoginComponent login;
    
    /**
     * Initialize the GUI
     */
    @Override
    protected void init() {
        BorderPane body = new BorderPane();
        body.setPrefSize(900, 600);

        body.setTop(getHeaderComponent());
        login = new LoginComponent();
        body.setCenter(login.toPane());
        
        initScene(body);
    }

    /**
     * Creates error header component
     * @return
     */
    private Pane getHeaderComponent() {
        return new GridPane();
    }

    /**
     * Initialize the JavaFX {@link Scene}
     * @param body Body component
     */
    private void initScene(BorderPane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(400);
        stage.setMinWidth(820);
    }
    
}
