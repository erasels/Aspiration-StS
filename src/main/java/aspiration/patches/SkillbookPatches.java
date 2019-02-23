package aspiration.patches;

import aspiration.Aspiration;
import aspiration.relics.skillbooks.SkillbookRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SkillbookPatches
{
    @SpirePatch(
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
    }

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

    private static Field PAD_X_f = null;

    @SpirePatch(
            clz=CardRewardScreen.class,
            method="placeCards"
    )
    public static class PositionCards
    {
        public static void Prefix(CardRewardScreen __instance, float x, float y) {
            if (__instance.rewardGroup.size() > 4) {
                float PAD_X = 40.0f * Settings.scale;
                try {
                    if (PAD_X_f == null) {
                        PAD_X_f = CardRewardScreen.class.getDeclaredField("PAD_X");
                        PAD_X_f.setAccessible(true);
                    }
                    PAD_X = PAD_X_f.getFloat(null);
                } catch (IllegalAccessException | NoSuchFieldException ignored) {
                }
                if (__instance.rewardGroup.size() == 5)
                {
                    __instance.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 1.5f;
                    __instance.rewardGroup.get(1).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 0.75f;
                    __instance.rewardGroup.get(2).target_x = Settings.WIDTH / 2.0f;
                    __instance.rewardGroup.get(3).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 0.75f;
                    __instance.rewardGroup.get(4).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 1.5f;
                    __instance.rewardGroup.get(0).target_y = y;
                    __instance.rewardGroup.get(1).target_y = y;
                    __instance.rewardGroup.get(2).target_y = y;
                    __instance.rewardGroup.get(3).target_y = y;
                    __instance.rewardGroup.get(4).target_y = y;
                }
                else if (__instance.rewardGroup.size() == 6)
                {
                    __instance.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 1.5f;
                    __instance.rewardGroup.get(1).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 0.9f;
                    __instance.rewardGroup.get(2).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 0.3f;
                    __instance.rewardGroup.get(3).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 0.3f;
                    __instance.rewardGroup.get(4).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 0.9f;
                    __instance.rewardGroup.get(5).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 1.5f;
                    __instance.rewardGroup.get(0).target_y = y;
                    __instance.rewardGroup.get(1).target_y = y;
                    __instance.rewardGroup.get(2).target_y = y;
                    __instance.rewardGroup.get(3).target_y = y;
                    __instance.rewardGroup.get(4).target_y = y;
                    __instance.rewardGroup.get(5).target_y = y;
                }
                else if (__instance.rewardGroup.size() == 7)
                {
                    __instance.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 1.5f;
                    __instance.rewardGroup.get(1).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 1.0f;
                    __instance.rewardGroup.get(2).target_x = Settings.WIDTH / 2.0f - (AbstractCard.IMG_WIDTH + PAD_X) * 0.5f;
                    __instance.rewardGroup.get(3).target_x = Settings.WIDTH / 2.0f;
                    __instance.rewardGroup.get(4).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 0.5f;
                    __instance.rewardGroup.get(5).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 1.0f;
                    __instance.rewardGroup.get(6).target_x = Settings.WIDTH / 2.0f + (AbstractCard.IMG_WIDTH + PAD_X) * 1.5f;
                    __instance.rewardGroup.get(0).target_y = y;
                    __instance.rewardGroup.get(1).target_y = y;
                    __instance.rewardGroup.get(2).target_y = y;
                    __instance.rewardGroup.get(3).target_y = y;
                    __instance.rewardGroup.get(4).target_y = y;
                    __instance.rewardGroup.get(5).target_y = y;
                    __instance.rewardGroup.get(6).target_y = y;
                }
            }
        }
    }
}