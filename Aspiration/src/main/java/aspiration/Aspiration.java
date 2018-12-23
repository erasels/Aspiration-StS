package aspiration;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import aspiration.events.TheDarkMirror;
import aspiration.relics.VileToxins;
import aspiration.relics.abstracts.AspirationRelic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class Aspiration implements
        PostInitializeSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        PostPowerApplySubscriber
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
    public void receiveEditRelics()
    {
        BaseMod.addRelic(new VileToxins(), RelicType.GREEN);
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath("loc/aspiration-EventStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("loc/aspiration-RelicStrings.json"));
    }
    
    @Override
    public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof  AspirationRelic) {
                ((AspirationRelic)r).onApplyPower(p, target, source);
            }
        }
    }
}