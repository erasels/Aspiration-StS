package aspiration.patches;

import aspiration.Aspiration;
import aspiration.Utility.RelicUtils;
import aspiration.relics.rare.DSix;
import aspiration.relics.uncommon.Nostalgia;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardDef;

import java.util.ArrayList;

public class NeowRewardPatches {
    //private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("aspiration:NeowRewards");
    //public static final String[] TEXT = uiStrings.TEXT;
    //Crashes when trying to load event because this is creates before UIStrings
	//Thanks Reina :D
	@SpireEnum
    public static NeowReward.NeowRewardType NOSTALGIA;
    @SpireEnum
    public static NeowReward.NeowRewardType DSIX;
	
	@SpirePatch(
            clz = NeowReward.class,
            method = "getRewardOptions"
    )
    public static class blessAddNostalgia {
	    @SpirePostfixPatch
        public static ArrayList<NeowRewardDef> patch(ArrayList<NeowRewardDef> __result, NeowReward __instance, final int category) {
            if (category == 2 && __instance.drawback != NeowReward.NeowRewardDrawback.CURSE) {
                String tmp;
                switch (Settings.language) {
                    case ZHS:
                        tmp = FontHelper.colorString("\u66f4\u6539\u6700\u591a\u0036\u4ef6\u9057\u7269\u548c\u836f\u6c34\u5956\u52b1", "g");
                        break;
                    default:
                        tmp = FontHelper.colorString("Reroll up to 6 relic and potion rewards", "g");
                }
                __result.add(new NeowRewardDef(DSIX, tmp + " ]"));
            }

            if (category == 3) {
                String tmp;
                switch (Settings.language) {
					case ZHS:
						tmp = FontHelper.colorString("\u989d\u5916\u83b7\u5f97\u4e00\u4ef6\u521d\u59cb\u9057\u7269", "g");
						break;
                    default:
                        tmp = FontHelper.colorString("Obtain an additional Starter Relic", "g");
                }
                __result.add(new NeowRewardDef(NOSTALGIA, "[ " + tmp + " ]"));
            }
            return __result;
        }
    }
	
	@SpirePatch(
            clz = NeowReward.class,
            method = "activate"
    )
    public static class ActivatePatch {
	    @SpirePrefixPatch
        public static void patch(NeowReward __instance) {
            if (__instance.type == NOSTALGIA) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new Nostalgia(Aspiration.uncommonNostalgia()));
                AbstractDungeon.uncommonRelicPool.removeIf(relic -> relic.equals(Nostalgia.ID));
                AbstractDungeon.shopRelicPool.removeIf(relic -> relic.equals(Nostalgia.ID));
            }
            if (__instance.type == DSIX) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new DSix());
                RelicUtils.removeRelicFromPool(DSix.ID, true);
            }
        }
    }
	
	
}
