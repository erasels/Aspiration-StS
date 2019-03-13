package aspiration.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BefuddledPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "aspiration:Befuddled";
    private static final PowerStrings powerStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int reduction_amount_for_turn = 0;

    public BefuddledPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;
        updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.DEBUFF;
        loadRegion("fumes");
    }

    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[2] + this.reduction_amount_for_turn + DESCRIPTIONS[3]);
    }

    public void onInitialApplication() {
        determineReduction();
    }

    public void atStartOfTurn() {
        determineReduction();
    }

    public void stackPower(int i) {
        this.fontScale = 8.0F;
        this.amount += i;
        determineReduction();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage - this.reduction_amount_for_turn;
        }
        return damage;
    }

    public AbstractPower makeCopy() {
        return new BefuddledPower(this.owner, this.amount);
    }

    public static String getDesc() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[4] + DESCRIPTIONS[1];
    }

    private void determineReduction() {
        this.reduction_amount_for_turn = AbstractDungeon.cardRandomRng.random(this.amount);
        updateDescription();
    }
}