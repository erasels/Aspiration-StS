package aspiration.patches.Unique;

import aspiration.Aspiration;
import aspiration.relics.skillbooks.SkillbookRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SkillbookPatches {
    /*@SpirePatch(
            clz=RewardItem.class,
            method="claimReward"
    )
    public static class ClaimReward
    {
        public static void Postfix(RewardItem __instance)
        {
            if (__instance.type == RewardItem.RewardType.CARD && SkillbookRelic.hasSkillbookRelic(AbstractDungeon.player)) {
                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if(r instanceof SkillbookRelic) {
                        r.flash();
                    }
                }
            }
        }
    }*/

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class FlashSkillbooks {
        public static void Postfix() {
            if (Aspiration.skillbookCardpool()) {
                if (SkillbookRelic.hasSkillbookRelic(AbstractDungeon.player)) {
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        if (r instanceof SkillbookRelic) {
                            r.flash();
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeCardPools"
    )
    public static class CardpoolInitFix {
        public static void Postfix(AbstractDungeon __instance) {
            if (Aspiration.skillbookCardpool() && AbstractDungeon.player != null) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof SkillbookRelic) {
                        ((SkillbookRelic) r).modifyCardPool();
                    }
                }
            }
        }
    }
}