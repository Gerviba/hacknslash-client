package hu.gerviba.hackandslash.client.packets;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelemetryPacket {

    @Data
    @NoArgsConstructor
    public static class PlayerModelStatus {
        private String name;
        private long entityId;
        private double x;
        private double y;
        private int direction;
        private boolean walking;
        private float hp;
        
        private String base;
        private String weapon;
        private String helmet;
        private String armor;
        private String boots;
    }
    
    private List<PlayerModelStatus> players;
    
    private List<Long> entityRemove;
    
    // Mob list
    
}
