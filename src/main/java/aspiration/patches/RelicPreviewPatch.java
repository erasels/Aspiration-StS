package aspiration.patches;

import aspiration.ui.events.RelicPreviewEventButton;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

@SpirePatch(clz = GenericEventDialog.class, method = "render")
public class RelicPreviewPatch {
    @SpireInsertPatch(rloc = 70)
    public static void Insert(GenericEventDialog __instance, SpriteBatch sb) {
        for (LargeDialogOptionButton b : __instance.optionList) {
            if ((b instanceof RelicPreviewEventButton)) {
                RelicPreviewEventButton rb = (RelicPreviewEventButton) b;
                rb.renderRelicPreview(sb);
            }
        }
    }
}
