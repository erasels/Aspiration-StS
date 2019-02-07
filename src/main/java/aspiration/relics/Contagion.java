package aspiration.relics;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class Contagion extends AspirationRelic {
	public static final String ID = "aspiration:Contagion";
	
	private static final int START_CHARGE = 0;
	
    private static final int ENERGY_GAIN = 1;
    private static final int MAX_GAIN_PER_ROUND = 2;
    private static final int POISON_THRESHOLD = 8;
    private static final int POISON_AMOUNT = 2;
    
    private int energy_counter =  0;

    public Contagion() {
        super(ID, "Contagion.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POISON_THRESHOLD + DESCRIPTIONS[1] + MAX_GAIN_PER_ROUND + DESCRIPTIONS[2] + POISON_AMOUNT + DESCRIPTIONS[3];
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (p.ID.equals(PoisonPower.POWER_ID) && target != AbstractDungeon.player && !target.hasPower(ArtifactPower.POWER_ID) && source == AbstractDungeon.player) {
        	manipCharge(p.amount);
        	
        	if(counter >= POISON_THRESHOLD) {
        		flash();
        		manipCharge(-POISON_THRESHOLD);
        		
        		if(energy_counter < 2) {
        		    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
        		    energy_counter++;
        		
        		    this.tips.clear();
                    this.tips.add(new PowerTip(this.name, getUpdatedDescription() + DESCRIPTIONS[5] + energy_counter));
                    this.initializeTips();
        		} else {
        			AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DESCRIPTIONS[4], 1.0F, 2.0F));
        		}
        	}
        }
    }
    
    @Override
    public void onPlayerEndTurn()
    {
    	energy_counter = 0;
    	
    	this.tips.clear();
        this.tips.add(new PowerTip(this.name, getUpdatedDescription() + DESCRIPTIONS[5] + energy_counter));
        this.initializeTips();
        
    	for(AbstractCreature m :  AbstractDungeon.getCurrRoom().monsters.monsters)
    	{
    		if(!m.isDeadOrEscaped() && m.hasPower(PoisonPower.POWER_ID)) {
    			return;
    		}
    	}
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PoisonPower(AbstractDungeon.player, AbstractDungeon.player, POISON_AMOUNT), POISON_AMOUNT));
    }
    
    @Override
    public void onEquip()
    {
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
    }
     
    @Override
    public boolean canSpawn()
    {
    	return deckDescriptionSearch(PoisonPower.NAME, PoisonPower.POWER_ID);
    }

    public AbstractRelic makeCopy() {
        return new Contagion();
    }
}