package aspiration.relics.crossovers;

import aspiration.relics.abstracts.AspirationRelic;
import blackrusemod.powers.KnivesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TrustyKnives extends AspirationRelic {
    public static final String ID = "aspiration:TrustyKnives";

    private static final int KNIVES_AMT = 2;

    public TrustyKnives() {
        super(ID, "TrustyKnives.png", RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + KNIVES_AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        if(!AbstractDungeon.player.hasPower(KnivesPower.POWER_ID)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KnivesPower(AbstractDungeon.player, KNIVES_AMT), KNIVES_AMT));
        }
    }

    public AbstractRelic makeCopy() {
        return new TrustyKnives();
    }
}