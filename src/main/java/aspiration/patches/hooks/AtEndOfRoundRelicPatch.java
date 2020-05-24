package aspiration.patches.hooks;

import aspiration.relics.abstracts.AtEndOfRound;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = MonsterGroup.class, method = "applyEndOfTurnPowers")
public class AtEndOfRoundRelicPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(MonsterGroup __instance) {
        AbstractDungeon.player.relics.stream().filter(r -> r instanceof AtEndOfRound).forEach(r -> ((AtEndOfRound) r).atEndOfRound());
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}
