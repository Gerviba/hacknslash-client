package hu.gerviba.hackandslash.client.packets;

import java.util.List;

import lombok.Data;

/**
 * Players and mobs positions (telemetry packet)
 * @author Gergely Szabó
 */
@Data
public class TelemetryPacket {

    /**
     * Player status
     * @author Gergely Szabó
     */
    @Data
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
    
    /**
     * Mob status
     * @author Gergely Szabó
     */
    @Data
    public static class MobStatus {
        private long entityId;
        private String name;
        private String texture;
        private double targetX;
        private double targetY;
        private float hp;
    }

    private List<PlayerModelStatus> players;
    private List<Long> entityRemove;
    private List<MobStatus> mobs;

}
