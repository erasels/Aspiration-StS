package aspiration.patches;

import aspiration.relics.boss.RandomNobGenerator;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="applyPreCombatLogic"
)
public class AtPrePreBattlePatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance) {
        if(__instance.hasRelic(RandomNobGenerator.ID)) {
            __instance.getRelic(RandomNobGenerator.ID).onTrigger();
        }
    }

}
