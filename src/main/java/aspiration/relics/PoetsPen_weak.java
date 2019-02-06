package aspiration.relics;

import aspiration.actions.PoetsPenAction;
import aspiration.patches.AbstractCardPoetsPendField;
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
        if(AbstractCardPoetsPendField.ppTriggered.get(c)) {
            AbstractCardPoetsPendField.ppTriggered.set(c, false);
        } else {
            if(c.type == CardType.ATTACK) {
                flash();
                //ppTriggered is set in the action.
                AbstractDungeon.actionManager.addToBottom(new PoetsPenAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false, true));
            }
        }
    }

    
    public AbstractRelic makeCopy() {
        return new PoetsPen_weak();
    }
}