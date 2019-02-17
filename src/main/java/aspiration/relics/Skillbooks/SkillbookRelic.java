package aspiration.relics.Skillbooks;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class SkillbookRelic extends AspirationRelic {

    public SkillbookRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
    }

    public void mixCardpools(ArrayList<AbstractCard> cardList) {
        for (AbstractCard c : cardList) {
            if(c.rarity != AbstractCard.CardRarity.BASIC) {
                switch (c.rarity) {
                    case COMMON: {
                        AbstractDungeon.commonCardPool.addToTop(c);
                        AbstractDungeon.srcCommonCardPool.addToBottom(c);
                        continue;
                    }
                    case UNCOMMON: {
                        AbstractDungeon.uncommonCardPool.addToTop(c);
                        AbstractDungeon.srcUncommonCardPool.addToBottom(c);
                        continue;
                    }
                    case RARE: {
                        AbstractDungeon.rareCardPool.addToTop(c);
                        AbstractDungeon.srcRareCardPool.addToBottom(c);
                        continue;
                    }
                    default: {
                        AbstractDungeon.uncommonCardPool.addToTop(c);
                        AbstractDungeon.srcUncommonCardPool.addToBottom(c);
                    }
                }
            }
        }
    }
}
