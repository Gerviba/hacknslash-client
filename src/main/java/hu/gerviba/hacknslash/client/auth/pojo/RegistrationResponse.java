package hu.gerviba.hacknslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationResponse {

    @Getter
    @Setter
    private SimpleStatusResponse status;
    
    @Getter
    @Setter
    private UserRepresentation user;

}
