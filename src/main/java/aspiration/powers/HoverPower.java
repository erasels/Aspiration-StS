package aspiration.powers;

import aspiration.powers.abstracts.AspirationPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class HoverPower extends AspirationPower {
    public static final String POWER_ID = "aspiration:Hover";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final float DMG_MULTI = 0.5F;
    private static final float FLY_HEIGHT = 0.1F;

    private float initialPlayerHeight = 0;

    public HoverPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        isTurnBased = false;
        amount = -1;
        type = PowerType.BUFF;
        updateDescription();
        loadRegion("flight");
        priority = 50;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + MathUtils.round(DMG_MULTI*100) + DESCRIPTIONS[1];
    }

    @Override
    public void onInitialApplication(){
        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.state != null) AbstractDungeon.player.state.setTimeScale(10);
            initialPlayerHeight = AbstractDungeon.player.drawY;
            AbstractDungeon.player.drawY += Settings.HEIGHT * FLY_HEIGHT * Settings.scale;
        }
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * DMG_MULTI;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        flashWithoutSound();
        return damageAmount;
    }

    @Override
    public void onRemove() {
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.drawY = initialPlayerHeight;
            if (AbstractDungeon.player.state != null) AbstractDungeon.player.state.setTimeScale(1);
        }
    }

    public static String getDesc() {
        return DESCRIPTIONS[0] + MathUtils.round(DMG_MULTI*100) + DESCRIPTIONS[1];
    }
}
