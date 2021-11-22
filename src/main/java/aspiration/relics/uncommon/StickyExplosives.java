package aspiration.relics.uncommon;

import aspiration.powers.AftershockPower;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class StickyExplosives extends AspirationRelic{
public static final String ID = "aspiration:StickyExplosives";
	
	private boolean applied = false;
	private static final float percentagAftershock = 0.3f;
	
    public StickyExplosives() {
        super(ID, "StickyExplosives.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
        updateTip();
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!applied && GameActionManager.turn == 1 && info.owner == AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL && !target.isPlayer) {
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new AftershockPower(target, percentagAftershock), 1));
    		
    		applied = true;
    	}
    }
    
    @Override
    public void atPreBattle() {
    	flash();
    	applied = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    private void updateTip() {
    	initializeTips();
    	tips.clear();
    	tips.add(new PowerTip(name, description));
		tips.add(new PowerTip(AftershockPower.NAME, AftershockPower.DESCRIPTIONS[0] + Math.round(percentagAftershock*100) + AftershockPower.DESCRIPTIONS[1]));
    	initializeTips();
    }
}