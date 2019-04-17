package aspiration.relics.crossovers;

import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.abstracts.OnReducePower;
import blackrusemod.powers.KnivesPower;
import blackrusemod.powers.ProtectionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TrustyKnives extends AspirationRelic implements OnReducePower {
    public static final String ID = "aspiration:TrustyKnives";

    private static final int PROT_GAIN = 1;

    public TrustyKnives() {
        super(ID, "TrustyKnives.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PROT_GAIN + DESCRIPTIONS[1];
    }

    /*@Override
    public void atTurnStart() {
        if(!AbstractDungeon.player.hasPower(KnivesPower.POWER_ID)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KnivesPower(AbstractDungeon.player, KNIVES_AMT), KNIVES_AMT));
        }
    }*/

    @Override
    public int OnReducePower(AbstractPower powerInstance, int amount) {
        if(powerInstance.ID.equals(KnivesPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ProtectionPower(AbstractDungeon.player, amount), amount));
        }
        return amount;
    }

    public AbstractRelic makeCopy() {
        return new TrustyKnives();
    }
}