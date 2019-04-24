package aspiration.actions.unique;

import aspiration.relics.common.HiddenCompartment;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HiddenCompartmentAction extends AbstractGameAction {
    private AbstractPlayer p;
    private HiddenCompartment hC;

    public HiddenCompartmentAction(HiddenCompartment hC) {
        p = AbstractDungeon.player;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
        this.hC = hC;
    }

    public void update() {
        CardGroup tmp;
        if (this.duration == Settings.ACTION_DUR_MED) {
            tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.drawPile.group) {
                tmp.addToRandomSpot(c);
            }
            tmp.sortByRarity(false);

            if (tmp.size() < 2) {
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(tmp, 1, hC.DESCRIPTIONS[2], false);
            tickDuration();
            return;
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                p.drawPile.removeCard(c);
                hC.setStoredCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }
}

