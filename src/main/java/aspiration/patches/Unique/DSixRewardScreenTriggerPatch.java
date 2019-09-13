package aspiration.patches.Unique;

import aspiration.relics.rare.DSix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

public class DSixRewardScreenTriggerPatch {
    @SpirePatch(clz = CombatRewardScreen.class, method = "open", paramtypez = {})
    public static class Normal {
        @SpirePostfixPatch
        public static void trigger(CombatRewardScreen __instance) {
            dcheck();
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "openCombat", paramtypez = {String.class, boolean.class})
    public static class Mugger {
        @SpirePostfixPatch
        public static void trigger(CombatRewardScreen __instance, String x, boolean y) {
            dcheck();
        }
    }

    private static void dcheck() {
        DSix d6 = (DSix) AbstractDungeon.player.getRelic(DSix.ID);
        if (d6 != null) {
            if (d6.counter > 0 && d6.countRewards() > 0) {
                d6.beginLongPulse();
            }
        }
    }
}
