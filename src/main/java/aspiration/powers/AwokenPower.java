package aspiration.powers;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AwokenPower extends AbstractPower implements OnReceivePowerPower {
	public static final String POWER_ID = "aspiration:Awoken";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private static final int STR_GAIN = 1;
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
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    	if(target != owner) {
			int tmp = MathUtils.round(damageAmount * percentage_heal);
			if (tmp > 0) {
				AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, tmp));
			}
		}
    }

    @Override
    public boolean onReceivePower(AbstractPower p, AbstractCreature ptarget, AbstractCreature psource) {
        if(p.type == PowerType.DEBUFF && ptarget == owner && psource != this.owner) {
            flashWithoutSound();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, STR_GAIN), STR_GAIN));
        }
        return true;
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