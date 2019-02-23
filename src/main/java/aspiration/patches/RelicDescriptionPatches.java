package aspiration.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CtBehavior;
import org.apache.commons.lang3.StringUtils;

public class RelicDescriptionPatches {

    public static final String BLACKBEARD_REMOVE_SPACE = "[blackbeard:REMOVE_SPACE]";

    @SpirePatch(clz = FontHelper.class, method = "renderSmartText", paramtypez = {SpriteBatch.class, BitmapFont.class, String.class, float.class, float.class, float.class, float.class, Color.class})
    public static class FontHelperRenderSmartTextPatch {

        public static boolean removeSpace = false;

        @SpireInsertPatch(locator = RemoveSpecialWordLocator.class, localvars = {"word"})
        public static void Insert(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor, @ByRef String[] word) {
            removeSpace = false;
            if (word[0].startsWith(BLACKBEARD_REMOVE_SPACE)) {
                word[0] = word[0].replace(BLACKBEARD_REMOVE_SPACE, StringUtils.EMPTY);
                removeSpace = true;
            }
        }

        private static class RemoveSpecialWordLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(String.class, "equals");
                return LineFinder.findInOrder(method, matcher);
            }
        }

        @SpireInsertPatch(locator = RemoveSpaceLocator.class, localvars = {"curWidth", "spaceWidth"})
        public static void Insert(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor, @ByRef float[] curWidth, float spaceWidth) {
            if (removeSpace) {
                curWidth[0] -= spaceWidth;
            }
        }

        private static class RemoveSpaceLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(BitmapFont.class, "draw");
                return new int[]{LineFinder.findAllInOrder(method, matcher)[1]};
            }
        }
    }

    @SpirePatch(clz = FontHelper.class, method = "getSmartHeight", paramtypez = {BitmapFont.class, String.class, float.class, float.class})
    public static class FontHelperGetSmartHeightPatch {

        @SpireInsertPatch(locator = RemoveSpecialWordLocator.class, localvars = {"word"})
        public static void Insert(BitmapFont font, String msg, float lineWidth, float lineSpacing, @ByRef String[] word) {
            if (word[0].startsWith(BLACKBEARD_REMOVE_SPACE)) {
                word[0] = word[0].replace(BLACKBEARD_REMOVE_SPACE, StringUtils.EMPTY);
            }
        }

        private static class RemoveSpecialWordLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior method) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(String.class, "equals");
                return LineFinder.findInOrder(method, matcher);
            }
        }
    }
}