package aspiration.patches.Unique;

import aspiration.patches.Fields.AbstractCardFields;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class RunicSpoonPatches {
    @SpirePatch(clz = AbstractPlayer.class, method = "playCard")
    public static class PlayerPlayCardDetection {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(AbstractPlayer __instance) {
            AbstractCardFields.playerPlayed.set(__instance.hoveredCard, true);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class resetPlayerPlayed {
        @SpirePostfixPatch
        public static void reset(UseCardAction __instance, AbstractCard ___targetCard) {
            if(__instance.isDone) {
                AbstractCardFields.playerPlayed.set(___targetCard, false);
            }
        }
    }
}