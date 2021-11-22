package aspiration.relics.crossovers;

import aspiration.relics.abstracts.AspirationRelic;
import beaked.cards.Inspiration;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EmptySkull extends AspirationRelic {
    public static final String ID = "aspiration:EmptySkull";

    private static final int INSP_AMT = 1;

    public EmptySkull() {
        super(ID, "EmptySkull.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];

    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(AbstractDungeon.player.hand.group.size() == 1) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Inspiration(), INSP_AMT, true, true));
        }
    }
}