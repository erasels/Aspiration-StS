package aspiration.patches;

import aspiration.actions.unique.UpdateOrbsAction;
import aspiration.orbs.AmalgamateOrb;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.mod.replay.orbs.CrystalOrb;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Plasma;

public class AmalgamateCrystalPatch {
    @SpirePatch(optional = true, cls = "replayTheSpire.patches.ReplayOrbIntPatch$ReplayAbstractOrbIntPatch", method = "Postfix")
    @SpirePatch(optional = true, cls = "replayTheSpire.patches.ReplayOrbIntPatch$ReplayDarkOrbIntPatch", method = "Postfix")
    public static class OrbCalculatePatch {
        public static void Postfix(AbstractOrb __instance) {
            int mypos = AbstractDungeon.player.orbs.indexOf(__instance);
            if (mypos > -1 && !__instance.ID.equals(Plasma.ORB_ID) && !__instance.ID.equals("conspire:Water") && !__instance.ID.equals(AmalgamateOrb.ORB_ID)) {
                if (mypos > 0) {
                    AbstractOrb adorb = AbstractDungeon.player.orbs.get(mypos - 1);
                    if (adorb instanceof AmalgamateOrb) {
                        if (!__instance.ID.equals(CrystalOrb.ORB_ID)) {
                            __instance.passiveAmount += ((AmalgamateOrb) adorb).crystalBonus;
                        }
                        if (!__instance.ID.equals(Dark.ORB_ID)) {
                            __instance.evokeAmount += ((AmalgamateOrb) adorb).crystalBonus;
                        }
                    }
                }
                if (mypos < AbstractDungeon.player.orbs.size() - 1) {
                    AbstractOrb adorb = AbstractDungeon.player.orbs.get(mypos + 1);
                    if (adorb instanceof AmalgamateOrb) {
                        if (!__instance.ID.equals(CrystalOrb.ORB_ID)) {
                            __instance.passiveAmount += ((AmalgamateOrb) adorb).crystalBonus;
                        }
                        if (!__instance.ID.equals(Dark.ORB_ID)) {
                            __instance.evokeAmount += ((AmalgamateOrb) adorb).crystalBonus;
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
    public static class OrbChannelPatch {
        public static void Postfix(AbstractPlayer __Instance, AbstractOrb orb) {
            AbstractDungeon.actionManager.addToBottom(new UpdateOrbsAction());
        }
    }
}