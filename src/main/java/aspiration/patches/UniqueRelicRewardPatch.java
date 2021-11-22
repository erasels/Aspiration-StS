package aspiration.patches;

import aspiration.util.RelicUtils;
import aspiration.relics.rare.SneckoTail;
import aspiration.relics.special.Stabinomicon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;


@SpirePatch(clz = AbstractRoom.class, method = "addRelicToRewards", paramtypez = {AbstractRelic.RelicTier.class})
public class UniqueRelicRewardPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractRoom __instance, AbstractRelic.RelicTier rt) {
        if (__instance instanceof MonsterRoomElite) {
            for (AbstractMonster m : __instance.monsters.monsters) {
                if (m.id.equals(BookOfStabbing.ID)) {
                    if (!AbstractDungeon.player.hasRelic(Stabinomicon.ID)) {
                        if (AbstractDungeon.relicRng.random(19) == 0) {
                            for (RewardItem rw : __instance.rewards) {
                                if (rw.type == RewardItem.RewardType.RELIC) {
                                    Stabinomicon sb = new Stabinomicon();
                                    __instance.rewards.set(__instance.rewards.indexOf(rw), new RewardItem(sb));
                                    break;
                                }
                            }
                        }
                    }
                }

                if (m.id.equals(Snecko.ID) && RelicUtils.deckDescriptionSearch(new String[]{PoisonPower.NAME, PoisonPower.POWER_ID})) {
                    if (!AbstractDungeon.player.hasRelic(SneckoTail.ID)) {
                        if (AbstractDungeon.relicRng.random(19) == 0) {
                            for (RewardItem rw : __instance.rewards) {
                                if (rw.type == RewardItem.RewardType.RELIC) {
                                    SneckoTail st = new SneckoTail();
                                    __instance.rewards.set(__instance.rewards.indexOf(rw), new RewardItem(st));
                                    RelicUtils.removeRelicFromPool(st);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
