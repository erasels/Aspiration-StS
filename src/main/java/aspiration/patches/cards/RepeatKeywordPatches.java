package aspiration.patches.cards;

import aspiration.patches.Fields.AbstractCardFields;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class RepeatKeywordPatches {
    @SpirePatch(clz= AbstractPlayer.class, method="useCard")
    public static class MultiUse {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
                        m.replace("{" +
                                "for(int i = "+ RepeatKeywordPatches.class.getName() +".getRepeatCount(c); i > 0; i--) {" +
                                "c.use($$);" +
                                "}" +
                                "$proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }

    public static int getRepeatCount(AbstractCard c) {
        return AbstractCardFields.repeats.get(c);
    }
}