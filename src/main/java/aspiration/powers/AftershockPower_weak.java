package aspiration.powers;

import aspiration.Aspiration;
import aspiration.actions.SpawnTolerantDamageAllEnemiesAction;
import aspiration.powers.abstracts.AspirationPower;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AftershockPower_weak extends AspirationPower implements CloneablePowerInterface {
    public static final String POWER_ID = "aspiration:Aftershock_weak";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private float aftershockPercentageDamage;
    private int explosionAmt = 0;

    public AftershockPower_weak(AbstractCreature owner, float damageEcho, int turnAmt)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        aftershockPercentageDamage = damageEcho;
        this.amount = turnAmt;
        updateDescription();
        region48 = Aspiration.powerAtlas.findRegion("48/aftershock");
        region128 = Aspiration.powerAtlas.findRegion("128/aftershock");
        isTurnBased = true;
    }

    @Override
    public int onAttacked(DamageInfo info, int dmgAmount) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && dmgAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            this.flashWithoutSound();
            explosionAmt += dmgAmount;
            Aspiration.logger.info("Stored: " + explosionAmt + " Damage:" + MathUtils.floor(((float)explosionAmt)*aftershockPercentageDamage));
            updateDescription();
        }

        return dmgAmount;
    }

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, ID, 1));
        }
    }

    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToTop(new SpawnTolerantDamageAllEnemiesAction(AbstractDungeon.player, MathUtils.floor(((float)explosionAmt) * aftershockPercentageDamage), true, false, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + MathUtils.floor(aftershockPercentageDamage*100) + DESCRIPTIONS[2] + DESCRIPTIONS[3] + MathUtils.floor(((float)explosionAmt) * aftershockPercentageDamage) + DESCRIPTIONS[4];
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (explosionAmt > 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(MathUtils.floor(((float)explosionAmt)*aftershockPercentageDamage)), x, y + 15.0F * Settings.scale, this.fontScale, c);
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new AftershockPower_weak(owner, aftershockPercentageDamage, amount);
    }
}
