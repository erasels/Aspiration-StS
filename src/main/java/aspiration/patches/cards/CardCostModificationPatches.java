package aspiration.patches.cards;

import aspiration.relics.rare.HatOfInfinitePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class CardCostModificationPatches {
    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlayPatch {
        @SpirePostfixPatch
        public static boolean patch(boolean __result, AbstractCard __instance) {
            if(__result)
                return true;

            if(isIndeedWithoutADoubtInCombat() && __instance.type == AbstractCard.CardType.POWER && HatOfInfinitePower.shouldMakePowersFree()) {
                /*HatOfInfinitePower r = (HatOfInfinitePower) AbstractDungeon.player.getRelic(HatOfInfinitePower.ID);
                if(r != null) {
                    return r.shouldMakePowersFree();
                }*/
                return AbstractDungeon.player.hasRelic(HatOfInfinitePower.ID);
            }

            return __result;
        }
    }

    private static boolean isIndeedWithoutADoubtInCombat() {
        return (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT);
    }
}
