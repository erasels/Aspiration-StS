package aspiration.relics;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ThunderStrikeAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Ectoplasm;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;

import aspiration.relics.abstracts.AspirationRelic;

public class SupercapacitiveCoin extends AspirationRelic implements ClickableRelic {
	public static final String ID = "aspiration:SupercapacitiveCoin";
	
	private static final int START_CHARGE = 0;
	private static final int CHARGE_INCREASE = 1;
	private int lightning_damage = 3;
	private boolean used = false;
	
    public SupercapacitiveCoin() {
        super(ID, "SupercapacitiveCoin.png", RelicTier.COMMON, LandingSound.CLINK);
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip("Thunder", "Deals #b3 + #b1 * ( #yCharges / #b5 ) damage."));
        this.tips.add(new PowerTip("Synergy: Ectoplasm", "Gain #b1 #yCharge per floor traveled instead, if you have #gEctoplasm."));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
    	if(AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Ectoplasm.ID)) {
    		String tmp = DESCRIPTIONS[3] + DESCRIPTIONS[0] + DESCRIPTIONS[1] + CHARGE_INCREASE + " #yCharge everytime you travel up a floor. NL " + FontHelper.colorString(DESCRIPTIONS[4], "y");
    		this.tips.clear();
            this.tips.add(new PowerTip(name + "+", tmp));
            this.tips.add(new PowerTip("Thunder", "Deals #b3 + #b1 * ( #yCharges / #b10 ) damage."));
            this.initializeTips();
    		return tmp;
    	} else {
    		return DESCRIPTIONS[3] + DESCRIPTIONS[0] + DESCRIPTIONS[1] + CHARGE_INCREASE + DESCRIPTIONS[2] + FontHelper.colorString(DESCRIPTIONS[4], "y");
    	}
    }

	@Override
	public void onRightClick() {
		if(!used && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && counter > 0 && !AbstractDungeon.player.isDead && !AbstractDungeon.player.endTurnQueued && !AbstractDungeon.player.isEndingTurn && !AbstractDungeon.actionManager.turnHasEnded) {
			AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			
			if(counter < 21) {
				lightning_damage += Math.round(counter/5);
				AbstractDungeon.actionManager.addToBottom(new ThunderStrikeAction(AbstractDungeon.getMonsters().getRandomMonster(true), new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), lightning_damage, DamageType.THORNS), counter));
			} else {
				AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new MindblastEffect(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, AbstractDungeon.player.flipHorizontal), 0.1f));
	            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
	            	m.damageFlash = true;
	                m.damageFlashFrames = 4;
	            	AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
	            	AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(m.drawX, m.drawY)));
	            	AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.FIRE)));
	                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(AbstractDungeon.player, counter * 2, DamageInfo.DamageType.NORMAL)));
	                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new StunMonsterPower(m)));
	            }
			}
			startingCharges();
			pulse = false;
			used = true;
		}
	}
	
	//Ectoplasm negates this mechanic, postfix Patch AbstractPlay.GainGold
	@Override
	public void onGainGold() {
		flash();
		manipCharge(CHARGE_INCREASE);
	}
	
	@Override
    public void onEnterRoom(AbstractRoom room)
    {
		if(AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Ectoplasm.ID)) {
			flash();
    		manipCharge(CHARGE_INCREASE);
		}
    }
	
	@Override
    public void atPreBattle() {
        if(counter > 0) {
        	flash();
    		pulse = true;
        }
    }
	
	@Override
    public void onVictory() {
        if(counter > 0) {
        	pulse = false;
        }
        used = false;
    }
	
	@Override
	public void onEquip() {
    	startingCharges();
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
        return new SupercapacitiveCoin();
    }
}