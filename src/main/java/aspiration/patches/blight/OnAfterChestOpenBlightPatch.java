package aspiration.patches.blight;

import aspiration.blights.abstracts.AfterChestOpenBlight;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = AbstractChest.class, method = "open")
public class OnAfterChestOpenBlightPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(AbstractChest __instance, boolean bossChest) {
        for(AbstractBlight b : AbstractDungeon.player.blights) {
            if(b instanceof AfterChestOpenBlight) {
                ((AfterChestOpenBlight)b).onAfterChestOpen(bossChest, __instance);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "open");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
