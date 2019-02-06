package aspiration.patches;

import aspiration.ui.campfire.ReadArtOfWarOption;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ArtOfWar;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.rooms.CampfireUI",
        method = "initializeButtons"
)
public class InitializeButtonsPatches {

    public static void Postfix(Object __instance) { // "This is not mine and I think this patch sucks too." - BD
        CampfireUI campfire = (CampfireUI)__instance;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>) ReflectionHacks.getPrivate(campfire, CampfireUI.class, "buttons");
            if(AbstractDungeon.player.hasRelic(ArtOfWar.ID)) {
                campfireButtons.add(new ReadArtOfWarOption());
                campfireButtons.get(campfireButtons.size() - 1).setPosition((550) * Settings.scale, (721) * Settings.scale);
            }

        } catch (SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
