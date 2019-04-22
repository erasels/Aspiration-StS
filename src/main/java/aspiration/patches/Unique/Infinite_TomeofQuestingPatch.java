package aspiration.patches.Unique;

import aspiration.relics.crossovers.TomeofQuesting;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        cls = "infinitespire.quests.QuestLog",
        method = "receivePostUpdate",
        optional = true
)
public class Infinite_TomeofQuestingPatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("giveReward")) {
                    m.replace("{" +
                            "$_ = $proceed($$);" +
                            "aspiration.patches.Unique.Infinite_TomeofQuestingPatch.Nested.Do();" +
                            "}");
                }
            }
        };
    }

    public static class Nested {
        public static void Do() {
            AbstractRelic relic = AbstractDungeon.player.getRelic(TomeofQuesting.ID);
            if (relic != null) {
                relic.onTrigger();
            }
        }
    }
}