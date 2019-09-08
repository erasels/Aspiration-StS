package aspiration.relics.boss;

import aspiration.patches.Fields.AbstractCardFields;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

public class RunicSpoon extends AspirationRelic {
    public static final String ID = "aspiration:RunicSpoon";

    private static final int START_CHARGE = 0;
    //TODO: Make relic not trigger when not played by player and reduce this value again
    private static final int CARDS_TO_TRIGGER = 3;
    private boolean wasTriggered = false;

    public RunicSpoon() {
        super(ID, "RunicSpoon.png", RelicTier.BOSS, LandingSound.CLINK);
        setCounter(START_CHARGE);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(AbstractCardFields.playerPlayed.get(c)) {
            flash();
            manipCharge(1);
            if (wasTriggered) {
                atb(new MakeTempCardInHandAction(c));
                wasTriggered = false;
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + CARDS_TO_TRIGGER + TopPanel.getOrdinalNaming(CARDS_TO_TRIGGER) + DESCRIPTIONS[1];
    }

    @Override
    public void atPreBattle() {
        setCounter(START_CHARGE);
        wasTriggered = false;
    }

    @Override
    public void onVictory() {
        setCounter(-1);
        stopPulse();
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);

        if (counter >= CARDS_TO_TRIGGER) {
            flash();
            wasTriggered = true;
            stopPulse();
            counter -= CARDS_TO_TRIGGER;
        } else if (counter == CARDS_TO_TRIGGER - 1) {
            beginLongPulse();
        }
    }

    public boolean checkTrigger() { return pulse; }

    public AbstractRelic makeCopy() {
        return new RunicSpoon();
    }
}