package aspiration;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.interfaces.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import aspiration.events.ElementalEggBirdNest;
import aspiration.events.TheDarkMirror;
import aspiration.relics.AnachronicSnailShell;
import aspiration.relics.BabyByrd;
import aspiration.relics.BursterCore;
import aspiration.relics.Contagion;
import aspiration.relics.EnhancedActuators;
import aspiration.relics.FetidBarrel;
import aspiration.relics.FrozenJewel;
import aspiration.relics.Headhunter;
import aspiration.relics.Legacy_Headhunter;
import aspiration.relics.MysteriousAuxiliaryCore;
import aspiration.relics.PoetsPen;
import aspiration.relics.PoetsPen_weak;
import aspiration.relics.HummingbirdHeart;
import aspiration.relics.InfernalBlood;
import aspiration.relics.RingOfOuroboros;
import aspiration.relics.SneckoTail;
import aspiration.relics.StickyExplosives;
import aspiration.relics.SupercapacitiveCoin;
import aspiration.relics.VileToxins;
import aspiration.relics.abstracts.AspirationRelic;

import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class Aspiration implements
        PostInitializeSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        PostPowerApplySubscriber,
        PostDungeonInitializeSubscriber
{
	public static final Logger logger = LogManager.getLogger(Aspiration.class.getName());
    private static SpireConfig modConfig = null;
    public static SpireConfig otherSaveData = null;
    public static TextureAtlas powerAtlas;

    public static void initialize()
    {
        BaseMod.subscribe(new Aspiration());

        try {
            Properties defaults = new Properties();
            defaults.put("WeakPoetsPen", Boolean.toString(false));
            modConfig = new SpireConfig("Aspiration", "Config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String assetPath(String path)
    {
        return "aspirationAssets/" + path;
    }

    public static boolean weakPoetsPenEnabled()
    {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("WeakPoetsPen");
    }
    
    public static void loadOtherData()
    {
        logger.info("Loading Other Save Data");
        try {
            otherSaveData = new SpireConfig("Aspiration", "OtherSaveData");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData()
    {
        logger.info("Saving Data");
        try {
            if (otherSaveData == null) {
                otherSaveData = new SpireConfig("Aspiration", "OtherSaveData");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearData()
    {
        logger.info("Clearing Saved Data");
        try {
            SpireConfig config = new SpireConfig("Aspiration", "SaveData");
            config.clear();
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void receivePostInitialize() {
    	loadOtherData();

        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton aspirationBtn = new ModLabeledToggleButton("Make the Poet's Pen boss relic weaker.", 350, 600, Settings.CREAM_COLOR, FontHelper.charDescFont, weakPoetsPenEnabled(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("WeakPoetsPen", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(aspirationBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("img/UI/modBadge.png")), "Aspiration", "Erasels", "A mod, boyo.", settingsPanel);

    	
        BaseMod.addEvent(TheDarkMirror.ID, TheDarkMirror.class);
        BaseMod.addEvent(ElementalEggBirdNest.ID, ElementalEggBirdNest.class, Exordium.ID);
        
        this.loadAudio();
        powerAtlas = new TextureAtlas(Gdx.files.internal(assetPath("img/powers/powers.atlas")));
    }

    @SuppressWarnings("unchecked")
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
    	BaseMod.addRelic(new PoetsPen_weak(), RelicType.SHARED);
    	BaseMod.addRelic(new StickyExplosives(), RelicType.SHARED);
    	BaseMod.addRelic(new FrozenJewel(), RelicType.SHARED);
    	
    	//Special relics
    	BaseMod.addRelic(new BabyByrd(), RelicType.SHARED);
    	
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
        BaseMod.addRelic(new FetidBarrel(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath("loc/aspiration-EventStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("loc/aspiration-RelicStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("loc/aspiration-PowerStrings.json"));
    }
    
    @Override
    public void receivePostDungeonInitialize()
    {
        if (weakPoetsPenEnabled()) {
            if (AbstractDungeon.bossRelicPool.removeIf(r -> r.equals(PoetsPen.ID))) {
                logger.info(PoetsPen.ID + " removed.");
            }
        } else {
        	if (AbstractDungeon.bossRelicPool.removeIf(r -> r.equals(PoetsPen_weak.ID))) {
                logger.info(PoetsPen_weak.ID + " removed.");
            }
        }
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