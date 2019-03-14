package aspiration.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CrackedCore;

import aspiration.relics.abstracts.AspirationRelic;

public class BursterCore extends AspirationRelic{
    public static final String ID = "aspiration:BursterCore";
	
	private static final int LIGHTNING_AMOUNT = 1;
	private static final int TURN_INTERVAL = 2;
	private static final int START_CHARGE = 0;
	
    public BursterCore() {
        super(ID, "BursterCore.png", RelicTier.BOSS, LandingSound.CLINK);
    }
    
    @Override
	public void onEquip() {
    	this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, DESCRIPTIONS[0] + LIGHTNING_AMOUNT + DESCRIPTIONS[1] + TURN_INTERVAL + DESCRIPTIONS[2]));
        this.initializeTips();
        startingCharges();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[3] + DESCRIPTIONS[0] + LIGHTNING_AMOUNT + DESCRIPTIONS[1] + TURN_INTERVAL + DESCRIPTIONS[2];
    }
    
    @Override
    public boolean canSpawn()
    {
    	return AbstractDungeon.player.hasRelic(CrackedCore.ID);
    }

    public AbstractRelic makeCopy() {
        return new BursterCore();
    }
    
    @Override
	public void obtain() {
		if (AbstractDungeon.player.hasRelic(CrackedCore.ID)) {
			 for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
	                if (AbstractDungeon.player.relics.get(i).relicId.equals(CrackedCore.ID)) {
	                    instantObtain(AbstractDungeon.player, i, true);
	                    break;
	                }
			 }
		}
		else {
			super.obtain();
		}
	}
    
    @Override
    public void onPlayerEndTurn()
    {
    	if(((GameActionManager.turn) % TURN_INTERVAL) == 0) {
    		channelToFull();
    		startingCharges();
    	} else {
    		flash();
    		manipCharge(1);
    	}
    }
    
    private void channelToFull()
    {
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	
    	if (AbstractDungeon.player.hasEmptyOrb())
		{
			int channel_amount = AbstractDungeon.player.maxOrbs - AbstractDungeon.player.filledOrbCount();
			
			AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			for(int i = 0;i<channel_amount;i++)
			{
				AbstractDungeon.player.channelOrb(new Lightning());
			}
		}
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
}
