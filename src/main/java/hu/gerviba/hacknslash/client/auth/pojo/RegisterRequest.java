package hu.gerviba.hacknslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;
    
    @Getter
    @Setter
    private String password;
    
}
