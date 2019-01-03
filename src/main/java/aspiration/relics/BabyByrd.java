package aspiration.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;

import aspiration.relics.abstracts.AspirationRelic;

public class BabyByrd extends AspirationRelic {
	public static final String ID = "aspiration:BabyByrd";
	private static final int START_CHARGE = 0;
	private ArrayList<String> Dialogue = new ArrayList<String>();
	
	//Why are you here? You'll ruin the magic :(
	
    public BabyByrd() {
        super(ID, "BabyByrd.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
	public void onEquip() {
        startingCharges();
    }
    
    //TODO: Implement an integer that is saved and is checked here. Skills bird = defend, molten byrd = attack, power byrd = apply buffs or something? 
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
        	for(int i = 0;i<counter/10; i++) {
        		AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, 5, DamageType.THORNS), AttackEffect.SLASH_VERTICAL));
        	}
        	
        	java.util.Random rand = new java.util.Random();
        	AbstractDungeon.actionManager.addToBottom(new TalkAction(true, Dialogue.get(rand.nextInt(Dialogue.size() - 1)), 1.0F, 2.0F));
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
    
    public AbstractRelic makeCopy() {
        return new BabyByrd();
    }
}
