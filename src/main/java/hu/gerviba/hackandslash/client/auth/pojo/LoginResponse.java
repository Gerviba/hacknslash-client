package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse {
    
    @Getter
    @Setter
    private SimpleStatusResponse status;
    
    @Getter
    @Setter
    private UserRepresentation user;
    
    @Getter
    @Setter
    private String[] servers;
        
}
