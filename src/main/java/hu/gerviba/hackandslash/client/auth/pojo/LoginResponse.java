package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store login response data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    
    private SimpleStatusResponse status;
    private UserRepresentation user;
    private String[] servers;
        
}
