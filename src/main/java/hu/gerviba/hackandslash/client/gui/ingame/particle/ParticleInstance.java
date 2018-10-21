package hu.gerviba.hackandslash.client.gui.ingame.particle;

import lombok.Data;

@Data
public class ParticleInstance implements Comparable<ParticleInstance> {

    private final long time;
    private final int x;
    private final int y;
    
    @Override
    public int compareTo(ParticleInstance pi) {
        return Integer.compare(y, pi.y);
    }
    
}
