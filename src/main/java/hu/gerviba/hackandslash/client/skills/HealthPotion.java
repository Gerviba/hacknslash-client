package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;

public class HealthPotion extends Skill {

    public HealthPotion(double manaCost, double reloadTime) {
        super(manaCost, reloadTime);
    }

    @Override
    public void cast(PlayerModel pm, IngameWindow ingame) {
        use();
    }
    
}
