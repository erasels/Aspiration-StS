package aspiration.relics;

import aspiration.powers.BefuddledPower;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SneckoTail extends AspirationRelic {
	public static final String ID = "aspiration:SneckoTail";

    private static final int BSTACK = 1;

    public SneckoTail() {
        super(ID, "SneckoTail.png", RelicTier.RARE, LandingSound.FLAT);
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(BefuddledPower.NAME, BefuddledPower.getDesc()));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BSTACK + DESCRIPTIONS[1];
    }

    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if (this.isPoisonRelated(c.rawDescription)) {
            this.flash();
            if (c.target == AbstractCard.CardTarget.ENEMY) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(uac.target, AbstractDungeon.player, new BefuddledPower(uac.target, BSTACK), BSTACK));
            }
            else {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new BefuddledPower(m, BSTACK), BSTACK));
                    }
                }
            }
        }
    }

    private boolean isPoisonRelated(String desc) {
        return desc.toLowerCase().contains(PoisonPower.NAME.toLowerCase()) || desc.toLowerCase().contains(PoisonPower.POWER_ID.toLowerCase());
    }
    
    @Override
    public boolean canSpawn() {
    	return deckDescriptionSearch(PoisonPower.NAME, PoisonPower.POWER_ID);
    }

    public AbstractRelic makeCopy() {
        return new SneckoTail();
    }
}