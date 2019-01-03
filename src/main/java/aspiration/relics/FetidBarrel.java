package aspiration.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class FetidBarrel extends AspirationRelic {
	public static final String ID = "aspiration:FetidBarrel";
	
    private static final int POISON_STACK = 1;

    public FetidBarrel() {
        super(ID, "FetidBarrel.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POISON_STACK + DESCRIPTIONS[1];
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
          flash();
          AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
          for(AbstractCreature m : AbstractDungeon.getMonsters().monsters) {
        	  AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new PoisonPower(m, AbstractDungeon.player, POISON_STACK), POISON_STACK));
          }
        }
      }

    public AbstractRelic makeCopy() {
        return new FetidBarrel();
    }
}