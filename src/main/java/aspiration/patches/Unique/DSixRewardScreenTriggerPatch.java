package aspiration.patches.Unique;

import aspiration.relics.rare.DSix;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.CallingBell;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

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

    @SpirePatch(clz = CallingBell.class, method = "update")
    public static class CallingbellCompat {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(CallingBell __instance) {
            dcheck();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "rewardPopOutTimer");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
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
