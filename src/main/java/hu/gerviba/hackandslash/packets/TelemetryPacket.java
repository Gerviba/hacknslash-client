package hu.gerviba.hackandslash.packets;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelemetryPacket {

    @Data
    @NoArgsConstructor
    public static class PlayerModelStatus {
        private long entityId;
        private double x;
        private double y;
        private int direction;
        private boolean walking;
    }

    private List<PlayerModelStatus> players;
    
    private List<Long> entityRemove;
    
}
