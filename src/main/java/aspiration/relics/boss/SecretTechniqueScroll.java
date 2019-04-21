package aspiration.relics.boss;

import aspiration.actions.AddActionPostActionAction;
import aspiration.actions.FastMakeTempCardInDrawPileAction;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SecretTechniqueScroll extends AspirationRelic {
    public static final String ID = "aspiration:SecretTechniqueScroll";

    private static final int DRAW_AMT = 1;
    private static final int RANDOM = 3;

    public SecretTechniqueScroll() {
        super(ID, "SecretTechniqueScroll.png", AbstractRelic.RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAW_AMT + DESCRIPTIONS[1] + RANDOM + DESCRIPTIONS[2];
    }

    public void onEquip() {
        AbstractDungeon.player.masterHandSize += DRAW_AMT;
    }

    public void onUnequip() {
        AbstractDungeon.player.masterHandSize -= DRAW_AMT;
    }

    public void onShuffle() {
        flash();
        AbstractDungeon.actionManager.addToTop(new AddActionPostActionAction(new RelicAboveCreatureAction(AbstractDungeon.player, this), (action) -> action instanceof EmptyDeckShuffleAction));
        for (int i = 0; i < RANDOM; i++) {
            AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
            c.freeToPlayOnce = true;
            AbstractDungeon.actionManager.addToTop(new AddActionPostActionAction(new FastMakeTempCardInDrawPileAction(c, 1, true), (action) -> action instanceof EmptyDeckShuffleAction));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SecretTechniqueScroll();
    }
}
