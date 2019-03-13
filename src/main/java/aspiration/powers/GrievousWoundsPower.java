package aspiration.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GrievousWoundsPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "aspiration:GrievousWounds";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GrievousWoundsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;
        updateDescription();
        this.isTurnBased = true;
        this.type = AbstractPower.PowerType.DEBUFF;
        loadRegion("sadistic");
    }

    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[2]);
    }

    public void updateDescription(int futureStack) {
        this.description = (DESCRIPTIONS[0] + futureStack + DESCRIPTIONS[1] + DESCRIPTIONS[2]);
    }

    public void stackPower(int i) {
        this.fontScale = 8.0F;
        this.amount += i;
        updateDescription();
    }

    public void atEndOfRound() {
        if (this.amount <= 1) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, MathUtils.round(this.amount / 2.0F)));
            updateDescription(MathUtils.round(this.amount / 2.0F));
        }
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage + this.amount;
        }
        return damage;
    }

    public AbstractPower makeCopy() {
        return new GrievousWoundsPower(this.owner, this.amount);
    }

    public static String getDesc() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[3] + DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }
}
