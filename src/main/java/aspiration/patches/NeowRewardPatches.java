package aspiration.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardDef;

import aspiration.relics.Nostalgia;

public class NeowRewardPatches {
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
                    __result.add(new NeowRewardDef(NOSTALGIA, "[ #gObtain #gadditional #gStarter #gRelic ]"));
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
