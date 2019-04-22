package aspiration.patches.Unique;

import aspiration.relics.special.Stabinomicon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;


@SpirePatch(clz = AbstractRoom.class, method = "addRelicToRewards", paramtypez = {AbstractRelic.RelicTier.class})
public class BookOfStabbingRelicRewardPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractRoom __instance, AbstractRelic.RelicTier rt) {
        if (__instance instanceof MonsterRoomElite) {
            for (AbstractMonster m : __instance.monsters.monsters) {
                if (m.id.equals(BookOfStabbing.ID)) {
                    if (AbstractDungeon.relicRng.random(19) == 0) {
                        if (!AbstractDungeon.player.hasRelic(Stabinomicon.ID)) {
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
            }
        }
    }
}
