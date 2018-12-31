package aspiration.relics;

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

import basemod.abstracts.CustomSavable;

import aspiration.relics.abstracts.AspirationRelic;

public class Contagion extends AspirationRelic implements CustomSavable<Integer>{
	public static final String ID = "aspiration:Contagion";
	
    private static final int ENERGY_GAIN = 1;
    private static final int POISON_THRESHOLD = 6;
    private static final int POISON_AMOUNT = 2;
    
    private static int poison_counter = 0;

    public Contagion() {
        super(ID, "Contagion.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POISON_THRESHOLD + DESCRIPTIONS[1] + POISON_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (p.ID.equals(PoisonPower.POWER_ID) && target != AbstractDungeon.player && !target.hasPower(ArtifactPower.POWER_ID) && source == AbstractDungeon.player) {
        	poison_counter += p.amount;

        	if(poison_counter >= POISON_THRESHOLD) {
        		flash();
        		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
        		poison_counter -= POISON_THRESHOLD;
        	}
        	
        	this.tips.clear();
            this.tips.add(new PowerTip(this.name, "Amount of #yPoison applied: #b" + poison_counter));
            this.initializeTips();
        }
    }
    
    @Override
    public void onPlayerEndTurn()
    {
    	for(AbstractCreature m :  AbstractDungeon.getCurrRoom().monsters.monsters)
    	{
    		if(m.hasPower(PoisonPower.POWER_ID)) {
    			return;
    		}
    	}
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PoisonPower(AbstractDungeon.player, AbstractDungeon.player, POISON_AMOUNT), POISON_AMOUNT));
    }
    
    @Override
    public Integer onSave() {
    	return poison_counter;
    }
    
    @Override
    public void onLoad(Integer p)
    {
        if (p == null) {
            return;
        }
        poison_counter = p;
        
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, "Amount of #yPoison applied: #b" + poison_counter));
        this.initializeTips();
    }
     
    @Override
    public boolean canSpawn() //Checked when? AbstractDungeon.returnRandomRelicKey
    {
    	return deckDescriptionSearch(PoisonPower.NAME, PoisonPower.POWER_ID);
    }

    public AbstractRelic makeCopy() {
        return new Contagion();
    }
}