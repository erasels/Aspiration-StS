package aspiration.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class HummingbirdHeart extends AspirationRelic {
	public static final String ID = "aspiration:HummingbirdHeart";
	
    private static final int STARTTING_POWER = 3;

    public HummingbirdHeart() {
        super(ID, "HummingbirdHeart.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STARTTING_POWER + DESCRIPTIONS[1];
    }
    
    @Override
    public void atPreBattle()
    {
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		AbstractDungeon.actionManager.addToBottom(new SFXAction("HEART_SIMPLE"));
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STARTTING_POWER)));
    }
    
    @Override
    public void onPlayerEndTurn()
    {
    	if(GameActionManager.turn <= STARTTING_POWER) {
    		flash();
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, -1)));
    	}
    }

    public AbstractRelic makeCopy() {
        return new HummingbirdHeart();
    }
}