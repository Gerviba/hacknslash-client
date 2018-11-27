package hu.gerviba.hackandslash.client.gui;

import javafx.scene.layout.Pane;

/**
 * A GUI component
 * @author Gergely Szabó
 */
public interface CustomComponent {

    /**
     * Convert to pane
     * @return The root pane of the component
     */
    public Pane toPane();
    
}
