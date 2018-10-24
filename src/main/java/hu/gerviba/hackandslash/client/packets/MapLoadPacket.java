package hu.gerviba.hackandslash.client.packets;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapLoadPacket {
    
    @AllArgsConstructor
    public static enum LayerType {
        BACKGROUND("BG_FRAGMENT", "B"),
        FOREGROUND("FG_FRAGMENT", "F"),
        MIDDLE("MID_FRAGMENT", "M"),
        ;
        @Getter
        private final String command;
        @Getter
        private final String indicator;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MapLayerInfo {
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BackgroundPart {
            int x;
            int y;
            char character;
            String name;
            List<int[]> places;
        }
        
        List<BackgroundPart> parts;
        
    }

    private String name;
    private String displayName;
    private String texture;
    private double spawnX;
    private double spawnY;
    private MapLayerInfo foreground;
    private MapLayerInfo background;
    private MapLayerInfo middle;
    private String backgroundColor;
    
}
