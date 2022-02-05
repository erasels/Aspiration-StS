package aspiration.relics.rare;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class HatOfInfinitePower extends AspirationRelic {
    public static final String ID = "aspiration:HatOfInfinitePower";
    public static final int TURNS = 2;

    public HatOfInfinitePower() {
        super(ID, "HatOfInfinitePower.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(targetCard.type == AbstractCard.CardType.POWER && shouldMakePowersFree()) {
            flash();
        }
    }

    @Override
    public void atTurnStart() {
        if(GameActionManager.turn > TURNS) {
            grayscale = true;
        }
    }

    @Override
    public void onVictory() {
        grayscale = false;
    }

    //Called in CardCostModifcationPatches
    public static boolean shouldMakePowersFree() {
        return GameActionManager.turn <= TURNS;
    }
}