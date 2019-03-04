package aspiration.vfx.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;

public class RelicSpeechBubble extends AbstractGameEffect {
    private static final int RAW_W = 512;
    private float shadow_offset = 0.0F;
    private static final float SHADOW_OFFSET = 16.0F * Settings.scale;
    private float x;
    private float y;
    private float scale_x;
    private float scale_y;
    private float wavy_y;
    private float wavyHelper;
    private static final float WAVY_SPEED = 6.0F * Settings.scale;
    private static final float WAVY_DISTANCE = 2.0F * Settings.scale;
    private static final float SCALE_TIME = 0.3F;
    private float scaleTimer = 0.3F;
    private static final float ADJUST_X = 15.0F * Settings.scale;
    private static final float ADJUST_Y = 180.0F * Settings.scale;
    private static final float DEFAULT_DURATION = 2.0F;
    public static final float FADE_TIME = 0.3F;
    public Hitbox hb;

    public RelicSpeechBubble(float x, float y, String msg)
    {
        this(x, y, 2.0F, msg);
    }

    public RelicSpeechBubble(float x, float y, float duration, String msg)
    {
        this.x = x + ADJUST_X;
        this.y = y - ADJUST_Y;
        this.scale_x = (Settings.scale * 0.7F);
        this.scale_y = (Settings.scale * 0.7F);
        this.color = new Color(0.8F, 0.9F, 0.9F, 0.0F);
        this.duration = duration;
        AbstractDungeon.effectsQueue.add(new SpeechTextEffect(x + ADJUST_X, y - (ADJUST_Y - (10 * Settings.scale)), duration - 0.7f, msg, DialogWord.AppearEffect.BUMP_IN));

        this.hb = new Hitbox(x - 350.0F * Settings.scale, y, 350.0F * Settings.scale, 270.0F * Settings.scale);
    }

    public void update()
    {
        updateScale();
        this.hb.update();

        this.wavyHelper += Gdx.graphics.getDeltaTime() * WAVY_SPEED;
        this.wavy_y = (MathUtils.sin(this.wavyHelper) * WAVY_DISTANCE);

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
        if (this.duration > 0.3F) {
            this.color.a = MathUtils.lerp(this.color.a, 1.0F, Gdx.graphics.getDeltaTime() * 12.0F);
        } else {
            this.color.a = MathUtils.lerp(this.color.a, 0.0F, Gdx.graphics.getDeltaTime() * 12.0F);
        }
        this.shadow_offset = MathUtils.lerp(this.shadow_offset, SHADOW_OFFSET, Gdx.graphics.getDeltaTime() * 4.0F);
    }

    private void updateScale()
    {
        this.scaleTimer -= Gdx.graphics.getDeltaTime();
        if (this.scaleTimer < 0.0F) {
            this.scaleTimer = 0.0F;
        }
        this.scale_x = Interpolation.swingIn.apply(this.scale_x, Settings.scale, this.scaleTimer / 0.3F);
        this.scale_y = Interpolation.swingIn.apply(this.scale_y, Settings.scale, this.scaleTimer / 0.3F);
    }

    public void render(SpriteBatch sb)
    {
        this.hb.render(sb);

        sb.setColor(new Color(0.0F, 0.0F, 0.0F, this.color.a / 4.0F));
        sb.draw(ImageMaster.SHOP_SPEECH_BUBBLE_IMG, this.x - 256.0F + this.shadow_offset, this.y - 256.0F - this.shadow_offset + this.wavy_y, 256.0F, 256.0F, 512.0F, 512.0F, this.scale_x, this.scale_y, this.rotation, 0, 0, 512, 512, false, false);

        sb.setColor(this.color);
        sb.draw(ImageMaster.SHOP_SPEECH_BUBBLE_IMG, this.x - 256.0F, this.y - 256.0F + this.wavy_y, 256.0F, 256.0F, 512.0F, 512.0F, this.scale_x, this.scale_y, this.rotation, 0, 0, 512, 512, false, false);
    }

    public void dispose() {}
}