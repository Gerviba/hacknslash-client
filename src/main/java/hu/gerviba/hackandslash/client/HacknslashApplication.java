package hu.gerviba.hackandslash.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hu.gerviba.hackandslash.client.connection.WebSocketConnectionThread;
import hu.gerviba.hackandslash.client.gui.menu.AuthenticateWindow;
import hu.gerviba.hacknslash.client.auth.pojo.UserRepresentation;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class HacknslashApplication extends Application {

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
    
    public HacknslashApplication() {
        instance = this;
    }
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
//        stage.initStyle(StageStyle.UTILITY);
        
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Regular.ttf").toExternalForm(), 16);
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Light.ttf").toExternalForm(), 16);
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Bold.ttf").toExternalForm(), 20);
        Font.loadFont(HacknslashApplication.class.getResource("/assets/fonts/RobotoMono-Bold.ttf").toExternalForm(), 26);

        new AuthenticateWindow().setThisToCurrentWindow();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        ASYNC.shutdown();
        ASYNC.awaitTermination(3, TimeUnit.SECONDS);
    }
    
    public static void main(String args[]) {
        launch(args);
    }
    
    public static String respolveHost(String ip) {
        return (!ip.startsWith("http://") && !ip.startsWith("https://") 
                ? "http://" : "") + ip;
    }

}
