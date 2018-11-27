package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store login request information
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
    
}
