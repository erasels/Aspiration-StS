package aspiration.relics.boss;

import aspiration.Aspiration;
import aspiration.powers.AwokenPower;
import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.GlowyFireEyesEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

public class RitualDagger extends AspirationRelic implements CustomSavable<Boolean>, OnPlayerDeathRelic {
	public static final String ID = "aspiration:RitualDagger";
	
    private static final int HP_LOSS = 3;
	private static final float COOLDOWN_AMT = 0.2F;
    private float cooldown = COOLDOWN_AMT;
    
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
	public boolean onPlayerDeath(AbstractPlayer p, DamageInfo damageInfo) {
    	if(!usedUp && !p.hasRelic(MarkOfTheBloom.ID) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			img = ImageMaster.loadImage(Aspiration.assetPath("img/relics/" + "RitualDagger_used.png"));
			outlineImg = ImageMaster.loadImage(Aspiration.assetPath("img/relics/outline/" + "RitualDagger_used.png"));
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
			return false;
		}
    	return true;
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

    @Override
	public void update() {
		if(usedUp) {
			cooldown -= Gdx.graphics.getDeltaTime();
			if (cooldown < 0.0f)
			{
				cooldown = COOLDOWN_AMT;
				AbstractDungeon.effectsQueue.add(new GlowyFireEyesEffect(MathUtils.random(hb.x, hb.x + hb.width), MathUtils.random(hb.y, ((hb.y + hb.height)-15))));
			}
		}
    	super.update();
	}
    
    public boolean isUsedUp() {
    	return usedUp;
    }

    public AbstractRelic makeCopy() {
        return new RitualDagger();
    }

	@Override
	public void onLoad(Boolean arg0) {
		if(arg0 == null || !arg0) {
			usedUp = false;
		} else {
			usedUp = true;
			beginLongPulse();
			img = ImageMaster.loadImage(Aspiration.assetPath("img/relics/" + "RitualDagger_used.png"));
			outlineImg = ImageMaster.loadImage(Aspiration.assetPath("img/relics/outline/" + "RitualDagger_used.png"));
		}
	}

	@Override
	public Boolean onSave() {
		return usedUp;
	}
}