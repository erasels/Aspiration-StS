package aspiration.Utility;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicUtils {
    public static boolean removeRelicFromPool(AbstractRelic r, boolean allPools) {
        if(allPools) {
            AbstractDungeon.bossRelicPool.removeIf(id -> id.equals(r.relicId));
            AbstractDungeon.shopRelicPool.removeIf(id -> id.equals(r.relicId));
            AbstractDungeon.rareRelicPool.removeIf(id -> id.equals(r.relicId));
            AbstractDungeon.uncommonRelicPool.removeIf(id -> id.equals(r.relicId));
            AbstractDungeon.commonRelicPool.removeIf(id -> id.equals(r.relicId));
        } else {
            if (r.tier != null) {
                switch (r.tier) {
                    case COMMON:
                        AbstractDungeon.commonRelicPool.removeIf(id -> id.equals(r.relicId));
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonRelicPool.removeIf(id -> id.equals(r.relicId));
                        break;
                    case RARE:
                        AbstractDungeon.rareRelicPool.removeIf(id -> id.equals(r.relicId));
                        break;
                    case SHOP:
                        AbstractDungeon.shopRelicPool.removeIf(id -> id.equals(r.relicId));
                        break;
                    case BOSS:
                        AbstractDungeon.bossRelicPool.removeIf(id -> id.equals(r.relicId));
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    public static boolean removeRelicFromPool(String rel, boolean allPools) {
        if(allPools) {
            AbstractDungeon.bossRelicPool.removeIf(id -> id.equals(rel));
            AbstractDungeon.shopRelicPool.removeIf(id -> id.equals(rel));
            AbstractDungeon.rareRelicPool.removeIf(id -> id.equals(rel));
            AbstractDungeon.uncommonRelicPool.removeIf(id -> id.equals(rel));
            AbstractDungeon.commonRelicPool.removeIf(id -> id.equals(rel));
        } else {
            AbstractRelic r = RelicLibrary.getRelic(rel);
            return removeRelicFromPool(r, false);
        }
        return true;
    }

    public static boolean removeRelicFromPool(AbstractRelic r) {
        return removeRelicFromPool(r, false);
    }
    public static boolean removeRelicFromPool(String r) {
        return removeRelicFromPool(r, false);
    }

    public static boolean deckDescriptionSearch(String[] keywords) {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            for (String keyword : keywords) {
                if (c.rawDescription.toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
