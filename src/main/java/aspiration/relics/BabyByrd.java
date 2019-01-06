package aspiration.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;

import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;

public class BabyByrd extends AspirationRelic implements CustomSavable<Integer>{
	public static final String ID = "aspiration:BabyByrd";
	private static final int START_CHARGE = 0;
	private ArrayList<String> Dialogue = new ArrayList<String>();
	
	//1 = Skill, 2 = attack, 3 = power
	private int egg_type = 0;
	
    public BabyByrd() {
        super(ID, "BabyByrd.png", RelicTier.SPECIAL, LandingSound.FLAT);
        egg_type = 0;
    }
    
    public BabyByrd(int e_t) {
        super(ID, "BabyByrd.png", RelicTier.SPECIAL, LandingSound.FLAT);
        egg_type = e_t;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
	public void onEquip() {
        startingCharges();
    }
    
    @Override
    public void atBattleStart()
    {
    	if(Dialogue.isEmpty()) {
    		if (AbstractDungeon.player instanceof Ironclad) {
        		Dialogue.add(DESCRIPTIONS[1]);
        		Dialogue.add(DESCRIPTIONS[2]);
        		Dialogue.add(DESCRIPTIONS[3]);
        	} else if(AbstractDungeon.player instanceof TheSilent) {
        		Dialogue.add(DESCRIPTIONS[4]);
        		Dialogue.add(DESCRIPTIONS[5]);
        		Dialogue.add(DESCRIPTIONS[6]);
        	} else if (AbstractDungeon.player instanceof Defect) {
        		Dialogue.add(DESCRIPTIONS[7]);
        		Dialogue.add(DESCRIPTIONS[8]);
        		Dialogue.add(DESCRIPTIONS[9]);
        	} else {
        		for (String s : DESCRIPTIONS) {
        			if(!s.equals(DESCRIPTIONS[0])) {
        				Dialogue.add(s);
        			}
        		}
        	}
    	}
    	
    	if(counter > 35) {
    		flash();
        	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        	AbstractDungeon.actionManager.addToBottom(new SFXAction("BYRD_DEATH"));
        	
        	java.util.Random rand = new java.util.Random();
        	AbstractDungeon.actionManager.addToBottom(new TalkAction(true, Dialogue.get(rand.nextInt(Dialogue.size() - 1)), 1.0F, 2.0F));
        	//1 = Skill, 2 = attack, 3 = power
        	switch (egg_type) {
        		case 1:
        			for(int i = 0;i<counter/10; i++) {
        				AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, 3, DamageType.THORNS), AttackEffect.SLASH_DIAGONAL));
        				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 2));
        			}
        			break;
        		case 2:
        			for(int i = 0;i<counter/10; i++) {
        				AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, 6, DamageType.THORNS), AttackEffect.FIRE));
        			}
        			break;
        		case 3:
        			for(int i = 0;i<counter/20; i++) {
        				if (AbstractDungeon.player instanceof Ironclad) {
        					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        	        	} else if(AbstractDungeon.player instanceof TheSilent) {
        	        		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
        	        	} else if (AbstractDungeon.player instanceof Defect) {
        	        		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
        	        	} else {
        	        		if((i % 2) == 0) {
        	        			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        	        		} else {
        	        			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
        	        		}
        	        	}
        			}
        			break;
        		default:
        			for(int i = 0;i<counter/10; i++) {
        				AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, 5, DamageType.THORNS), AttackEffect.SLASH_VERTICAL));
        			}
        			break;
        	}
    	}
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room)
    {
    	flash();
        if (room instanceof RestRoom) {
            flash();
            manipCharge(2);
        }
    }
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
    	java.util.Random rand = new java.util.Random();
    	flash();
    	manipCharge(1);
    	AbstractDungeon.actionManager.addToBottom(new SFXAction("BYRD_DEATH"));
    	if(rand.nextInt(4) == 0) {
    		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        	AbstractDungeon.actionManager.addToBottom(new TalkAction(true, Dialogue.get(rand.nextInt(Dialogue.size() - 1)), 1.0F, 2.0F));
    	}
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
    
    @Override
    public Integer onSave() {
    	return egg_type;
    }
    
    @Override
    public void onLoad(Integer p)
    {
        if (p == null) {
            return;
        }
        
        egg_type = p;
    }
    
    public AbstractRelic makeCopy() {
        return new BabyByrd();
    }
}
