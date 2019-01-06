package aspiration.relics;

import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class SneckoTail extends AspirationRelic {
	public static final String ID = "aspiration:SneckoTail";	

    public SneckoTail() {
        super(ID, "SneckoTail.png", RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    //REPLACED BY ApplyPowerActionRelicPostfix.class, THIS WAY HAD UNINTENDED CONSEQUENCES SUCH AS NOT BEING TRIGGERING SNECK SKULL
    /*@Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (p.ID.equals(PoisonPower.POWER_ID) && target != AbstractDungeon.player && !target.hasPower(ArtifactPower.POWER_ID) && (target.hasPower(WeakPower.POWER_ID) || target.hasPower(VulnerablePower.POWER_ID)) && source != null && source != target) {
        	flash();
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, null, new PoisonPower(target, null, p.amount), p.amount));
        }
    }*/
    
    @Override
    public boolean canSpawn() //Checked when? AbstractDungeon.returnRandomRelicKey
    {
    	return deckDescriptionSearch(PoisonPower.NAME, PoisonPower.POWER_ID);
    }

    public AbstractRelic makeCopy() {
        return new SneckoTail();
    }
}