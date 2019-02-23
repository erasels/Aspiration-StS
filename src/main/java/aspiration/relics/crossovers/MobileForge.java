package aspiration.relics.crossovers;

import aspiration.actions.unique.EnhanceRandomCardInHandAction;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import runesmith.patches.EnhanceCountField;

public class MobileForge extends AspirationRelic {
    public static final String ID = "aspiration:MobileForge";

    private static final int ENHANCE_AMT = 1;

    public MobileForge() {
        super(ID, "MobileForge.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.upgraded) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new EnhanceRandomCardInHandAction(ENHANCE_AMT));
        }
        if(EnhanceCountField.lastEnhance.get(card) > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new UpgradeRandomCardAction());
        }
    }

    public AbstractRelic makeCopy() {
        return new MobileForge();
    }
}