package aspiration.relics.boss;

import aspiration.Aspiration;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BurningBlood;

//import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;

public class InfernalBlood extends AspirationRelic {
	public static final String ID = "aspiration:InfernalBlood";

	private static final int SKILL_DAMAGE = 1;
	private static final int ATTACK_HEAL = 2;
	
    public InfernalBlood() {
        super(ID, "InfernalBlood.png", RelicTier.BOSS, LandingSound.MAGICAL);
    }
    
    @Override
	public void onEquip() {
    	this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, DESCRIPTIONS[0] + SKILL_DAMAGE + DESCRIPTIONS[1] + ATTACK_HEAL + DESCRIPTIONS[2]));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[3] + DESCRIPTIONS[0] + SKILL_DAMAGE + DESCRIPTIONS[1] + ATTACK_HEAL + DESCRIPTIONS[2];
    }
    
    @Override
    public boolean canSpawn()
    {
    	return AbstractDungeon.player.hasRelic(BurningBlood.ID);
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
		if(c.type == CardType.SKILL || c.type == CardType.ATTACK)
		{
			flash();
			//AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			//CardCrawlGame.sound.play("ASP-BLOODPUMP");
			java.util.Random r = new java.util.Random();
			if(r.nextInt(6) > 4) {
				AbstractDungeon.actionManager.addToBottom(new SFXAction("HEART_SIMPLE"));
			}
			
			if(c.type == CardType.ATTACK) {
        		//AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, ATTACK_HEAL));
				try {
					AbstractDungeon.player.heal(ATTACK_HEAL, true);
				} catch (Exception e) {
					Aspiration.logger.info(e);
				}
        	}
        	if(c.type == CardType.SKILL) {
        		AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, SKILL_DAMAGE, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE));
        	}
		}
    }
}