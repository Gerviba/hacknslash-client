package hu.gerviba.hackandslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store server list response data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class ServerListInfoResponse {

    private String name;
    private String motd;
    private int users;
    private int maxUsers;
    private String icon;
    
}
