package aspiration.patches;

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
                        String foo = "\u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0434\u043e\u043f\u043e\u043b\u043d\u0438\u0442\u0435\u043b\u044c\u043d\u0443\u044e \u0441\u0442\u0430\u0440\u0442\u043e\u0432\u0443\u044e \u0440\u0435\u043b\u0438\u043a\u0432\u0438\u044e";
                        //byte x[] = foo.getBytes(StandardCharsets.UTF_8);
                        //String y = new String(x, StandardCharsets.UTF_8);
                        //String blah = new String("\\u041f\\u043e\\u043b\\u0443\\u0447\\u0438\\u0442\\u044c \\u0434\\u043e\\u043f\\u043e\\u043b\\u043d\\u0438\\u0442\\u0435\\u043b\\u044c\\u043d\\u0443\\u044e \\u0441\\u0442\\u0430\\u0440\\u0442\\u043e\\u0432\\u0443\\u044e \\u0440\\u0435\\u043b\\u0438\\u043a\\u0432\\u0438\\u044e".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                        tmp = FontHelper.colorString(foo, "g");
                        break;
                    case DEU:
                        tmp = FontHelper.colorString("Erhalte ein zusätzliches Anfängerrelikt", "g");
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
        public static void Prefix(NeowReward __instance) {
            if (__instance.type == NOSTALGIA) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 3, Settings.HEIGHT / 2, new Nostalgia(false));
            }
        }
    }
	
	
}
