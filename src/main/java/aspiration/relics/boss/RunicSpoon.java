package aspiration.relics.boss;

import aspiration.actions.unique.RSCopyAction;
import aspiration.patches.Fields.AbstractCardFields;
import aspiration.relics.abstracts.StatRelic;
import aspiration.util.RelicStatsHelper;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

public class RunicSpoon extends StatRelic {
    public static final String ID = "aspiration:RunicSpoon";

    private static final String STAT1 = "Cards duplicated: ";
    private static final int START_CHARGE = 0;
    private static final int CARDS_TO_TRIGGER = 3;
    private boolean wasTriggered = false;

    public RunicSpoon() {
        super(ID, "RunicSpoon.png", RelicTier.BOSS, LandingSound.CLINK);
        setCounter(START_CHARGE);

        if(CardBorderGlowManager.getGlowInfo("AspirationRunicSpoonGlow") == null) {
            CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
                @Override
                public boolean test(AbstractCard c) {
                    RunicSpoon r = (RunicSpoon) AbstractDungeon.player.getRelic(ID);
                    if (r != null) {
                        return r.checkTrigger();
                    }
                    return false;
                }

                @Override
                public Color getColor(AbstractCard c) {
                    return Color.PURPLE.cpy();
                }

                @Override
                public String glowID() {
                    return "AspirationRunicSpoonGlow";
                }
            });
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(AbstractCardFields.playerPlayed.get(c)) {
            flash();
            manipCharge(1);
            if (wasTriggered) {
                RelicStatsHelper.incrementStat(this, STAT1);
                atb(new RSCopyAction(c));
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

    @Override
    public void statsInit() {
        stats.put(STAT1, 0);
    }
}