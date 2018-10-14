package hu.gerviba.hacknslash.client.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleStatusResponse {

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String message;
    
}
