package aspiration.relics.uncommon;

import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

public class FrozenJewel extends AspirationRelic {
	public static final String ID = "aspiration:FrozenJewel";
	
    private static final int BLOCK_GAIN = 2;
    private int mon_counter = 0;
    private int atk_counter = 0;

    public FrozenJewel() {
        super(ID, "FrozenJewel.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLOCK_GAIN + DESCRIPTIONS[1];
    }
    
    @Override
    public void onPlayerEndTurn() {
    	for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
    		if(!mo.isDeadOrEscaped()) {
            	mon_counter++;
            
            	if ((mo.intent == AbstractMonster.Intent.ATTACK || mo.intent == AbstractMonster.Intent.ATTACK_BUFF || mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF || mo.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
            		flash();
                	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_GAIN));
                	atk_counter++;
            	}
    		}
    	}
    	if(atk_counter > 1 && atk_counter == mon_counter) {
			AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new VerticalAuraEffect(Color.LIGHT_GRAY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.0F));
    		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 1), 1));
    	}
    	atk_counter = 0;
    	mon_counter = 0;
    }

    public AbstractRelic makeCopy() {
        return new FrozenJewel();
    }
}