package aspiration.patches.Fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.WingBoots;

@SpirePatch(
        clz= WingBoots.class,
        method=SpirePatch.CLASS
)
public class WingBootsCampfireactionField {
    public static SpireField<Boolean> campUsed = new SpireField<>(() -> false);
}
