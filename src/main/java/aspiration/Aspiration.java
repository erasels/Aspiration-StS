package aspiration;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.interfaces.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import aspiration.events.TheDarkMirror;
import aspiration.relics.AnachronicSnailShell;
import aspiration.relics.BursterCore;
import aspiration.relics.Contagion;
import aspiration.relics.EnhancedActuators;
import aspiration.relics.Headhunter;
import aspiration.relics.Legacy_Headhunter;
import aspiration.relics.MysteriousAuxiliaryCore;
import aspiration.relics.PoetsPen;
import aspiration.relics.HummingbirdHeart;
import aspiration.relics.InfernalBlood;
import aspiration.relics.RingOfOuroboros;
import aspiration.relics.SneckoTail;
import aspiration.relics.SupercapacitiveCoin;
import aspiration.relics.VileToxins;
import aspiration.relics.abstracts.AspirationRelic;

import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;
import java.util.HashMap;
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
        
        this.loadAudio();
    }

    public void loadAudio() {
		HashMap<String, Sfx> map = (HashMap<String, Sfx>)ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put("ASP-BLOODPUMP", new Sfx(assetPath("audio/BloodPump.ogg"), false));
    }
    
    @Override
    public void receiveEditRelics()
    {
    	//"Normal" Relics
    	BaseMod.addRelic(new HummingbirdHeart(), RelicType.SHARED);
    	BaseMod.addRelic(new Legacy_Headhunter(), RelicType.SHARED);
    	BaseMod.addRelic(new Headhunter(), RelicType.SHARED);
    	BaseMod.addRelic(new AnachronicSnailShell(), RelicType.SHARED);
    	BaseMod.addRelic(new SupercapacitiveCoin(), RelicType.SHARED);
    	BaseMod.addRelic(new PoetsPen(), RelicType.SHARED);
    	
    	//Starter Upgrades
    	BaseMod.addRelic(new RingOfOuroboros(), RelicType.SHARED);
    	BaseMod.addRelic(new InfernalBlood(), RelicType.SHARED);
    	BaseMod.addRelic(new BursterCore(), RelicType.SHARED);
    	
    	//Defect Only
    	BaseMod.addRelic(new EnhancedActuators(), RelicType.BLUE);
    	BaseMod.addRelic(new MysteriousAuxiliaryCore(), RelicType.BLUE);
    	
    	//If poison card in deck
        BaseMod.addRelic(new VileToxins(), RelicType.SHARED);
        BaseMod.addRelic(new Contagion(), RelicType.SHARED);
        BaseMod.addRelic(new SneckoTail(), RelicType.SHARED);
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