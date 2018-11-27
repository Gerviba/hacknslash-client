package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store registration response data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class RegistrationResponse {

    private SimpleStatusResponse status;
    private UserRepresentation user;

}
