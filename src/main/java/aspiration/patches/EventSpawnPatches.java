package aspiration.patches;

import aspiration.events.MeetingTheSilent;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EventSpawnPatches {
    @SpirePatch(
            clz=AbstractDungeon.class,
            method="initializeCardPools"
    )
    public static class MeetingTheSilentDuplicateRemoval {
        public static void Prefix(AbstractDungeon __instance) {
            if (AbstractDungeon.player instanceof TheSilent) {
                AbstractDungeon.eventList.remove(MeetingTheSilent.ID);
                AbstractDungeon.shrineList.remove(MeetingTheSilent.ID);
            }
        }
    }
}
