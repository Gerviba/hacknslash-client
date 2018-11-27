package hu.gerviba.hackandslash.client.gui.menu;

import org.springframework.web.client.RestTemplate;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.auth.pojo.AuthStatus;
import hu.gerviba.hackandslash.client.auth.pojo.LoginRequest;
import hu.gerviba.hackandslash.client.auth.pojo.LoginResponse;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;

/**
 * Login component
 * @author Gergely Szabó
 */
@Slf4j
public class LoginComponent implements CustomComponent {

    GridPane errorBox;
    Text error;
    GridPane login;
    
    /**
     * Getter of the component root element
     */
    @Override
    public Pane toPane() {
        GridPane authBox = new GridPane();
        addErrorBox(authBox);

        login = new GridPane();
        GridPane.setFillWidth(login, true);
        login.getStyleClass().add("login");
        login.setPrefHeight(250);
        login.setPrefWidth(400);
        login.setAlignment(Pos.CENTER);
        login.setMinHeight(200);
        authBox.add(login, 0, 1);

        addTexts(login);

        TextField ipField = new TextField();
        ipField.setText(System.getProperty("login.ip", "127.0.0.1:8080"));
        ipField.setPrefWidth(200);
        login.add(ipField, 1, 0);

        TextField emailField = new TextField();
        emailField.setText(System.getProperty("login.email", ""));
        login.add(emailField, 1, 1);

        PasswordField passwordField = new PasswordField();
        passwordField.setText(System.getProperty("login.password", ""));
        login.add(passwordField, 1, 2);

        Button submit = new Button("LOGIN");
        GridPane.setFillWidth(submit, true);
        submit.setAlignment(Pos.CENTER_RIGHT);
        submit.setOnMouseClicked(event -> fireLoginEvent(
                ipField.getText(), 
                emailField.getText(), 
                passwordField.getText()));
        GridPane.setHalignment(submit, HPos.RIGHT);
        login.add(submit, 0, 3, 2, 1);

        ipField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                fireLoginEvent(
                        ipField.getText(), 
                        emailField.getText(), 
                        passwordField.getText());
            }
        });
        
        emailField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                fireLoginEvent(
                        ipField.getText(), 
                        emailField.getText(), 
                        passwordField.getText());
            }
        });
        
        passwordField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                fireLoginEvent(
                        ipField.getText(), 
                        emailField.getText(), 
                        passwordField.getText());
            }
        });

        return authBox;
    }

    /**
     * Perform the login action
     * @param ip IP address or host of the server
     * @param email Email of the user
     * @param password Password of the user
     */
    private void fireLoginEvent(String ip, String email, String password) {
        login.setDisable(true);
        RestTemplate restTemplate = new RestTemplate();
        LoginResponse response = null;
        try {
            response = restTemplate.postForObject(
                    HacknslashApplication.respolveHost(ip) + "/auth/login",
                    new LoginRequest(email, password),
                    LoginResponse.class);
        } catch (Exception e) {
            log.error("Error in authentication", e);
        }

        if (response == null) {
            error.setText("Failed to connect");
            errorBox.setVisible(true);
            login.setDisable(false);
            
        } else if (response.getStatus().getStatus().equals(AuthStatus.VALID.name())) {
            HacknslashApplication.getInstance().setUser(response.getUser());
            new ServerListWindow(response.getServers()).setThisToCurrentWindow();
            
        } else {
            error.setText(response.getStatus().getMessage().equals("BANNED") 
                    ? "You are not allowed to log in"
                    : "Invalid username of password");
            errorBox.setVisible(true);
            login.setDisable(false);
        }
    }

    /**
     * Append the red feedback box
     */
    private void addErrorBox(GridPane authBox) {
        authBox.setMaxHeight(300);
        authBox.setMaxWidth(400);
        authBox.setAlignment(Pos.CENTER);
        errorBox = new GridPane();
        errorBox.getStyleClass().add("error-box");
        errorBox.setVisible(false);
        error = new Text("Invalid username or password");
        error.getStyleClass().add("text");
        errorBox.add(error, 0, 0);
        authBox.add(errorBox, 0, 0);
        authBox.setVgap(20);
    }

    /**
     * Add the text fiends
     */
    private void addTexts(GridPane login) {
        Text ipLabel = new Text("Server IP");
        ipLabel.setTextAlignment(TextAlignment.LEFT);
        ipLabel.getStyleClass().add("text");
        login.add(ipLabel, 0, 0);

        Text emailLabel = new Text("E-mail");
        emailLabel.setTextAlignment(TextAlignment.LEFT);
        emailLabel.getStyleClass().add("text");
        login.add(emailLabel, 0, 1);

        Text passwordLabel = new Text("Password");
        passwordLabel.setTextAlignment(TextAlignment.LEFT);
        passwordLabel.getStyleClass().add("text");
        login.add(passwordLabel, 0, 2);
    }

}
