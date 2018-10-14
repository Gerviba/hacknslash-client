package hu.gerviba.hacknslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServerListInfoResponse {

    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String motd;
    
    @Getter
    @Setter
    private int users;
    
    @Getter
    @Setter
    private int maxUsers;
    
    @Getter
    @Setter
    private String icon;
    
}
