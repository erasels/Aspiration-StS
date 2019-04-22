package aspiration.patches.Fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.AbstractPower;

@SpirePatch(
        clz= AbstractPower.class,
        method=SpirePatch.CLASS
)
public class AbstractPowerScribeBookField {
    public static SpireField<Boolean> ssbTriggered = new SpireField<>(() -> false);
}
