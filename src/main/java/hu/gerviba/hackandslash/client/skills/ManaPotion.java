package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;

public class ManaPotion extends Skill {

    public ManaPotion(double manaCost, double reloadTime) {
        super(manaCost, reloadTime);
    }

    @Override
    public void cast(PlayerModel pm, IngameWindow ingame) {
        use();
    }

}
