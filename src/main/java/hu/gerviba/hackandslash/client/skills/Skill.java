package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Skill {

    private long lastUsed;
    private final double manaCost;
    @Getter
    private final double reloadTime;
    
    public abstract void cast(PlayerModel pm, IngameWindow ingame);
    
    public boolean canCast() {
        return System.currentTimeMillis() > lastUsed + reloadTime;
    }
    
    protected final void use() {
        lastUsed = System.currentTimeMillis();
    }
    
}
