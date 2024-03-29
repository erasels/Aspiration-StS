package aspiration.relics.common;

import aspiration.actions.unique.HiddenCompartmentAction;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class HiddenCompartment extends AspirationRelic {
    public static final String ID = "aspiration:HiddenCompartment";

    public AbstractCard storedCard = null;

    public HiddenCompartment() {
        super(ID, "HiddenCompartment.png", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToTop(new HiddenCompartmentAction(this));
    }

    @Override
    public void onShuffle() {
        if(!grayscale) {
            grayscale = true;
            if(storedCard != null) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(storedCard));
                setStoredCard(null);
            }
        }
    }

    @Override
    public void onVictory() {
        setStoredCard(null);
        grayscale = false;
    }

    public void setStoredCard(AbstractCard c) {
        storedCard = c;
        addendumDesc(c);
    }

    private void addendumDesc(AbstractCard card) {
        if(card != null) {
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description + DESCRIPTIONS[1] + FontHelper.colorString(card.name, "b")));
            this.initializeTips();
        } else {
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
    }
}