package aspiration.relics;

import aspiration.powers.SappedPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class InnerPendulum extends AspirationRelic {
    public static final String ID = "aspiration:InnerPendulum";

    private static final int START_CHARGE = 0;
    private static final int TURNS_TRIGGER = 3;
    private static final int POWER_AMT = 1;

    private boolean debuff_turn = false;

    //TODO: Make this actually a good relic somebody would want to use and finish the picture.

    public InnerPendulum() {
        super(ID, "InnerPendulum.png", RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        manipCharge(1);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        startingCharges();
    }

    private void startingCharges()
    {
        setCounter(START_CHARGE);
    }

    private void manipCharge(int amt) {
        if (counter < 0) {
            counter = 0;
        }
        setCounter(counter + amt);

        if (counter >= TURNS_TRIGGER) {
            flash();
            if(debuff_turn) {
                SappedPower sp = new SappedPower(AbstractDungeon.player, POWER_AMT);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, sp, POWER_AMT));
            } else {
                ArtifactPower ap = new ArtifactPower(AbstractDungeon.player, POWER_AMT);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, ap, POWER_AMT));
            }

            debuff_turn = !debuff_turn;
            counter -= TURNS_TRIGGER;
        }
    }

    public AbstractRelic makeCopy() {
        return new InnerPendulum();
    }
}