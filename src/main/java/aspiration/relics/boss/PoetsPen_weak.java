package aspiration.relics.boss;

import aspiration.Aspiration;
import aspiration.actions.unique.PoetsPenAction;
import aspiration.patches.Fields.AbstractCardFields;
import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoetsPen_weak extends AspirationRelic implements OnAfterUseCardRelic {
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
    public void onAfterUseCard(AbstractCard c, UseCardAction useCardAction) {
        if(AbstractCardFields.ppTriggered.get(c)) {
            AbstractCardFields.ppTriggered.set(c, false);
        } else {
            if(c.type == CardType.ATTACK) {
                flash();
                //ppTriggered is set in the action.
                AbstractDungeon.actionManager.addToBottom(new PoetsPenAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false, true));
            }
        }
    }
}