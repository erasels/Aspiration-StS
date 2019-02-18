package aspiration.relics.crossovers;

import aspiration.relics.abstracts.AspirationRelic;
import beaked.cards.Inspiration;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EmptySkull extends AspirationRelic {
    public static final String ID = "aspiration:EmptySkull";

    private static final int INSP_AMT = 1;

    public EmptySkull() {
        super(ID, "EmptySkull.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if(INSP_AMT > 1) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[2] + INSP_AMT + DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        }

    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(AbstractDungeon.player.hand.group.size() == 1) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Inspiration(), INSP_AMT, true, true));
        }
    }

    public AbstractRelic makeCopy() {
        return new EmptySkull();
    }
}