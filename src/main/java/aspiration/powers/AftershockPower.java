package aspiration.powers;

import aspiration.Aspiration;
import aspiration.actions.SpawnTolerantDamageAllEnemiesAction;
import aspiration.powers.abstracts.AspirationPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AftershockPower extends AspirationPower implements CloneablePowerInterface {
	public static final String POWER_ID = "aspiration:Aftershock";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private float aftershockPercentageDamage = 0;
    private boolean justApplied = false;

    public AftershockPower(AbstractCreature owner, float damageEcho)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        aftershockPercentageDamage = damageEcho;
        updateDescription();
        region48 = Aspiration.powerAtlas.findRegion("48/aftershock");
        region128 = Aspiration.powerAtlas.findRegion("128/aftershock");
        isTurnBased = false;
    }
    
    @Override
    public int onAttacked(DamageInfo info, int dmgAmount) {
    	amount = 1;
    	justApplied = true;
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && dmgAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && info.owner != null) {
            this.flashWithoutSound();
            AbstractDungeon.actionManager.addToBottom(new SpawnTolerantDamageAllEnemiesAction(AbstractDungeon.player, Math.round(dmgAmount * aftershockPercentageDamage), true, false, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        }
    	
    	return dmgAmount;
    }
    
    @Override
    public void atEndOfRound() {
    	 if (this.amount == 0) {
    	      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
    	    } else {
    	    	if(!justApplied) {
    	    		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, ID, 1));
    	    	}
    	    }
    	 justApplied = false;
    }
    
    @Override
    public void onInitialApplication() {
    	amount = 1;
    	justApplied = true;
    }
    
    /*@Override
    public void atStartOfTurn() {
    	amount = 0;
    }*/
    
    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + Math.round(aftershockPercentageDamage*100) + DESCRIPTIONS[1];
    }
    
    public String getDesc() {
    	if(aftershockPercentageDamage != 0) {
    		return description;
    	} else {
    		return DESCRIPTIONS[0] + "X" + DESCRIPTIONS[1];
    	}
    	
    }

    @Override
    public AbstractPower makeCopy() {
        return new AftershockPower(owner, aftershockPercentageDamage);
    }
}
