package hu.gerviba.hackandslash.client.gui;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public abstract class CustomWindow {

    @Getter
    private static CustomWindow currentWindow = null;
    
    @Getter
    protected Scene scene;   
    
    protected Stage stage;
    
    public CustomWindow() {
        stage = HacknslashApplication.getInstance().getStage();
        init();
    }
    
    public CustomWindow(boolean autoInit) {
        stage = HacknslashApplication.getInstance().getStage();
        if (autoInit)
            init();
    }

    public final void setThisToCurrentWindow() {
        if (currentWindow != null)
            currentWindow.onClose();
        currentWindow = this;
//        System.err.println("Replacing " + Thread.currentThread().getStackTrace()[2]);
        stage.setScene(scene);
        stage.show();
    }

    protected abstract void init();
    
    public void onClose() {
        
    }
    
}
