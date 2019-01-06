package aspiration.relics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.blue.GeneticAlgorithm;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.Aspiration;
import aspiration.actions.PoetsPen_weakAction;
import aspiration.relics.abstracts.AspirationRelic;

public class PoetsPen_weak extends AspirationRelic{
	public static final String ID = "aspiration:PoetsPen_weak";
	
	public static final Logger logger = LogManager.getLogger(Aspiration.class.getName());

    public PoetsPen_weak() {
        super(ID, "PoetsPen.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
    	return DESCRIPTIONS[0];
    }
    
    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
    	//WHY DID I DO THIS. THIS WILL HAVE CONSEQUENCES, I'M SURE OF IT.
    	if(c.misc == 66 && !(c.name.equals(RitualDagger.NAME) || c.name.equals(GeneticAlgorithm.NAME))) {
    		c.misc = 0; 
    		logger.info("If the triggered card permanently increases a value of itself and behaves wonky, please tell the Aspiration author.");
    	} else {
    		if(c.type == CardType.ATTACK) {
    			flash();
    			AbstractDungeon.actionManager.addToBottom(new PoetsPen_weakAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
    		}
		}
    }

    
    public AbstractRelic makeCopy() {
        return new PoetsPen_weak();
    }
}