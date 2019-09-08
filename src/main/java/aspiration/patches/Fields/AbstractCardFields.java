package aspiration.patches.Fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz=AbstractCard.class,
        method=SpirePatch.CLASS
)
public class AbstractCardFields {
    public static SpireField<Boolean> ppTriggered = new SpireField<>(() -> false);
    public static SpireField<Boolean> playerPlayed = new SpireField<>(() -> false);
}
