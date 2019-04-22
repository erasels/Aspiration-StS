package aspiration.patches.hooks;

import aspiration.relics.abstracts.OnEnergyUse;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class onUseEnergyRelicPatch {
    @SpirePatch(
            clz = EnergyPanel.class,
            method = "useEnergy"
    )
    public static class onUseEnergyListener {
        public static void Prefix(@ByRef int[] e) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof OnEnergyUse) {
                    e[0] = ((OnEnergyUse) r).onEnergyUse(e[0]);
                }
            }
        }
    }
}
