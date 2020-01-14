package aspiration.patches;

import aspiration.relics.skillbooks.BardSkillbook;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BardSkillbookPatches {
    @SpirePatch(cls = "com.evacipated.cardcrawl.mod.bard.relics.BagPipes", method = "hasNotesAvailable", optional = true)
    public static class DoNoteEvalIfHasSkillbookMyDude {
        @SpirePostfixPatch
        public static boolean patch(boolean __result) {
            return __result || (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BardSkillbook.ID));
        }
    }
}
