package aspiration.patches.Unique;

import aspiration.Aspiration;
import aspiration.relics.skillbooks.SkillbookRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;

import java.util.ArrayList;

public class SkillbookPatches
{
    /*@SpirePatch(
            clz=RewardItem.class,
            method="claimReward"
    )
    public static class ClaimReward
    {
        public static void Postfix(RewardItem __instance)
        {
            if (__instance.type == RewardItem.RewardType.CARD && SkillbookRelic.hasSkillbookRelic(AbstractDungeon.player)) {
                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if(r instanceof SkillbookRelic) {
                        r.flash();
                    }
                }
            }
        }
    }*/

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="getRewardCards"
    )
    public static class AddCardReward
    {
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result)
        {
            if(Aspiration.skillbookCardpool()) {
                if (SkillbookRelic.hasSkillbookRelic(AbstractDungeon.player)) {
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        if (r instanceof SkillbookRelic) {
                            r.flash();
                        }
                    }

                    AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
                    AbstractCard card = new VoidCard();
                    boolean dupeTest = true;

                    while (dupeTest) {
                        dupeTest = false;
                        if (AbstractDungeon.player.hasRelic(PrismaticShard.ID)) {
                            card = CardLibrary.getAnyColorCard(rarity);
                        } else {
                            card = AbstractDungeon.getCard(rarity);
                        }
                        for (AbstractCard c : __result) {
                            if (c.cardID.equals(card.cardID)) {
                                dupeTest = true;
                                break;
                            }
                        }
                    }

                    if (card != null) {
                        __result.add(card.makeCopy());
                    }
                }
            }

            return __result;
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="initializeCardPools"
    )
    public static class CardpoolInitFix {
        public static void Postfix(AbstractDungeon __instance) {
            if(Aspiration.skillbookCardpool() && AbstractDungeon.player != null) {
                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if(r instanceof SkillbookRelic) {
                        ((SkillbookRelic) r).modifyCardPool();
                    }
                }
            }
        }
    }
}