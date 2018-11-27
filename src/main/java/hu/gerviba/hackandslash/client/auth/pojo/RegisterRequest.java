package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store register request data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    
}
