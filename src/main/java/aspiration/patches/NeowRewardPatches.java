package aspiration.patches;

import aspiration.util.RelicUtils;
import aspiration.blights.ChestSnatcher;
import aspiration.relics.rare.DSix;
import aspiration.relics.shop.Nostalgia;
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
    @SpireEnum
    public static NeowReward.NeowRewardDrawback PUNISHING;

    @SpirePatch(clz = NeowReward.class, method = "getRewardDrawbackOptions")
    public static class NewDrawback {
        @SpirePostfixPatch
        public static ArrayList<NeowReward.NeowRewardDrawbackDef> patch(ArrayList<NeowReward.NeowRewardDrawbackDef> __result, NeowReward __instance) {
            String tmp;
            switch (Settings.language) {
                case ZHS:
                    tmp = FontHelper.colorString("\u4f60\u6253\u5f00\u7684\u7b2c\u4e00\u4e2a\u80f8\u90e8\u662f\u7a7a\u7684", "r");
                    break;
                default:
                    tmp = FontHelper.colorString("First opened chest is empty", "r") + " ";
            }
            __result.add(new NeowReward.NeowRewardDrawbackDef(PUNISHING, "[ " + tmp));
            return __result;
        }
    }
	
	@SpirePatch(
            clz = NeowReward.class,
            method = "getRewardOptions"
    )
    public static class AddRewards {
	    @SpirePostfixPatch
        public static ArrayList<NeowRewardDef> patch(ArrayList<NeowRewardDef> __result, NeowReward __instance, final int category) {
            if (category == 2 && __instance.drawback == PUNISHING) {
                __result.clear();
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
	        if(__instance.drawback == PUNISHING) {
	            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new ChestSnatcher());
            }

            if (__instance.type == NOSTALGIA) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new Nostalgia());
                AbstractDungeon.shopRelicPool.removeIf(relic -> relic.equals(Nostalgia.ID));
            } else if (__instance.type == DSIX) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new DSix());
                RelicUtils.removeRelicFromPool(DSix.ID, true);
            }
        }
    }
}
