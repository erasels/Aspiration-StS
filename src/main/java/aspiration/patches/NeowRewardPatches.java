package aspiration.patches;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardDef;

import aspiration.relics.Nostalgia;

public class NeowRewardPatches {
    //private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("aspiration:NeowRewards");
    //public static final String[] TEXT = uiStrings.TEXT;
    //Crashes when trying to load event because this is creates before UIStrings
	//Thanks Reina :D
	@SpireEnum
    public static NeowReward.NeowRewardType NOSTALGIA;
	
	@SpirePatch(
            clz = NeowReward.class,
            method = "getRewardOptions"
    )
    public static class blessAddNostalgia {
        public static ArrayList<NeowRewardDef> Postfix(ArrayList<NeowRewardDef> __result, NeowReward __instance, final int category) {
                if (category == 3) {
                    String tmp;
                    switch (Settings.language) {
                        case RUS:
                            String bleh ="Получить дополнительную стартовую реликвию";
                            byte bytes[] = bleh.getBytes(StandardCharsets.UTF_8);
                            String blah = new String(bytes, StandardCharsets.UTF_8);
                            tmp = FontHelper.colorString(blah, "g");
                            break;
                        case DEU:
                            tmp = FontHelper.colorString("Obtain additional Starter Relic", "g");
                            break;
                        default:
                            tmp = FontHelper.colorString("Obtain additional Starter Relic", "g");
                    }
                    __result.add(new NeowRewardDef(NOSTALGIA, "[ "+ tmp +" ]"));
                }
                return __result;
        }
    }
	
	@SpirePatch(
            clz = NeowReward.class,
            method = "activate"
    )
    public static class ActivatePatch {
        public static void Prefix(NeowReward __instance) {
            if (__instance.type == NOSTALGIA) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new Nostalgia(false));
            }
        }
    }
	
	
}
