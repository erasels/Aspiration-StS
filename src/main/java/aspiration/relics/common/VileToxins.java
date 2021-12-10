package aspiration.relics.common;

import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.stslib.patches.powerInterfaces.HealthBarRenderPowerPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import javassist.CtBehavior;

public class VileToxins extends AspirationRelic {
	public static final String ID = "aspiration:VileToxins";

    private static final int ADD_DMG = 5;

    public VileToxins() {
        super(ID, "VileToxins.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], ADD_DMG);
    }
    
    @Override
    public boolean canSpawn() //Checked when? AbstractDungeon.returnRandomRelicKey
    {
    	return deckDescriptionSearch(PoisonPower.NAME, PoisonPower.POWER_ID) || AbstractDungeon.player instanceof TheSilent;
    }

    //Increase damage dealt by poison lose hp action
    @SpirePatch2(clz = PoisonLoseHpAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class IncreasePoisonDamage {
        @SpirePostfixPatch
        public static void patch(PoisonLoseHpAction __instance) {
            if(AbstractDungeon.player.hasRelic(ID)) {
                __instance.amount += ADD_DMG;
            }
        }
    }

    @SpirePatch2(clz = PoisonPower.class, method = "updateDescription")
    public static class FixDescription {
        @SpirePostfixPatch
        public static void patch(PoisonPower __instance) {
            if(AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
                __instance.description = __instance.description.replace(Integer.toString(__instance.amount), Integer.toString(__instance.amount + ADD_DMG));
            }
        }
    }

    //Increase the width of the poison hp loss bar rendering by increaseing the amount of retrieved poison for both stslib and basegame calculations
    @SpirePatch2(clz = AbstractCreature.class, method = "renderRedHealthBar")
    @SpirePatch2(clz = HealthBarRenderPowerPatch.RenderPowerHealthBar.class, method = "Insert")
    public static class FixPoisonHealthDamageRender {
        @SpireInsertPatch(locator = Locator.class, localvars = {"poisonAmt"})
        public static void patch(@ByRef int[] poisonAmt) {
            if(AbstractDungeon.player.hasRelic(ID)) {
                poisonAmt[0] += ADD_DMG;
            }
        }

        //line below getPower(poison)
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "hasPower");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]};
            }
        }
    }
}