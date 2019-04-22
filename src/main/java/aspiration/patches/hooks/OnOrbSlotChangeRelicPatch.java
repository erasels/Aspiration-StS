package aspiration.patches.hooks;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.OnOrbSlotChange;

//Ausbaubar, ByRef verwenden um Amount ver�ndern zu k�nnen z.B. "Gain 1 additional orb slots when increasing orbslots" oder so.
public class OnOrbSlotChangeRelicPatch {
	@SpirePatch(
			clz = AbstractPlayer.class, 
			method = "increaseMaxOrbSlots", 
			paramtypez = { 
					int.class, 
					boolean.class 
					}
			)
	public static class IncreaseMaxOrbSlots {
		@SpireInsertPatch(
                rloc=0
        )
		public static void Insert(AbstractPlayer __instance, int amount, boolean playSfx) {
			for (AbstractRelic r : AbstractDungeon.player.relics) {
				if (r instanceof OnOrbSlotChange) {
					((OnOrbSlotChange) r).onOrbSlotChange(amount, false);
				}
			}
		}
	}
	
	@SpirePatch(
			clz = AbstractPlayer.class, 
			method = "decreaseMaxOrbSlots", 
			paramtypez = { 
					int.class
					}
			)
	public static class DecreaseMaxOrbSlots {
		@SpireInsertPatch(
                rloc=0
        )
		public static void Insert(AbstractPlayer __instance, int amount) {
			for (AbstractRelic r : AbstractDungeon.player.relics) {
				if (r instanceof OnOrbSlotChange) {
					((OnOrbSlotChange) r).onOrbSlotChange(amount, true);
				}
			}
		}
	}

}
