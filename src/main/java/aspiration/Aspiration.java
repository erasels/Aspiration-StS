package aspiration;

import aspiration.events.CultistTraining;
import aspiration.relics.*;
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
import aspiration.relics.abstracts.AspirationRelic;

import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.megacrit.cardcrawl.relics.CultistMask;
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
            defaults.put("uncommonNostalgia", Boolean.toString(false));
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
    
    public static boolean uncommonNostalgia()
    {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("uncommonNostalgia");
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
        ModLabeledToggleButton PPBtn = new ModLabeledToggleButton("Make the Poet's Pen boss relic weaker.", 350, 700, Settings.CREAM_COLOR, FontHelper.charDescFont, weakPoetsPenEnabled(), settingsPanel, l -> {},
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
        settingsPanel.addUIElement(PPBtn);
        
        ModLabeledToggleButton nostalgiaBtn = new ModLabeledToggleButton("Make Nostalgia an Uncommon relic. (instead of Shop)", 350, 650, Settings.CREAM_COLOR, FontHelper.charDescFont, uncommonNostalgia(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("uncommonNostalgia", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(nostalgiaBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("img/UI/modBadge.png")), "Aspiration", "Erasels", "A mod, boyo.", settingsPanel);

    	
        BaseMod.addEvent(TheDarkMirror.ID, TheDarkMirror.class);
        BaseMod.addEvent(ElementalEggBirdNest.ID, ElementalEggBirdNest.class, Exordium.ID);
        BaseMod.addEvent(CultistTraining.ID, CultistTraining.class, TheCity.ID);
        
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
    	BaseMod.addRelic(new FetidBarrel(), RelicType.SHARED);
    	BaseMod.addRelic(new StickyExplosives(), RelicType.SHARED);
    	BaseMod.addRelic(new FrozenJewel(), RelicType.SHARED);
    	BaseMod.addRelic(new EvolvingReagent(), RelicType.SHARED);
    	BaseMod.addRelic(new Lifesprig(), RelicType.SHARED);
    	BaseMod.addRelic(new RitualDagger(), RelicType.SHARED);
    	BaseMod.addRelic(new KaomsHeart(), RelicType.SHARED);
    	BaseMod.addRelic(new Nostalgia(false), RelicType.SHARED);
    	BaseMod.addRelic(new Nostalgia(true), RelicType.SHARED);
        BaseMod.addRelic(new TrainingWeights(), RelicType.SHARED);
    	
    	//Special relics
    	BaseMod.addRelic(new BabyByrd(), RelicType.SHARED);
    	BaseMod.addRelic(new RitualStick(), RelicType.SHARED);
    	
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
        
        if (uncommonNostalgia()) {
            if (AbstractDungeon.shopRelicPool.removeIf(r -> r.equals(Nostalgia.ID))) {
                logger.info(Nostalgia.ID + " (Shop) removed.");
            }
        } else {
        	if (AbstractDungeon.uncommonRelicPool.removeIf(r -> r.equals(Nostalgia.ID))) {
                logger.info(Nostalgia.ID + " (Uncommon) removed.");
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