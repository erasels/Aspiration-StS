package aspiration.patches.blight;

import aspiration.blights.ChestSnatcher;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;

@SpirePatch(clz = BlightHelper.class, method = "getBlight")
public class InjectBlightPatches {
    @SpirePrefixPatch
    public static SpireReturn<AbstractBlight> returnBlight(String ID) {
        if (ID.equals(ChestSnatcher.ID)) {
            return SpireReturn.Return(new ChestSnatcher());
        }

        return SpireReturn.Continue();
    }
}