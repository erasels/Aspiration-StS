package aspiration.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class AwokenPower extends AbstractPower{
	public static final String POWER_ID = "aspiration:Awoken";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private static final int WEAK_STACK = 1;
    public static final float percentage_heal = 0.1f;

    public AwokenPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        loadRegion("corruption");
    }
    
    @Override
    public void onInitialApplication() {
    	if(!(AbstractDungeon.player.energy.energy >= AbstractDungeon.player.energy.energyMaster)) {
    		AbstractDungeon.player.energy.energy = AbstractDungeon.player.energy.energyMaster;
    	}
    }
    
    @Override
    public void atStartOfTurn()
    {
      for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
    	  if(!m.isDeadOrEscaped()) {
    		  AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, owner, new WeakPower(owner, WEAK_STACK, false), WEAK_STACK, true));
    	  }
      }
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    	if(target != owner) {
			int tmp = MathUtils.round(damageAmount * percentage_heal);
			if (tmp > 0) {
				AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, tmp));
			}
		}
    }
    
    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + (int)(percentage_heal*100) + DESCRIPTIONS[1];
    }
    
    public static String getDesc() {
    	return DESCRIPTIONS[0] + (int)(percentage_heal*100) + DESCRIPTIONS[1];
    }

}