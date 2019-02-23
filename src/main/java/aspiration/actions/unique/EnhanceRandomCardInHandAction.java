package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static runesmith.actions.EnhanceCard.canEnhance;
import static runesmith.actions.EnhanceCard.enhance;

public class EnhanceRandomCardInHandAction extends AbstractGameAction
{
    private AbstractPlayer p;
    private int enhanceAmt;

    public EnhanceRandomCardInHandAction(int amount)
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.enhanceAmt = amount;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (this.p.hand.group.size() <= 0) {
                this.isDone = true;
                return;
            }

            CardGroup enhanceable = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard c : this.p.hand.group) {
                if (canEnhance(c)) {
                    enhanceable.addToTop(c);
                }
            }

            if (enhanceable.size() > 0) {
                enhanceable.shuffle();
                enhance(enhanceable.group.get(0), enhanceAmt);
                enhanceable.group.get(0).superFlash();
                //((AbstractCard)enhanceable.group.get(0)).applyPowers();
            }

            this.isDone = true;
            return;
        }

        tickDuration();
    }
}