package aspiration.cardMods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class IncreaseCostUntilPlayedCMod extends AbstractCardModifier {
    private int originalCost;
    private int costInc;

    public IncreaseCostUntilPlayedCMod(int amt) {
        costInc = amt;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        originalCost = card.cost;
        card.updateCost(costInc);
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.updateCost(-costInc);
        if(card.cost == originalCost) {
            card.isCostModified = false;
        }
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return "aspiration:IncCost";
    }

    public int getAmt() {
        return costInc;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IncreaseCostUntilPlayedCMod(costInc);
    }
}
