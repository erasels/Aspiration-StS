package aspiration.relics.rare;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;

public class HatOfInfinitePower extends AspirationRelic {
    public static final String ID = "aspiration:HatOfInfinitePower";
    public static final int TURNS = 1;

    public HatOfInfinitePower() {
        super(ID, "HatOfInfinitePower.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    //Called in CardCostModifcationPatches
    public static boolean shouldMakePowersFree() {
        return GameActionManager.turn <= TURNS;
    }
}