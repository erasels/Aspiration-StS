package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ModifyCostAction extends AbstractGameAction {
    int costInc, originalCost;
    AbstractCard c;
    public ModifyCostAction(int costInc, int originalCost, AbstractCard c) {
        this.costInc = costInc;
        this.originalCost = originalCost;
        this.c = c;
    }

    @Override
    public void update() {
        c.updateCost(-costInc);
        if(c.cost == originalCost) {
            c.isCostModified = false;
        }
        isDone = true;
    }
}
