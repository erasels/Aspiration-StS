package aspiration.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AfterImagePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class HummingbirdHeart extends AspirationRelic {
	public static final String ID = "aspiration:HummingbirdHeart";
	
    private static final int STARTING_BLOCK_PER_CARD = 3;

    public HummingbirdHeart() {
        super(ID, "HummingbirdHeart.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STARTING_BLOCK_PER_CARD + DESCRIPTIONS[1];
    }
    
    @Override
    public void atPreBattle()
    {
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		AbstractDungeon.actionManager.addToBottom(new SFXAction("HEART_SIMPLE"));
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AfterImagePower(AbstractDungeon.player, STARTING_BLOCK_PER_CARD)));
    }
    
    @Override
    public void onPlayerEndTurn()
    {
    	if(GameActionManager.turn <= STARTING_BLOCK_PER_CARD && AbstractDungeon.player.getPower(AfterImagePower.POWER_ID) != null) {
    		flash();
    		//AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AfterImagePower(AbstractDungeon.player, -1)));
    		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(AfterImagePower.POWER_ID), 1));
    		AbstractDungeon.player.getPower(AfterImagePower.POWER_ID).updateDescription();
    		
    		if(AbstractDungeon.player.getPower(AfterImagePower.POWER_ID).amount <= 0) {
    			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(AfterImagePower.POWER_ID)));
    		}
    	}
    }

    public AbstractRelic makeCopy() {
        return new HummingbirdHeart();
    }
}