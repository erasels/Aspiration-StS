package aspiration.relics.rare;

import aspiration.orbs.OrbUtilityMethods;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MysteriousAuxiliaryCore extends AspirationRelic{
public static final String ID = "aspiration:MysteriousAuxiliaryCore";
	
	private static final int ORB_AMOUNT = 1;
	private static final int TURN_INTERVAL = 2;
	private static final int START_CHARGE = 0;
	
    public MysteriousAuxiliaryCore() {
        super(ID, "MysteriousAuxiliaryCore.png", RelicTier.RARE, LandingSound.CLINK);
    }
    
    @Override
	public void onEquip() {
        startingCharges();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ORB_AMOUNT + DESCRIPTIONS[1];
    }
    
    @Override
    public void onPlayerEndTurn()
    {
    	if(((GameActionManager.turn) % TURN_INTERVAL) == 0) {
    	    flash();
    	    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	    for(int i = 0; i<ORB_AMOUNT;i++) {
                AbstractDungeon.actionManager.addToBottom(new ChannelAction(OrbUtilityMethods.getSelectiveRandomOrb(AbstractDungeon.relicRng)));
            }
    		startingCharges();
    	} else {
    		manipCharge(1);
    	}
    }

    @Override
    public void atPreBattle() {
        startingCharges();
    }

    @Override
    public void onVictory() {
        setCounter(-1);
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
    
    public AbstractRelic makeCopy() {
        return new MysteriousAuxiliaryCore();
    }
}
