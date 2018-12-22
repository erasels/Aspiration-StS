package aspiration;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import aspiration.events.TheDarkMirror;

import com.megacrit.cardcrawl.localization.*;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class Aspiration implements
        PostInitializeSubscriber,
        EditStringsSubscriber
{

    private static SpireConfig modConfig = null;


    public static void initialize()
    {
        BaseMod.subscribe(new Aspiration());

        try {
            Properties defaults = new Properties();
            defaults.put("startingAspiration", Boolean.toString(false));
            modConfig = new SpireConfig("Aspiration", "Config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String assetPath(String path)
    {
        return "aspirationAssets/" + path;
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addEvent(TheDarkMirror.ID, TheDarkMirror.class);
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath("loc/aspiration-EventStrings.json"));
    }
}