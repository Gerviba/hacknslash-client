package hu.gerviba.hackandslash.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.gerviba.hackandslash.client.auth.pojo.UserRepresentation;
import hu.gerviba.hackandslash.client.connection.WebSocketConnectionThread;
import hu.gerviba.hackandslash.client.gui.menu.AuthenticateWindow;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * The Main class of the application
 * @author Gergely Szabó
 */
public class HacknslashApplication extends Application {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    public static final ScheduledExecutorService ASYNC = Executors.newSingleThreadScheduledExecutor();
    
    @Getter
    private static HacknslashApplication instance;
    
    @Getter
    private Stage stage;
    
    @Getter
    @Setter
    private UserRepresentation user;

    @Getter
    @Setter
    private volatile WebSocketConnectionThread connection;
    
    /**
     * Constructor of the main class
     */
    public HacknslashApplication() {
        instance = this;
    }
    
    /**
     * JavaFX entry point
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Regular.ttf").toExternalForm(), 16);
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Light.ttf").toExternalForm(), 16);
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Bold.ttf").toExternalForm(), 20);
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Bold.ttf").toExternalForm(), 26);

        new AuthenticateWindow().setThisToCurrentWindow();
    }

    /**
     * Stop listener method.
     * Stops the {@link #ASYNC} scheduled executor thread pool.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        ASYNC.shutdown();
        ASYNC.awaitTermination(3, TimeUnit.SECONDS);
    }
    
    /**
     * The entrypoint of the application.
     * @param args Command line arguments
     */
    public static void main(String args[]) {
        launch(args);
    }
    
    /**
     * Host prefixer function
     * @param ip An IP address or host
     * @return Append a 'http://' prefix if no http(s):// presents.
     */
    public static String respolveHost(String ip) {
        return (!ip.startsWith("http://") && !ip.startsWith("https://") 
                ? "http://" : "") + ip;
    }

}
