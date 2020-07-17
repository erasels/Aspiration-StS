package aspiration.actions.unique;

import aspiration.cardMods.IncreaseCostUntilPlayedCMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class RSCopyAction extends MakeTempCardInHandAction {
    AbstractCard bakC;

    public RSCopyAction(AbstractCard card) {
        super(card);
        bakC = card;
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = "makeNewCard")
    public static class ModifyCard {
        @SpirePostfixPatch
        public static AbstractCard patch(AbstractCard __result, MakeTempCardInHandAction __instance) {
            if (__instance instanceof RSCopyAction) {
                CardModifierManager.addModifier(__result, new IncreaseCostUntilPlayedCMod(1));
            }
            return __result;
        }
    }


}
