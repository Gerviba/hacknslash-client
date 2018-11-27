package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store HTTP request status data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class SimpleStatusResponse {

    private String status;
    private String message;
    
}
