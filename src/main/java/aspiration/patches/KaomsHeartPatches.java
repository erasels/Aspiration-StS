package aspiration.patches;

import aspiration.relics.special.KaomsHeart;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class KaomsHeartPatches
{
	@SpirePatch (
			clz=AbstractRelic.class,
	        method="obtain"
	)
	public static class norm_obtain {
		@SpirePrefixPatch
		public static SpireReturn<?> Prefix(AbstractRelic __instance)
	    {
	        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(KaomsHeart.ID)) {
	        	__instance.isDone = true;
	        	__instance.isObtained = false;
	        	__instance.discarded = true;
	        	AbstractDungeon.player.getRelic(KaomsHeart.ID).flash();
	        	return SpireReturn.Return(null);
	        }
	        return SpireReturn.Continue();
	    }
	}
    
	@SpirePatch (
			clz=AbstractRelic.class,
	        method="instantObtain",
	        		paramtypez = { }
	)
	public static class norm_instantObtain {
		@SpirePrefixPatch
		public static SpireReturn<?> Prefix(AbstractRelic __instance)
	    {
	        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(KaomsHeart.ID)) {
	        	__instance.isDone = true;
	        	__instance.isObtained = false;
	        	__instance.discarded = true;
	        	AbstractDungeon.player.getRelic(KaomsHeart.ID).flash();
	        	return SpireReturn.Return(null);
	        }
	        return SpireReturn.Continue();
	    }
	}
	
	@SpirePatch (
			clz=AbstractRelic.class,
	        method="instantObtain",
			paramtypez = { 
					AbstractPlayer.class,
					int.class, 
					boolean.class 
					}
	)
	public static class param_instantObtain {
		@SpirePrefixPatch
		public static SpireReturn<?> Prefix(AbstractRelic __instance, AbstractPlayer p, int slot, boolean callOnEquip)
	    {
	        if (p != null && p.hasRelic(KaomsHeart.ID)) {
	        	__instance.isDone = true;
	        	__instance.isObtained = false;
	        	__instance.discarded = true;
	        	p.getRelic(KaomsHeart.ID).flash();
	        	return SpireReturn.Return(null);
	        }
	        return SpireReturn.Continue();
	    }
	}

	/*@SpirePatch(clz = ShopScreen.class, method = "init")
	public static class SetGoldToPlayGoldPostfix {
		public static void PostFix(ShopScreen __instance, final ArrayList<AbstractCard> coloredCards, final ArrayList<AbstractCard> colorlessCards) {
			for (final StoreRelic r : (ArrayList<StoreRelic>) ReflectionHacks.getPrivate(__instance, ShopScreen.class, "relics")) {
				if(r.relic.relicId.equals(KaomsHeart.ID) && AbstractDungeon.player != null) {
					r.price = AbstractDungeon.player.gold;
				}
			}
		}
	}*/
}