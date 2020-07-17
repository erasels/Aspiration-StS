package aspiration.relics.common;

import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ThunderStrikeAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Ectoplasm;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;

public class SupercapacitiveCoin extends AspirationRelic implements ClickableRelic {
	public static final String ID = "aspiration:SupercapacitiveCoin";
	
	private static final int START_CHARGE = 0;
	private static final int CHARGE_INCREASE = 1;
	private int lightning_damage = 3;
	private boolean duringTurn = true;
	
    public SupercapacitiveCoin() {
        super(ID, "SupercapacitiveCoin.png", RelicTier.SPECIAL, LandingSound.CLINK);
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(DESCRIPTIONS[9] + new Ectoplasm().name, DESCRIPTIONS[6]));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
    	if(AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Ectoplasm.ID)) {
    		String tmp = CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + DESCRIPTIONS[1] + CHARGE_INCREASE + DESCRIPTIONS[7];
    		return tmp;
    	} else {
    		return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + DESCRIPTIONS[1] + CHARGE_INCREASE + DESCRIPTIONS[2];
    	}
    }

	@Override
	public void onRightClick() {
		if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && counter > 0 && !AbstractDungeon.player.isDead && duringTurn) {
			AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			
			if(counter < 21) {
				AbstractDungeon.actionManager.addToBottom(new ThunderStrikeAction(AbstractDungeon.getMonsters().getRandomMonster(true), new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), lightning_damage, DamageType.THORNS), counter));
			} else {
				AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new MindblastEffect(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, AbstractDungeon.player.flipHorizontal), 0.1f));
	            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
	            	if(!m.isDeadOrEscaped()) {
						AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
						AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(m.drawX, m.drawY)));
						AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.FIRE)));
						AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(AbstractDungeon.player, counter * 3, DamageInfo.DamageType.THORNS)));
						AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(m, AbstractDungeon.player));
					}
	            }
			}
			startingCharges();
			stopPulse();
		}
	}
	
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
    		beginLongPulse();
        }
    }
	
	@Override
    public void onVictory() {
        if(counter > 0) {
        	stopPulse();
        }
    }

	@Override
	public void atTurnStart() {
		duringTurn = true;
	}

	@Override
	public void onPlayerEndTurn()
	{
		duringTurn = false;
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
    	boolean has_ecto = AbstractDungeon.player.hasRelic(Ectoplasm.ID);
		if(has_ecto) {
			this.tips.clear();
			this.tips.add(new PowerTip(name + "+", getUpdatedDescription()));
			this.initializeTips();
		}
		if(!has_ecto) {
			this.tips.clear();
			this.tips.add(new PowerTip(name, description));
			this.tips.add(new PowerTip(DESCRIPTIONS[9] + new Ectoplasm().name, DESCRIPTIONS[6]));
			this.initializeTips();
		}

        if (counter < 0) {
            counter = 0;
        }
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && counter == 0) {
			beginLongPulse();
		}
        setCounter(counter + amt);
    }

	
	public AbstractRelic makeCopy() {
        return new SupercapacitiveCoin();
    }
}