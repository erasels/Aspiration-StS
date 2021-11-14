package aspiration;

import aspiration.Utility.RelicUtils;
import aspiration.cards.Runesmith.UnbridledHammer;
import aspiration.cards.blue.Polymerization;
import aspiration.cards.green.Lunge;
import aspiration.events.CultistTraining;
import aspiration.events.ElementalEggBirdNest;
import aspiration.events.MeetingTheSilent;
import aspiration.events.TheDarkMirror;
import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.boss.*;
import aspiration.relics.common.*;
import aspiration.relics.crossovers.EmptySkull;
import aspiration.relics.crossovers.MobileForge;
import aspiration.relics.crossovers.PocketMushroom;
import aspiration.relics.crossovers.TrustyKnives;
import aspiration.relics.rare.*;
import aspiration.relics.shop.Earplugs;
import aspiration.relics.shop.Nostalgia;
import aspiration.relics.skillbooks.*;
import aspiration.relics.special.*;
import aspiration.relics.uncommon.*;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import blackrusemod.patches.AbstractCardEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

@SpireInitializer
public class Aspiration implements
        PostInitializeSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        EditCardsSubscriber,
        PostPowerApplySubscriber,
        PostDungeonInitializeSubscriber,
        AddAudioSubscriber,
        EditKeywordsSubscriber,
        RelicGetSubscriber,
        PostUpdateSubscriber,
        PotionGetSubscriber
{
	public static final Logger logger = LogManager.getLogger(Aspiration.class.getName());
    private static SpireConfig modConfig = null;
    public static SpireConfig otherSaveData = null;
    public static TextureAtlas powerAtlas;
    public static final int SKILLBOOK_SPAWN_AMOUNT = 3;
    public static boolean dsixTriggered;

    // Crossover checks
    public static final boolean hasMarisa;
    public static final boolean hasServant;
    public static final boolean hasBeaked;
    public static final boolean hasRunesmith;
    public static final boolean hasReplay;
    public static final boolean hasConspire;
    public static final boolean hasInfinite;
    public static final boolean hasScribe;
    public static final boolean hasAnimator;
    public static final boolean hasHubris;
    public static final boolean hasBard;

    static {
        hasMarisa = Loader.isModLoaded("TS05_Marisa");
        if (hasMarisa) {
            logger.info("Detected Character: Marisa");
        }
        hasServant = Loader.isModLoaded("BlackRuseMod");
        if (hasServant) {
            logger.info("Detected Character: Servant");
        }
        hasBeaked = Loader.isModLoaded("beakedthecultist-sts");
        if (hasBeaked) {
            logger.info("Detected Character: Beaked");
        }
        hasRunesmith = Loader.isModLoaded("therunesmith");
        if (hasRunesmith) {
            logger.info("Detected Character: Runesmith");
        }
        hasReplay = Loader.isModLoaded("ReplayTheSpireMod");
        if (hasReplay) {
            logger.info("Detected Mod: Replay The Spire");
        }
        hasConspire = Loader.isModLoaded("conspire");
        if (hasConspire) {
            logger.info("Detected Mod: Conspire");
        }
        hasInfinite = Loader.isModLoaded("infinitespire");
        if (hasInfinite) {
            Aspiration.logger.info("Detected Mod: Infinite Spire");
        }
        hasScribe = Loader.isModLoaded("thescribe");
        if (hasScribe) {
            Aspiration.logger.info("Detected Character: Scribe");
        }
        hasAnimator = Loader.isModLoaded("eatyourbeetsvg-theanimator");
        if (hasAnimator) {
            Aspiration.logger.info("Detected Character: The Animator");
        }
        hasHubris = Loader.isModLoaded("hubris");
        if (hasHubris) {
            Aspiration.logger.info("Detected Mod: Hubris");
        }
        hasBard = Loader.isModLoaded("bard");
        if (hasBard) {
            Aspiration.logger.info("Detected Character: Bard");
        }
    }

    public static void initialize()
    {
        BaseMod.subscribe(new Aspiration());

        try {
            Properties defaults = new Properties();
            defaults.put("WeakPoetsPen", Boolean.toString(true));
            defaults.put("SkillbookCardpool", Boolean.toString(true));
            defaults.put("SpawnRNG", Boolean.toString(false));
            defaults.put("HoverSpeed", Boolean.toString(false));
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

    public static boolean skillbookCardpool()
    {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("SkillbookCardpool");
    }

    public static boolean SpawnRNG()
    {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("SpawnRNG");
    }

    public static boolean hoverSpeed()
    {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("HoverSpeed");
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

        UIStrings buttonStrings = CardCrawlGame.languagePack.getUIString("aspiration:ModButtonText");
        String[] TEXT = buttonStrings.TEXT;

        ModPanel settingsPanel = new ModPanel();

        int yPos = 700;

        ModLabeledToggleButton PPBtn = new ModLabeledToggleButton(TEXT[0], 350, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, weakPoetsPenEnabled(), settingsPanel, l -> {},
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
        yPos -= 50;

        ModLabeledToggleButton skillbookBtn = new ModLabeledToggleButton(TEXT[2], 350, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, skillbookCardpool(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("SkillbookCardpool", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(skillbookBtn);
        yPos -= 50;

        ModLabeledToggleButton rngButton = new ModLabeledToggleButton(TEXT[3], 350, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, SpawnRNG(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("SpawnRNG", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(rngButton);

        ModLabeledToggleButton hoverButton = new ModLabeledToggleButton(TEXT[5], 350, 250, Settings.BLUE_TEXT_COLOR, FontHelper.charDescFont, hoverSpeed(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("HoverSpeed", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(hoverButton);

        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("img/UI/modBadge.png")), "Aspiration", "Erasels", "A mod, boyo.", settingsPanel);


        BaseMod.addEvent(ElementalEggBirdNest.ID, ElementalEggBirdNest.class, Exordium.ID);
        BaseMod.addEvent(CultistTraining.ID, CultistTraining.class, TheCity.ID);

        BaseMod.addEvent(new AddEventParams.Builder(TheDarkMirror.ID, TheDarkMirror.class).eventType(EventUtils.EventType.NORMAL).spawnCondition(() -> AbstractDungeon.player.hasRelic(PrismaticShard.ID)).dungeonID(TheCity.ID).create());
        BaseMod.addEvent(new AddEventParams.Builder(MeetingTheSilent.ID, MeetingTheSilent.class).spawnCondition(() -> !(AbstractDungeon.player instanceof TheSilent)).eventType(EventUtils.EventType.SHRINE).create());

        powerAtlas = new TextureAtlas(Gdx.files.internal(assetPath("img/powers/powers.atlas")));
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("aspiration:Bloodpump", assetPath("audio/BloodPump.ogg"));
    }
    
    @Override
    public void receiveEditRelics()
    {
    	//"Normal" Relics
    	BaseMod.addRelic(new HummingbirdHeart(), RelicType.SHARED);
    	BaseMod.addRelic(new Legacy_Headhunter(), RelicType.SHARED);
    	BaseMod.addRelic(new Headhunter(), RelicType.SHARED);
    	BaseMod.addRelic(new AnachronicSnailShell(), RelicType.SHARED);
    	//BaseMod.addRelic(new SupercapacitiveCoin(), RelicType.SHARED);
    	BaseMod.addRelic(new PoetsPen(), RelicType.SHARED);
    	BaseMod.addRelic(new PoetsPen_weak(), RelicType.SHARED);
    	BaseMod.addRelic(new FetidBarrel(), RelicType.SHARED);
    	//BaseMod.addRelic(new StickyExplosives(), RelicType.SHARED);
        //BaseMod.addRelic(new StickyExplosives_weak(), RelicType.SHARED);
    	BaseMod.addRelic(new FrozenJewel(), RelicType.SHARED);
    	//BaseMod.addRelic(new EvolvingReagent(), RelicType.SHARED);
    	BaseMod.addRelic(new Lifesprig(), RelicType.SHARED);
    	BaseMod.addRelic(new RitualDagger(), RelicType.SHARED);
    	BaseMod.addRelic(new KaomsHeart(), RelicType.SHARED);
        BaseMod.addRelic(new KaomsHeart_nothing(), RelicType.SHARED);
    	BaseMod.addRelic(new Nostalgia(), RelicType.SHARED);
        //BaseMod.addRelic(new TrainingWeights(), RelicType.SHARED);
        BaseMod.addRelic(new SeaSaltIceCream(), RelicType.SHARED);
        BaseMod.addRelic(new Stellarator(), RelicType.SHARED);
        BaseMod.addRelic(new FutureDiary(), RelicType.SHARED);
        //Both of these are boring TODO: Make them less so.
        //BaseMod.addRelic(new HeadsmansAxe(), RelicType.SHARED);
        //BaseMod.addRelic(new HangmansNoose(), RelicType.SHARED);
        BaseMod.addRelic(new RandomNobGenerator(), RelicType.SHARED);
        BaseMod.addRelic(new FaultyCoupler(), RelicType.SHARED);
        BaseMod.addRelic(new ChemicalBlood(), RelicType.SHARED);
        BaseMod.addRelic(new SecretTechniqueScroll(), RelicType.SHARED);
        BaseMod.addRelic(new HiddenCompartment(), RelicType.SHARED);
        BaseMod.addRelic(new HatOfInfinitePower(), RelicType.SHARED);
        BaseMod.addRelic(new RunicSpoon(), RelicType.SHARED);
        BaseMod.addRelic(new DSix(), RelicType.SHARED);
        BaseMod.addRelic(new MechanicalEye(), RelicType.SHARED);
        BaseMod.addRelic(new Mageblood(), RelicType.SHARED);

        //Vanilla skillbooks
        BaseMod.addRelic(new IroncladSkillbook(), RelicType.SHARED);
        BaseMod.addRelic(new DefectSkillbook(), RelicType.SHARED);
        BaseMod.addRelic(new SilentSkillbook(), RelicType.SHARED);
        BaseMod.addRelic(new WatcherSkillbook(), RelicType.SHARED);
    	
    	//Special relics
    	BaseMod.addRelic(new BabyByrd(), RelicType.SHARED);
    	BaseMod.addRelic(new RitualStick(), RelicType.SHARED);
        BaseMod.addRelic(new ArtOfWarUpgrade(), RelicType.SHARED);
        BaseMod.addRelic(new Stabinomicon(), RelicType.SHARED);
    	
    	//Starter Upgrades
    	//BaseMod.addRelic(new RingOfOuroboros(), RelicType.SHARED);
    	//BaseMod.addRelic(new InfernalBlood(), RelicType.SHARED);
    	BaseMod.addRelic(new BursterCore(), RelicType.SHARED);
    	
    	//Defect Only
    	BaseMod.addRelic(new EnhancedActuators(), RelicType.BLUE);
    	BaseMod.addRelic(new MysteriousAuxiliaryCore(), RelicType.BLUE);
    	
    	//If poison card in deck
        BaseMod.addRelic(new VileToxins(), RelicType.SHARED);
        //BaseMod.addRelic(new Contagion(), RelicType.SHARED);
        BaseMod.addRelic(new SneckoTail(), RelicType.SHARED);

        //Crossover
        if(hasMarisa) {
            BaseMod.addRelicToCustomPool(new PocketMushroom(), ThMod.patches.AbstractCardEnum.MARISA_COLOR);
            BaseMod.addRelic(new MarisaSkillbook(), RelicType.SHARED);
        }
        if(hasServant) {
            BaseMod.addRelicToCustomPool(new TrustyKnives(), AbstractCardEnum.SILVER);
            BaseMod.addRelic(new ServantSkillbook(), RelicType.SHARED);
        }
        if(hasBeaked) {
            BaseMod.addRelic(new EmptySkull(), RelicType.SHARED);
            BaseMod.addRelic(new BeakedSkillbook(), RelicType.SHARED);
        }
        if(hasRunesmith) {
            BaseMod.addRelic(new MobileForge(), RelicType.SHARED);
            BaseMod.addRelic(new RunesmithSkillbook(), RelicType.SHARED);
        }
        if(hasScribe) {
            BaseMod.addRelic(new ScribeSkillbook(), RelicType.SHARED);
        }
        if(hasBard) {
            BaseMod.addRelic(new BardSkillbook(), RelicType.SHARED);
            BaseMod.addRelicToCustomPool(new Earplugs(), Bard.Enums.COLOR);
        }
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Polymerization());
        BaseMod.addCard(new Lunge());

        if(hasRunesmith) {
            BaseMod.addCard(new UnbridledHammer());
        }
    }

    @Override
    public void receivePostUpdate() {
        if (!CardCrawlGame.isInARun()) return;
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            if (dsixTriggered) {
                dsixTriggered = false;
                DSix d = (DSix) AbstractDungeon.player.getRelic(DSix.ID);
                if(d != null) {
                    d.replaceRewards();
                }
            }
        }
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String keywordStrings = Gdx.files.internal(assetPath("loc/" + languageSupport() + "/" +"aspiration-KeywordStrings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = (Map)gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            // Keyword word = (Keyword)v;
            logger.info("Adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword("aspiration:", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    private String languageSupport()
    {
        switch (Settings.language) {
            case ZHS:
                return "zhs";
            default:
                return "eng";
        }
    }

    private void loadLocStrings(String language)
    {
        String path = "loc/" + language + "/";

        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath(path + "aspiration-EventStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath(path + "aspiration-UIStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath(path + "aspiration-PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath(path + "aspiration-RelicStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class, assetPath(path + "aspiration-OrbStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath(path + "aspiration-CardStrings.json"));
        BaseMod.loadCustomStringsFile(KeywordStrings.class, assetPath(path + "aspiration-KeywordStrings.json"));
        BaseMod.loadCustomStringsFile(BlightStrings.class, assetPath(path + "aspiration-BlightStrings.json"));
    }

    @Override
    public void receiveEditStrings()
    {
        String language = languageSupport();

        loadLocStrings("eng");
        if(!language.equals("eng"))
            loadLocStrings(language);
    }

    @Override
    public void receivePostDungeonInitialize()
    {
        dsixTriggered = false;
        if (weakPoetsPenEnabled()) {
            if (RelicUtils.removeRelicFromPool(PoetsPen.ID)) {
                logger.info(PoetsPen.ID + " removed.");
            }
        } else {
            if (RelicUtils.removeRelicFromPool(PoetsPen_weak.ID)) {
                logger.info(PoetsPen_weak.ID + " removed.");
            }
        }

        if (!SpawnRNG()) {
            if (RelicUtils.removeRelicFromPool(RandomNobGenerator.ID)) {
                logger.info(RandomNobGenerator.ID + " removed.");
            }
        }

        //Allow only SKILLBOOK_SPAWN_AMOUNT skillbooks into the boss relic pool
        Random rng = AbstractDungeon.relicRng;
        ArrayList<SkillbookRelic> skillbookPool = new ArrayList<>();
        for(String r : AbstractDungeon.bossRelicPool) {
            AbstractRelic tmp = RelicLibrary.getRelic(r);
            if(tmp instanceof SkillbookRelic) {
                skillbookPool.add((SkillbookRelic) tmp);
            }
        }
        for(int i = 0;i<SKILLBOOK_SPAWN_AMOUNT;i++) {
            if(skillbookPool.size()>0) {
                skillbookPool.remove(rng.random(skillbookPool.size()-1));
            }
        }
        if(!skillbookPool.isEmpty()) {
            if(AbstractDungeon.bossRelicPool.removeIf(relic -> RelicLibrary.getRelic(relic) instanceof SkillbookRelic && skillbookPool.contains(RelicLibrary.getRelic(relic)))) {
                skillbookPool.forEach(sb -> logger.info("Removed Skillbook: " + sb.name));
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

    @Override
    public void receiveRelicGet(AbstractRelic rel) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof  AspirationRelic) {
                ((AspirationRelic)r).onRelicGet(rel);
            }
        }
    }

    @Override
    public void receivePotionGet(AbstractPotion abstractPotion) {
        Mageblood r = (Mageblood) AbstractDungeon.player.getRelic(Mageblood.ID);
        if(r != null) {
            r.onPotionGet(abstractPotion);
        }
    }
}