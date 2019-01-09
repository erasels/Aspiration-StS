package aspiration.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AwokenPower extends AbstractPower{
	public static final String POWER_ID = "aspiration:Awoken";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public static final int ENERGY_GAIN = 1;

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
    	AbstractDungeon.player.energy.energy = AbstractDungeon.player.energy.energyMaster;
    }
    
    @Override
    public void atStartOfTurn()
    {
      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
    }
    
    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + ENERGY_GAIN + DESCRIPTIONS[1];
    }
    
    public static String getDesc() {
    	return DESCRIPTIONS[0] + ENERGY_GAIN + DESCRIPTIONS[1];
    }

}
