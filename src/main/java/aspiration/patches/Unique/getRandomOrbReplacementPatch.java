package aspiration.patches.Unique;

import aspiration.orbs.OrbUtilityMethods;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class getRandomOrbReplacementPatch {
    @SpirePatch(
            clz = AbstractOrb.class,
            method = "getRandomOrb"
    )
    public static class RandomOrbRetrieval {
        @SpirePrefixPatch
        public static SpireReturn<AbstractOrb> Prefix(boolean useCardRng) {
            return SpireReturn.Return(OrbUtilityMethods.getWeightedRandomOrb(useCardRng ? AbstractDungeon.cardRandomRng : AbstractDungeon.miscRng, false));
        }
    }
}
