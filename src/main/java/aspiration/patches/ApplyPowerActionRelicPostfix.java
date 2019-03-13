package aspiration.patches;

import aspiration.relics.EvolvingReagent;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

@SpirePatch(
        clz=ApplyPowerAction.class,
        method=SpirePatch.CONSTRUCTOR,
        paramtypez={
        		AbstractCreature.class,
        		AbstractCreature.class,
        		AbstractPower.class,
        		int.class,
        		boolean.class,
        		AbstractGameAction.AttackEffect.class
        }
)
public class ApplyPowerActionRelicPostfix {
	 public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
     {
		 /*if ((AbstractDungeon.player.hasRelic(SneckoTail.ID)) && (source != null) && (source.isPlayer) && (target != source) && target != AbstractDungeon.player && (powerToApply.ID.equals("Poison")) && target.hasPower(VulnerablePower.POWER_ID))
		 {
			 AbstractDungeon.player.getRelic(SneckoTail.ID).flash();
			 powerToApply.amount *= 2;
			 __instance.amount *= 2;
		 }*/
		 
		 if(AbstractDungeon.player.hasRelic(EvolvingReagent.ID) && ((EvolvingReagent) AbstractDungeon.player.getRelic(EvolvingReagent.ID)).isPoisonDoubled()  && (source != null) && (source.isPlayer) && (target != source) && (powerToApply.ID.equals(PoisonPower.POWER_ID))) {
			 AbstractDungeon.player.getRelic(EvolvingReagent.ID).flash();
			 powerToApply.amount *= 2;
			 __instance.amount *= 2;
		 }
     }

}
