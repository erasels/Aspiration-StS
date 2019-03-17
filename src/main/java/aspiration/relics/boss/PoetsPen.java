package aspiration.relics.boss;

import aspiration.patches.AbstractCardPoetsPendField;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import aspiration.actions.unique.PoetsPenAction;
import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;

public class PoetsPen extends AspirationRelic implements CustomSavable<Integer>, OnAfterUseCardRelic {
	public static final String ID = "aspiration:PoetsPen";
	
	private static final int START_CHARGE = 0;
	private static final int STARTING_DEBUFF_AMOUNT = 6;
	private static final int FLOOR_THRESHOLD = 7;
	private int debuffs_applied = STARTING_DEBUFF_AMOUNT;

    public PoetsPen() {
        super(ID, "PoetsPen.png", RelicTier.BOSS, LandingSound.FLAT);
        debuffs_applied = STARTING_DEBUFF_AMOUNT;
        updateTip();
    }

    @Override
    public String getUpdatedDescription() {
    	return DESCRIPTIONS[0] + STARTING_DEBUFF_AMOUNT +  DESCRIPTIONS[1];
    }
    
    @Override
    public void atPreBattle() {
    	if(debuffs_applied > 0) {
    		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FrailPower(AbstractDungeon.player, debuffs_applied, false)));
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WeakPower(AbstractDungeon.player, debuffs_applied, false)));
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VulnerablePower(AbstractDungeon.player, debuffs_applied, false)));
    	}
    }
    
    /*@Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
    	if(AbstractCardPoetsPendField.ppTriggered.get(c)) {
    		AbstractCardPoetsPendField.ppTriggered.set(c, false);
		} else {
    		if(c.type == CardType.ATTACK) {
    			flash();
    			//ppTriggered is set in the action.
				AbstractDungeon.actionManager.addToBottom(new PoetsPenAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false, false));
			}
		}
    }*/

	@Override
	public void onAfterUseCard(AbstractCard c, UseCardAction useCardAction) {
		if(AbstractCardPoetsPendField.ppTriggered.get(c)) {
			AbstractCardPoetsPendField.ppTriggered.set(c, false);
		} else {
			if(c.type == CardType.ATTACK) {
				flash();
				//ppTriggered is set in the action.
				AbstractDungeon.actionManager.addToBottom(new PoetsPenAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false, false));
			}
		}
	}

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
    	manipCharge(1);
    	if(counter == FLOOR_THRESHOLD && debuffs_applied > 0) {
    		flash();
    		debuffs_applied -= 1;
    		startingCharges();
    		updateTip();
    	}
    }
    
    @Override
	public void onEquip() {
    	debuffs_applied -= AbstractDungeon.bossCount;
    	if(debuffs_applied < 0) {
    		debuffs_applied = 0;
    	}
    	startingCharges();
    	updateTip();
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
        return new PoetsPen();
    }

    private void updateTip() {
    	this.initializeTips();
    	this.tips.clear();
    	this.tips.add(new PowerTip(name, DESCRIPTIONS[0] + debuffs_applied +  DESCRIPTIONS[1]));
    	this.tips.add(new PowerTip(DESCRIPTIONS[4], DESCRIPTIONS[2] + FLOOR_THRESHOLD +  DESCRIPTIONS[3])); //Experience
    	this.initializeTips();
    }
    
	@Override
	public void onLoad(Integer p) {
		if (p == null) {
            return;
        }
		
		debuffs_applied = p;
		updateTip();
	}

	@Override
	public Integer onSave() {
		return debuffs_applied;
	}
}