package aspiration.orbs;

import aspiration.Aspiration;
import aspiration.actions.unique.TriggerEvokeAction;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;

import java.util.ArrayList;

public class AmalgamateOrb extends AbstractOrb {

    public static final String ORB_ID = "aspiration:Amalgamate";
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    private static final String NAME = orbString.NAME;

    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    private ArrayList<AbstractOrb> components;
    private ArrayList<Texture> textures;

    public AmalgamateOrb(ArrayList<AbstractOrb> components) {
        this.ID = ORB_ID;
        this.name = NAME;

        evokeAmount = baseEvokeAmount = 0;
        passiveAmount = basePassiveAmount = 0;

        this.components = new ArrayList<>();
        this.textures = new ArrayList<>();

        ArrayList<AbstractOrb> accpetableOrbs = OrbUtilityMethods.getOrbList();
        components.removeIf(o -> {
            for(AbstractOrb orb : accpetableOrbs) {
                if (orb.getClass().isInstance(o)) {
                    return false;
                }
            }
            return true;
        });

        if (!components.isEmpty()) {
            for (AbstractOrb orb : components) {
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
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus

        if (components.isEmpty()) {
            description = orbString.DESCRIPTION[0];
        } else {
            ArrayList<String> lines = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            for (AbstractOrb orb : components) {
                orb.updateDescription();
                sb.append(orb.description).append(" NL ");
            }

            description = sb.toString();
            description = description.substring(0, description.lastIndexOf(" NL "));
        }
    }

    @Override
    public void applyFocus() {
        for (AbstractOrb orb : components) {
            orb.applyFocus();
        }
    }

    @Override
    public void onEvoke() {
        for (AbstractOrb orb : components) {
            AbstractDungeon.actionManager.addToBottom(new TriggerEvokeAction(orb));
        }
        this.updateDescription();
    }

    @Override
    public void onStartOfTurn() {// 1.At the start of your turn.
        for (AbstractOrb orb : components) {
            orb.onStartOfTurn();
        }
        this.updateDescription();
    }

    @Override
    public void onEndOfTurn() {
        for (AbstractOrb orb : components) {
            orb.onEndOfTurn();
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

    @Override
    public AbstractOrb makeCopy() {
        return new AmalgamateOrb(); //not too useful.
    }
}