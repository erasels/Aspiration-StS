package aspiration.patches.Unique;

import aspiration.relics.rare.DSix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

@SpirePatch(clz = CombatRewardScreen.class, method = "open", paramtypez = {})
public class DSixRewardScreenTriggerPatch {
    @SpirePostfixPatch
    public static void trigger(CombatRewardScreen __instance) {
        DSix d6 = (DSix) AbstractDungeon.player.getRelic(DSix.ID);
        if(d6 != null) {
            if(d6.counter > 0 && d6.countRewards() > 0) {
                d6.beginLongPulse();
            }
        }
    }
}
