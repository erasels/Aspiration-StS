package aspiration.relics.boss;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

import aspiration.powers.AwokenPower;
import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;

public class RitualDagger extends AspirationRelic implements CustomSavable<Boolean>{
	public static final String ID = "aspiration:RitualDagger";
	
    private static final int HP_LOSS = 3;
    
    private boolean usedUp = false;

    public RitualDagger() {
        super(ID, "RitualDagger.png", RelicTier.BOSS, LandingSound.SOLID);
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(AwokenPower.NAME, AwokenPower.getDesc()));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_LOSS + DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }
    
    @Override
    public void onTrigger() {
    	usedUp = true;
    	beginLongPulse();
    	AbstractDungeon.actionManager.addToBottom(new SFXAction("HEART_BEAT"));
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	healPlayer();
    	AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new VerticalAuraEffect(Color.FIREBRICK, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.0F));
    	AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_JAW_WORM_BELLOW"));
    	AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_PIERCING_WAIL"));
    	AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));
    	AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new RoomTintEffect(Color.FIREBRICK, 0.3f, 999f, true), 0.0F));
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AwokenPower(AbstractDungeon.player), 1));
    }
    
    private void healPlayer() {
    	 AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
         AbstractDungeon.player.healthBarUpdatedEvent();
    }
    
    @Override
    public void atTurnStartPostDraw()
    {
    	flash();
		if (usedUp) {
			AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new VerticalAuraEffect(Color.FIREBRICK, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.0F));
			AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_JAW_WORM_BELLOW"));
		}
    	AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, HP_LOSS));
      
    }
    
    @Override
    public void atPreBattle() {
    	if(usedUp) {
    		AbstractDungeon.actionManager.addToTop(new VFXAction(AbstractDungeon.player, new RoomTintEffect(Color.FIREBRICK, 0.3f, 999f, true), 0.0F));
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AwokenPower(AbstractDungeon.player), 1));
    	}
    }
    
    public boolean isUsedUp() {
    	return usedUp;
    }

    public AbstractRelic makeCopy() {
        return new RitualDagger();
    }

	@Override
	public void onLoad(Boolean arg0) {
		if(arg0 == null) {
			usedUp = false;
		} else {
			usedUp = arg0;
		}
	}

	@Override
	public Boolean onSave() {
		return usedUp;
	}
}