package aspiration.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import aspiration.relics.SneckoTail;

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
public class ApplyPowerAction_Postfix_SneckoTail {
	 public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
     {
		 if ((AbstractDungeon.player.hasRelic(SneckoTail.ID)) && (source != null) && (source.isPlayer) && (target != source) && target != AbstractDungeon.player && (powerToApply.ID.equals("Poison")) && (target.hasPower(WeakPower.POWER_ID) || target.hasPower(VulnerablePower.POWER_ID)))
		 {
			 AbstractDungeon.player.getRelic(SneckoTail.ID).flash();
			 powerToApply.amount *= 2;
			 __instance.amount *= 2;
		 }
     }

}
