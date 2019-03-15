package aspiration.Utility;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

    public static boolean removeRelicFromPool(AbstractRelic r) {
        return removeRelicFromPool(r, false);
    }
}
