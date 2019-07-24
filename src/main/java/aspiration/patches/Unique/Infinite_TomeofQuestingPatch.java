package aspiration.patches.Unique;

import aspiration.relics.crossovers.TomeofQuesting;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.abstracts.Quest;
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
                            Infinite_TomeofQuestingPatch.class.getName() +".triggerTome(this.get(i));" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }

    public static void triggerTome(Object q) {
        TomeofQuesting relic = (TomeofQuesting) AbstractDungeon.player.getRelic(TomeofQuesting.ID);
        if (relic != null) {
            relic.onTrigger((Quest)q);
        }
    }
}