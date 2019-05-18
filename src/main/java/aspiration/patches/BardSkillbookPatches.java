package aspiration.patches;

import aspiration.relics.skillbooks.BardSkillbook;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BardSkillbookPatches {
    @SpirePatch(cls = "com.evacipated.cardcrawl.mod.bard.patches.BagPipesCardNotesPreviewPatch", method = "Insert1", optional = true)
    @SpirePatch(cls = "com.evacipated.cardcrawl.mod.bard.patches.BagPipesCardNotesPreviewPatch", method = "Insert2", optional = true)
    @SpirePatch(cls = "com.evacipated.cardcrawl.mod.bard.patches.BagPipesCardNotesPreview2Patch", method = "Prefix", optional = true)
    public static class DoNoteEvalIfHasSkillbookMyDude {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("hasRelic")) {
                        m.replace("{" +
                                "if("+AbstractDungeon.class.getName()+".player != null && " + AbstractDungeon.class.getName() + ".player.hasRelic(\""+ BardSkillbook.ID +"\")){" +
                                "$_ = true;"+
                                "} else {"+
                                "$_ = ("+AbstractDungeon.class.getName()+".player != null && " + AbstractDungeon.class.getName() + ".player.hasRelic($$));}" +
                                "}");
                    }
                }
            };
        }
    }
}
