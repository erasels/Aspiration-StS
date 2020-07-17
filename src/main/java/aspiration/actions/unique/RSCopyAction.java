package aspiration.actions.unique;

import aspiration.patches.cards.CostModifiedUntilPlayedPatches;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class RSCopyAction extends MakeTempCardInHandAction {
    AbstractCard bakC;
    public RSCopyAction(AbstractCard card) {
        super(card);
        bakC = card;
    }

    @SpireOverride
    private AbstractCard makeNewCard() {
        AbstractCard tmp = bakC.makeStatEquivalentCopy();
        CostModifiedUntilPlayedPatches.ModifiedUntilPlayedSpireField.isCostModifiedUntilPlayed.set(tmp, true);
        tmp.setCostForTurn(tmp.costForTurn + 1);
        return tmp;
    }
}
