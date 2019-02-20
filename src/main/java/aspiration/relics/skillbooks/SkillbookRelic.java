package aspiration.relics.skillbooks;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class SkillbookRelic extends AspirationRelic implements SkillbookRelicDescription {

    protected SkillbookRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        initializeTips();
    }

    protected SkillbookRelic(String setId, String imgName, RelicTier tier, LandingSound sfx, PowerTip pTip) {
        super(setId, imgName, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        tips.add(pTip);
        initializeTips();
    }

    protected SkillbookRelic(String setId, String imgName, RelicTier tier, LandingSound sfx, ArrayList<PowerTip> pTips) {
        super(setId, imgName, tier, sfx);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        tips.addAll(pTips);
        initializeTips();
    }

    public static boolean hasSkillbookRelic(AbstractPlayer p) {
        for(AbstractRelic r : p.relics) {
            if(r instanceof SkillbookRelic) {
                return true;
            }
        }
        return false;
    }

    protected void mixCardpools(ArrayList<AbstractCard> cardList) {
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
