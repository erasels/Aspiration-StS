package aspiration.patches.Unique;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Ectoplasm;

import aspiration.relics.common.SupercapacitiveCoin;

@SpirePatch(
        clz=AbstractPlayer.class,
        method="gainGold",
        paramtypez={
        		int.class
        }
)
public class GainGold_postfix_SupercapacitiveCoin {
	 public static void Postfix(AbstractPlayer __instance, int amount)
     {
		 if (AbstractDungeon.player.hasRelic(SupercapacitiveCoin.ID) && AbstractDungeon.player.hasRelic(Ectoplasm.ID))
		 {
			 AbstractDungeon.player.getRelic(SupercapacitiveCoin.ID).flash();
			 AbstractDungeon.player.getRelic(SupercapacitiveCoin.ID).getUpdatedDescription();
		 }
     }

}