package hu.gerviba.hackandslash.client.gui;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * Abstract window class
 * @author Gergely Szabó
 */
public abstract class CustomWindow {

    @Getter
    private static CustomWindow currentWindow = null;
    
    @Getter
    protected Scene scene;   
    
    protected Stage stage;
    
    /**
     * This constructor will automatically executes the {@link #init()} method.
     */
    public CustomWindow() {
        stage = HacknslashApplication.getInstance().getStage();
        init();
    }
    
    /**
     * Constructor
     * @param autoInit Executes the {@link #init()} method at the end of the constructor
     */
    public CustomWindow(boolean autoInit) {
        stage = HacknslashApplication.getInstance().getStage();
        if (autoInit)
            init();
    }

    /**
     * Changes the current scene in the active stage
     */
    public final void setThisToCurrentWindow() {
        if (currentWindow != null)
            currentWindow.onClose();
        currentWindow = this;
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initialize the window. Must be overridden,
     */
    protected abstract void init();
    
    /**
     * Close listener. Can be overridden.
     */
    public void onClose() {}
    
}
