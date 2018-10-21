package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRepresentation {

    @Getter
    @Setter
    private String uuid;
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String sessionId;
    
}