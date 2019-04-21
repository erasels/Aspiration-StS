package aspiration.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

public class FastMakeTempCardInDrawPileAction extends AbstractGameAction {
    private AbstractCard cardToMake;
    private boolean randomSpot;
    private boolean toBottom;

    public FastMakeTempCardInDrawPileAction(AbstractCard card, int amount, boolean randomSpot, boolean toBottom) {
        UnlockTracker.markCardAsSeen(card.cardID);
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = 0.1F;
        this.cardToMake = card;
        this.randomSpot = randomSpot;
        this.toBottom = toBottom;
    }

    public FastMakeTempCardInDrawPileAction(AbstractCard card, int amount, boolean randomSpot) {
        this(card, amount, randomSpot, false);
    }

    public void update() {
        if (this.duration == 0.1F) {
            ShowCardAndAddToDrawPileEffect effect = new ShowCardAndAddToDrawPileEffect(this.cardToMake.makeStatEquivalentCopy(), this.randomSpot, this.toBottom);
            if (Settings.FAST_MODE)
                effect.duration = 0.5f;

            AbstractDungeon.effectList.add(effect);
        }

        this.tickDuration();
    }
}