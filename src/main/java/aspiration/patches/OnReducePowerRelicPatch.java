package aspiration.patches;

import aspiration.relics.abstracts.OnReducePower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OnReducePowerRelicPatch {

    /*@SpirePatch(
            clz = ReducePowerAction.class,
            method = "update"
    )
    public static class onReducePowerListener {
        @SpireInsertPatch(
                rloc=0
        )
        public static SpireReturn<?> Insert(ReducePowerAction __instance) {
            boolean firstTime = true;
            String pString = null;
            AbstractPower pPower = null;
            //System.out.println("Started.");
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof OnReducePower) {
                    if(firstTime) {
                        try {
                            Field privatePSting = ReducePowerAction.class.getDeclaredField("powerID");
                            privatePSting.setAccessible(true);
                            pString = (String) privatePSting.get(__instance);

                            Field privatePPower = ReducePowerAction.class.getDeclaredField("powerInstance");
                            privatePPower.setAccessible(true);
                            pPower = (AbstractPower) privatePPower.get(__instance);

                            firstTime = false;
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(pString != null) {
                        if(__instance.target.hasPower(pString)) {
                            pPower = __instance.target.getPower(pString);
                        }
                    } else if(pPower == null) {
                        return SpireReturn.Return(null);
                    }

                    //System.out.println("Power instance acquired: " + pPower.ID);

                    if(!((OnReducePower) r).OnReducePower(__instance.target, __instance.source, pPower, __instance.amount)) {
                        return SpireReturn.Return(null);
                    }

                }
            }
            return SpireReturn.Continue();
        }
    }*/

    @SpirePatch(
            clz = AbstractPower.class,
            method = "reducePower"
    )
    public static class onReducePowerListener {
        public static void Prefix(AbstractPower __instance, @ByRef int[] amount) {
            //System.out.println("Started. Amount is: " + amount[0]);
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof OnReducePower) {
                    //System.out.println("Power instance acquired: " + pPower.ID);
                    amount[0] = ((OnReducePower) r).OnReducePower(__instance, amount[0]);
                }
            }
        }
    }
}
