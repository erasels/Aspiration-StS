package aspiration.relics.boss;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MechanicalEye extends AspirationRelic {
    public static final String ID = "aspiration:MechanicalEye";

    private static final int TURNS = 3;

    public MechanicalEye() {
        super(ID, "MechanicalEye.png", AbstractRelic.RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], TURNS);
    }

    @Override
    public void atTurnStartPostDraw() {
        if(GameActionManager.turn <= TURNS) {
            beginLongPulse();
            addToBot(new BetterDrawPileToHandAction(1, true));
        } else {
            stopPulse();
            grayscale = true;
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
        grayscale = false;
    }
}
