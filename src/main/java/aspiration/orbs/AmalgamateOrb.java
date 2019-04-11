package aspiration.orbs;

import aspiration.Aspiration;
import aspiration.actions.unique.TriggerEvokeAction;
import aspiration.relics.uncommon.FaultyCoupler;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.mod.replay.orbs.*;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import conspire.orbs.Water;
import vexMod.orbs.GoldenLightning;

import java.util.ArrayList;
import java.util.HashMap;

public class AmalgamateOrb extends AbstractOrb {

    public static final String ORB_ID = "aspiration:Amalgamate";
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    private static final String NAME = orbString.NAME;

    private static final String[] DESC = orbString.DESCRIPTION;

    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    private static final int GOLD_PER_GOLDEN_LIGHTNING = 20; //smh vex if you won't define a constant I will

    private ArrayList<AbstractOrb> components;
    private ArrayList<Texture> textures;

    private HashMap<String, Integer> passiveValues = new HashMap<>();
    private HashMap<String, Integer> evokeValues = new HashMap<>();
    private int manaSparkAmount;
    public int goldCap = 0;
    public int goldGenerated = 0; //Theoretically, you can repeatedly amalgamate golden lightning to get infinite gold.

    public int crystalBonus;

    public AmalgamateOrb(ArrayList<AbstractOrb> components) {
        this.ID = ORB_ID;
        this.name = NAME;

        evokeAmount = baseEvokeAmount = 0;
        passiveAmount = basePassiveAmount = 0;

        goldCap = 0;

        this.components = new ArrayList<>();
        this.textures = new ArrayList<>();

        //TODO: See if this is even neccesary
        if(AbstractDungeon.player.hasRelic(FaultyCoupler.ID)) {
            if(components == null || components.isEmpty()) {
                this.components.add(new Lightning());
            }
        }

        if(components != null && !components.isEmpty()) {
            ArrayList<AbstractOrb> acceptableOrbs = OrbUtilityMethods.getOrbList(true);
            ArrayList<AbstractOrb> useableOrbs = new ArrayList<>();

            for (AbstractOrb orb : components) {
                for (AbstractOrb aorb : acceptableOrbs) {
                    if (aorb.getClass().isInstance(orb)) {
                        if (aorb.ID.equals(Dark.ORB_ID)) {
                            AbstractOrb darkOrb = orb.makeCopy();
                            darkOrb.evokeAmount = orb.evokeAmount;
                            useableOrbs.add(darkOrb);
                        } else {
                            useableOrbs.add(orb.makeCopy());
                        }
                    }
                }
            }

            if (!useableOrbs.isEmpty()) {
                for (AbstractOrb orb : useableOrbs) {
                    if (orb instanceof AmalgamateOrb) {
                        this.components.addAll(((AmalgamateOrb) orb).components);
                    } else if (!(orb instanceof EmptyOrbSlot)) {
                        this.components.add(orb);
                    }
                }

                for (AbstractOrb orb : this.components) {
                    try {
                        Texture orbTexture = (Texture) ReflectionHacks.getPrivate(orb, AbstractOrb.class, "img");
                        if (orbTexture != null) {
                            textures.add(orbTexture);
                        }
                    } catch (Exception e) {
                        Aspiration.logger.info(e);
                    }
                }
            }
        }

        updateDescription();

        angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    public AmalgamateOrb() {
        ID = ORB_ID;
        name = NAME;
        //img = IMG;

        evokeAmount = baseEvokeAmount = 0;
        passiveAmount = basePassiveAmount = 0;

        this.components = new ArrayList<>();
        this.textures = new ArrayList<>();

        updateDescription();

        angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() {
        applyFocus();

        if (components.isEmpty())
        {
            description = "";
        }
        else
        {
            manaSparkAmount = 0;
            goldCap = 0; //if gold cap > 0 adds gold cap message at end of orb description

            passiveValues.clear();
            evokeValues.clear();

            StringBuilder sb = new StringBuilder();
            for (AbstractOrb orb : components)
            {
                if (!passiveValues.containsKey(orb.ID))
                {
                    passiveValues.put(orb.ID, orb.passiveAmount);
                    evokeValues.put(orb.ID, orb.evokeAmount);
                }
                else
                {
                    passiveValues.put(orb.ID, passiveValues.get(orb.ID) + orb.passiveAmount);
                    evokeValues.put(orb.ID, evokeValues.get(orb.ID) + orb.evokeAmount);
                }
                if (Aspiration.hasMarisa && Aspiration.hasReplay) {
                    if (orb.ID.equals(ManaSparkOrb.ORB_ID)) {
                        manaSparkAmount++;
                    }
                }
                if (Aspiration.hasVex)
                {
                    if (orb.ID.equals(GoldenLightning.ORB_ID)) {
                        goldCap += GOLD_PER_GOLDEN_LIGHTNING; //smh hardcoded value with no constant
                    }
                }
            }

            sb.append(DESC[0]);

            boolean notFound;
            for (String key : passiveValues.keySet())
            {
                notFound = false;
                switch (key)
                {
                    case Dark.ORB_ID:
                        sb.append(DESC[2]).append(passiveValues.get(key)).append(DESC[1]);
                        break;
                    case Frost.ORB_ID:
                        sb.append(DESC[3]).append(passiveValues.get(key)).append(DESC[4]);
                        break;
                    case Lightning.ORB_ID:
                        sb.append(DESC[5]).append(passiveValues.get(key)).append(DESC[6]);
                        break;
                    case Plasma.ORB_ID:
                        sb.append(DESC[7]).append(passiveValues.get(key)).append(DESC[8]);
                        break;
                    default:
                        notFound = true;
                        break;

                }
                if(notFound) {
                    if (Aspiration.hasReplay) {
                        switch (key) {
                            case ReplayLightOrb.ORB_ID:
                                sb.append(DESC[12]).append(passiveValues.get(key)).append(DESC[13]);
                                break;
                            case HellFireOrb.ORB_ID:
                                sb.append(DESC[16]).append(passiveValues.get(key)).append(DESC[17]);
                                break;
                            case CrystalOrb.ORB_ID:
                                sb.append(DESC[14]).append(passiveValues.get(key)).append(DESC[15]);
                                break;
                            case GlassOrb.ORB_ID: //nothing
                                break;
                        }
                        if (Aspiration.hasMarisa) {
                            switch (key) {
                                case ManaSparkOrb.ORB_ID:
                                    sb.append(DESC[18]).append(passiveValues.get(key)).append(DESC[19]);
                                    break;
                            }
                        }
                    }
                    if (Aspiration.hasConspire) {
                        switch (key) {
                            case Water.ORB_ID:
                                sb.append(DESC[9]).append(passiveValues.get(key));
                                if (passiveValues.get(key) == 1) {
                                    sb.append(DESC[10]);
                                } else {
                                    sb.append(DESC[11]);
                                }
                                break;
                        }
                    }
                    if (Aspiration.hasVex) {
                        switch (key) {
                            case GoldenLightning.ORB_ID:
                                sb.append(DESC[5]).append(passiveValues.get(key)); //first part is same as lightning
                                sb.append(DESC[33]).append(passiveValues.get(key)).append(DESC[34]); //second part is not
                                break;
                        }
                    }
                }
            }

            sb.append(DESC[20]);

            for (String key : evokeValues.keySet())
            {
                notFound = false;
                switch (key)
                {
                    case Dark.ORB_ID:
                        sb.append(DESC[21]).append(evokeValues.get(key)).append(DESC[22]);
                        break;
                    case Frost.ORB_ID:
                        sb.append(DESC[23]).append(evokeValues.get(key)).append(DESC[4]);
                        break;
                    case Lightning.ORB_ID:
                        sb.append(DESC[21]).append(evokeValues.get(key)).append(DESC[6]);
                        break;
                    case Plasma.ORB_ID:
                        sb.append(DESC[23]).append(evokeValues.get(key)).append(DESC[8]);
                        break;
                    default:
                        notFound = true;
                }
                if(notFound) {
                    if(Aspiration.hasReplay) {
                        switch (key)
                        {
                            case ReplayLightOrb.ORB_ID:
                                sb.append(DESC[23]).append(evokeValues.get(key)).append(DESC[24]);
                                break;
                            case HellFireOrb.ORB_ID:
                                sb.append(DESC[26]).append(evokeValues.get(key)).append(DESC[27]);
                                break;
                            case CrystalOrb.ORB_ID:
                                sb.append(DESC[29]).append(evokeValues.get(key)).append(DESC[30]);
                                break;
                            case GlassOrb.ORB_ID:
                                sb.append(DESC[31]).append(evokeValues.get(key)).append(DESC[32]);
                                break;
                        }
                        if(Aspiration.hasMarisa) {
                            switch (key)
                            {
                                case ManaSparkOrb.ORB_ID:
                                    sb.append(DESC[23]).append(manaSparkAmount).append(DESC[28]).append(evokeValues.get(key)).append(DESC[19]);
                                    break;
                            }
                        }
                    }
                    if(Aspiration.hasConspire) {
                        switch (key)
                        {
                            case Water.ORB_ID:
                                sb.append(DESC[25]).append(evokeValues.get(key)).append(DESC[11]);
                                break;
                        }
                    }
                    if (Aspiration.hasVex) {
                        switch (key) {
                            case GoldenLightning.ORB_ID:
                                sb.append(DESC[21]).append(evokeValues.get(key)); //first part is same as lightning
                                sb.append(DESC[33]).append(evokeValues.get(key)).append(DESC[34]); //second part is not
                                break;
                        }
                    }
                }
            }

            if (goldCap > 0)
                sb.append(DESC[35]).append(goldCap).append(DESC[34]);

            description = sb.toString();
            //description = "";//description.substring(0, description.lastIndexOf(" NL "));
        }
    }

    @Override
    public void applyFocus() {
        basePassiveAmount = 0;
        passiveAmount = 0;
        crystalBonus = 0;

        if (Aspiration.hasReplay) {
            int mypos = AbstractDungeon.player.orbs.indexOf(this);
            if (mypos > -1) {
                if (mypos > 0) {
                    AbstractOrb adjOrb = AbstractDungeon.player.orbs.get(mypos - 1);
                    if (adjOrb != null && adjOrb.ID != null) {
                        if (adjOrb.ID.equals(CrystalOrb.ORB_ID)) {
                            this.passiveAmount += adjOrb.passiveAmount;
                        } else if (adjOrb instanceof aspiration.orbs.AmalgamateOrb) {
                            this.passiveAmount += ((aspiration.orbs.AmalgamateOrb) adjOrb).crystalBonus;
                        }
                    }
                }
                if (mypos < AbstractDungeon.player.orbs.size() - 1) {
                    AbstractOrb adjOrb = AbstractDungeon.player.orbs.get(mypos + 1);
                    if (adjOrb != null && adjOrb.ID != null) {
                        if (adjOrb.ID.equals(CrystalOrb.ORB_ID)) {
                            this.passiveAmount += adjOrb.passiveAmount;
                        } else if (adjOrb instanceof aspiration.orbs.AmalgamateOrb) {
                            this.passiveAmount += ((aspiration.orbs.AmalgamateOrb) adjOrb).crystalBonus;
                        }
                    }
                }
            }

            for (AbstractOrb orb : components) {
                orb.applyFocus();
                if (!orb.ID.equals(Plasma.ORB_ID) && (!Aspiration.hasConspire || !orb.ID.equals(Water.ORB_ID))) {
                    if (!orb.ID.equals(CrystalOrb.ORB_ID)) {
                        orb.passiveAmount += this.passiveAmount;
                    } else {
                        crystalBonus += orb.passiveAmount;
                    }

                    if (!orb.ID.equals(Dark.ORB_ID)) {
                        orb.evokeAmount += this.passiveAmount;
                    }
                }
            }
        }
    }

    @Override
    public void onEvoke() {
        boolean triggeredDark = false;
        boolean triggeredFrost = false;
        boolean triggeredLightning = false;
        boolean triggeredGoldenLightning = false;
        boolean triggeredLight = false;
        boolean triggeredPlasma = false;
        boolean triggeredCrystal = false;
        boolean triggeredGlass = false;

        boolean notFound;
        for (AbstractOrb orb : components)
        {
            notFound = false;
            switch (orb.ID)
            {
                case Dark.ORB_ID:
                    if (!triggeredDark)
                    {
                        Dark toTrigger = new Dark();
                        toTrigger.evokeAmount = evokeValues.get(orb.ID);
                        AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                        triggeredDark = true;
                    }
                    break;
                case Frost.ORB_ID:
                    if (!triggeredFrost)
                    {
                        Frost toTrigger = new Frost();
                        toTrigger.evokeAmount = evokeValues.get(orb.ID);
                        AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                        triggeredFrost = true;
                    }
                    break;
                case Lightning.ORB_ID:
                    if (!triggeredLightning)
                    {
                        Lightning toTrigger = new Lightning();
                        toTrigger.evokeAmount = evokeValues.get(orb.ID);
                        AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                        triggeredLightning = true;
                    }
                    break;
                case Plasma.ORB_ID:
                    if (!triggeredPlasma)
                    {
                        AbstractOrb toTrigger = orb.makeCopy();
                        toTrigger.evokeAmount = evokeValues.get(orb.ID);
                        AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                        triggeredPlasma = true;
                    }
                    break;
                default:
                    notFound = true;
                    break;
            }
            if(notFound) {
                if(Aspiration.hasReplay) {
                    switch (orb.ID)
                    {
                        case ReplayLightOrb.ORB_ID:
                            if (!triggeredLight) {
                                AbstractOrb toTrigger = orb.makeCopy();
                                toTrigger.evokeAmount = evokeValues.get(orb.ID);
                                AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                                triggeredLight = true;
                            }
                            break;
                        case CrystalOrb.ORB_ID:
                            if (!triggeredCrystal) {
                                AbstractOrb toTrigger = orb.makeCopy();
                                toTrigger.evokeAmount = evokeValues.get(orb.ID);
                                AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                                triggeredCrystal = true;
                            }
                            break;
                        case GlassOrb.ORB_ID:
                            if (!triggeredGlass) {
                                AbstractOrb toTrigger = orb.makeCopy();
                                toTrigger.evokeAmount = evokeValues.get(orb.ID);
                                AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));
                                triggeredGlass = true;
                            }
                            break;
                        case HellFireOrb.ORB_ID:
                            AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(orb));
                            break;

                    }
                    if(Aspiration.hasMarisa) {
                        switch (orb.ID)
                        {
                            case ManaSparkOrb.ORB_ID: //too much of a bother
                                AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(orb));
                                break;
                        }
                    }
                }
                if(Aspiration.hasConspire) {
                    switch (orb.ID)
                    {
                        case Water.ORB_ID: //no difference
                            AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(orb));
                            break;
                    }
                }
                if (Aspiration.hasVex) {
                    switch (orb.ID)
                    {
                        case GoldenLightning.ORB_ID:
                            if (!triggeredGoldenLightning)
                            {
                                Lightning toTrigger = new Lightning(); //Use lightning orb to do the damage, and gain gold itself
                                //Since golden lightning has hard 20 gold cap,
                                //Making a golden lightning wouldn't work well if two were combined.
                                toTrigger.evokeAmount = evokeValues.get(orb.ID);
                                AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(toTrigger));

                                this.gainGold(evokeValues.get(orb.ID));

                                triggeredGoldenLightning = true;
                            }
                            break;
                    }
                }
            }
        }
        this.updateDescription();
    }

    @Override
    public void onStartOfTurn() {
        boolean triggeredHellfire = false;
        boolean triggeredManaspark = false;

        boolean notFound;
        for (AbstractOrb orb : components)
        {
            notFound = false;
            switch (orb.ID)
            {
                case Dark.ORB_ID: //end of turn orbs
                case Frost.ORB_ID:
                case Lightning.ORB_ID:
                    break;
                case Plasma.ORB_ID:
                    orb.onStartOfTurn(); //these don't have any problem being spammed, no difference
                    break;
                default:
                    notFound =true;
                    break;
            }
            if(notFound) {
                if(Aspiration.hasReplay) {
                    switch (orb.ID)
                    {
                        case ReplayLightOrb.ORB_ID:
                        case CrystalOrb.ORB_ID:
                        case GlassOrb.ORB_ID:
                            break;
                        case HellFireOrb.ORB_ID: //This stuff ensures vfx spams, but not the effect itself, which can affect interactions when applying powers
                            if (triggeredHellfire)
                            {
                                AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.PLASMA), 0.1f));
                            }
                            else
                            {
                                int hellfireValue = orb.passiveAmount;
                                orb.passiveAmount = passiveValues.get(orb.ID);
                                triggeredHellfire = true;
                                orb.onStartOfTurn();
                                orb.passiveAmount = hellfireValue;
                            }
                            break;
                    }
                    if(Aspiration.hasMarisa) {
                        switch (orb.ID)
                        {
                            case ManaSparkOrb.ORB_ID:
                                if (triggeredManaspark)
                                {
                                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.PLASMA), 0.1f));
                                }
                                else
                                {
                                    int manasparkValue = orb.passiveAmount;
                                    orb.passiveAmount = passiveValues.get(orb.ID);
                                    orb.onStartOfTurn();
                                    triggeredManaspark = true;
                                    orb.passiveAmount = manasparkValue;
                                }
                                break;
                        }
                    }
                }
                if(Aspiration.hasConspire) {
                    switch (orb.ID)
                    {
                        case Water.ORB_ID:
                            orb.onStartOfTurn(); //these don't have any problem being spammed, no difference
                            break;
                    }
                }
            }
        }
        this.updateDescription();
    }

    @Override
    public void onEndOfTurn() {
        boolean triggeredFrost = false;
        boolean triggeredLightning = false;
        boolean triggeredLight = false;
        boolean triggeredGlass = false;
        boolean triggeredGoldenLightning = false;

        boolean notFound;

        //for lightning
        float speedTime = 0.2F / (float)AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }

        for (AbstractOrb orb : components)
        {
            notFound = false;
            switch (orb.ID)
            {
                case Dark.ORB_ID: //end of turn orbs
                    orb.onEndOfTurn();
                    break;
                case Frost.ORB_ID:
                    if (triggeredFrost)
                    {
                        float frostSpeedTime = 0.6F / (float)AbstractDungeon.player.orbs.size();
                        if (Settings.FAST_MODE) {
                            frostSpeedTime = 0.0F;
                        }

                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.FROST), frostSpeedTime));
                    }
                    else
                    {
                        int frostValue = orb.passiveAmount;
                        orb.passiveAmount = passiveValues.get(orb.ID);
                        triggeredFrost = true;
                        orb.onEndOfTurn();
                        orb.passiveAmount = frostValue;
                    }
                    break;
                case Lightning.ORB_ID:
                    if (triggeredLightning)
                    {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.LIGHTNING), speedTime));
                    }
                    else
                    {
                        int lightningValue = orb.passiveAmount;
                        orb.passiveAmount = passiveValues.get(orb.ID);
                        orb.onEndOfTurn();
                        triggeredLightning = true;
                        orb.passiveAmount = lightningValue;
                    }
                    break;
                case Plasma.ORB_ID: //no end of turn effect
                    break;
                default:
                    notFound = true;
                    break;
            }
            if(notFound) {
                if(Aspiration.hasReplay) {
                    switch (orb.ID)
                    {
                        case ReplayLightOrb.ORB_ID:
                            int lightValue = orb.passiveAmount;
                            if (triggeredLight)
                            {
                                orb.passiveAmount = 0;
                            }
                            else
                            {
                                orb.passiveAmount = passiveValues.get(orb.ID);
                                triggeredLight = true;
                            }
                            orb.onEndOfTurn();
                            orb.passiveAmount = lightValue;
                            break;
                        case HellFireOrb.ORB_ID:
                        case CrystalOrb.ORB_ID:
                            break;
                        case GlassOrb.ORB_ID: //glass actually has end of turn effect with a specific power
                            int glassValue = orb.passiveAmount;
                            if (!triggeredGlass) //only trigger once
                            {
                                orb.passiveAmount = passiveValues.get(orb.ID);
                                triggeredGlass = true;
                                orb.passiveAmount = glassValue;
                                orb.onEndOfTurn();
                            }
                            break;
                    }
                    if(Aspiration.hasMarisa) {
                        switch (orb.ID)
                        {
                            case ManaSparkOrb.ORB_ID:
                                break;
                        }
                    }
                }
                if(Aspiration.hasConspire) {
                    switch (orb.ID)
                    {
                        case Water.ORB_ID:
                            break;
                    }
                }
                if (Aspiration.hasVex) {
                    switch (orb.ID)
                    {
                        case GoldenLightning.ORB_ID:
                            if (triggeredGoldenLightning)
                            {
                                AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.LIGHTNING), speedTime));
                            }
                            else
                            {
                                Lightning toTrigger = new Lightning(); //Use lightning orb to do the damage, and gain gold itself
                                toTrigger.passiveAmount = passiveValues.get(orb.ID);
                                toTrigger.onEndOfTurn();
                                this.gainGold(passiveValues.get(orb.ID));
                                triggeredGoldenLightning = true;
                            }
                    }
                }
            }
        }
        this.updateDescription();
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        // If you want to create a whole new orb effect - take a look at conspire's Water Orb. It includes a custom sound, too!
        super.updateAnimation();

        for (AbstractOrb orb : components) //ensure animation effects are in correct place
        {
            orb.tX = this.cX;
            orb.tY = this.cY;
            orb.cX = this.cX;
            orb.cY = this.cY;
        }

        angle += Gdx.graphics.getDeltaTime() * 45.0f;
        for (AbstractOrb orb : components) {
            orb.updateAnimation(); //This might add.. a moderately large amount of vfx
        }
    }

    // Render the orb.
    @Override
    public void render(SpriteBatch sb) {
        if (components.isEmpty()) {
            sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a / 2.0f));
            sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);
            sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
            sb.setBlendFunction(770, 1);
            sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, -angle, 0, 0, 96, 96, false, false);
            sb.setBlendFunction(770, 771);
        } else {
            Color renderColor = new Color(1.0f, 1.0f, 1.0f, c.a / components.size());
            sb.setColor(renderColor);
            for (Texture t : textures) {
                sb.draw(t, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale, angle, 0, 0, 96, 96, false, false);
            }

            //do any other special renders
            for (AbstractOrb orb : components) {
                if (orb instanceof Frost) {
                    sb.draw(Frost.img1, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
                    sb.draw(Frost.img2, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F - this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
                    sb.draw(Frost.img3, this.cX - 48.0F - this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 2.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
                }
                else if (Aspiration.hasReplay && orb.ID.equals(CrystalOrb.ORB_ID))
                {
                    try
                    {
                        Texture img1 = (Texture)ReflectionHacks.getPrivateStatic(CrystalOrb.class, "img1");
                        Texture img2 = (Texture)ReflectionHacks.getPrivateStatic(CrystalOrb.class, "img2");
                        Texture img3 = (Texture)ReflectionHacks.getPrivateStatic(CrystalOrb.class, "img3");
                        sb.draw(img1, this.cX - 48.0f + this.bobEffect.y / 4.0f, this.cY - 48.0f + this.bobEffect.y / 4.0f, 48.0f, 48.0f, 96.0f, 96.0f, this.scale, this.scale, 0.0f, 0, 0, 96, 96, false, false);
                        sb.draw(img2, this.cX - 48.0f + this.bobEffect.y / 4.0f, this.cY - 48.0f - this.bobEffect.y / 4.0f, 48.0f, 48.0f, 96.0f, 96.0f, this.scale, this.scale, 0.0f, 0, 0, 96, 96, false, false);
                        sb.draw(img3, this.cX - 48.0f - this.bobEffect.y / 4.0f, this.cY - 48.0f + this.bobEffect.y / 2.0f, 48.0f, 48.0f, 96.0f, 96.0f, this.scale, this.scale, 0.0f, 0, 0, 96, 96, false, false);
                    }
                    catch (Exception e) { Aspiration.logger.info(e);}
                }
                //add anything else that renders special that you want to bother with
            }
        }
        hb.render(sb);
    }


    @Override
    public void triggerEvokeAnimation() {
        if (components.isEmpty()) {
            AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(cX, cY));
        } else {
            for (AbstractOrb orb : components) {
                orb.triggerEvokeAnimation();
            }
        }
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }

    private void gainGold(int amount)
    {
        int cap = Math.min(goldGenerated + amount, goldCap);
        for (; goldGenerated < cap; goldGenerated++) {
            AbstractDungeon.player.gainGold(1);
            AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
        }
    }

    @Override
    public AbstractOrb makeCopy() {
        return new AmalgamateOrb(components);
    }
}