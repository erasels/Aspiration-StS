package aspiration.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BurningBlood;

import aspiration.relics.abstracts.AspirationRelic;

public class InfernalBlood extends AspirationRelic {
	public static final String ID = "aspiration:InfernalBlood";
	
	private static final int SKILL_DAMAGE = 1;
	private static final int ATTACK_HEAL = 2;
	private static final int POWER_HEAL = 1;
	
    public InfernalBlood() {
        super(ID, "InfernalBlood.png", RelicTier.BOSS, LandingSound.MAGICAL);
    }
    
    @Override
	public void onEquip() {
    	this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, DESCRIPTIONS[0] + SKILL_DAMAGE + DESCRIPTIONS[1] + ATTACK_HEAL + DESCRIPTIONS[2] + POWER_HEAL + DESCRIPTIONS[3]));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[4] + DESCRIPTIONS[0] + SKILL_DAMAGE + DESCRIPTIONS[1] + ATTACK_HEAL + DESCRIPTIONS[2] + POWER_HEAL + DESCRIPTIONS[3];
    }
    
    @Override
    public boolean canSpawn()
    {
    	return AbstractDungeon.player.hasRelic(BurningBlood.ID);
    }

    public AbstractRelic makeCopy() {
        return new InfernalBlood();
    }
    
    @Override
	public void obtain() {
		if (AbstractDungeon.player.hasRelic(BurningBlood.ID)) {
			 for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
	                if (AbstractDungeon.player.relics.get(i).relicId.equals(BurningBlood.ID)) {
	                    instantObtain(AbstractDungeon.player, i, true);
	                    break;
	                }
			 }
		}
		else {
			super.obtain();
		}
	}
    
	@Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
		flash();
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        if(c.type == CardType.ATTACK) {
        	AbstractDungeon.player.heal(ATTACK_HEAL);
        }
        if(c.type == CardType.SKILL) {
        	AbstractDungeon.player.damage(new DamageInfo(null, SKILL_DAMAGE, DamageInfo.DamageType.HP_LOSS));
        }
        if(c.type == CardType.POWER) {
        	AbstractDungeon.player.heal(POWER_HEAL);
        }
    }
}