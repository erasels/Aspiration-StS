package aspiration.powers;

import aspiration.orbs.OrbUtilityMethods;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ChannelOrbPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "aspiration:ChannelOrb";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ChannelOrbPower(AbstractCreature owner, int stack)
    {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = stack;
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        updateDescription();
        loadRegion("skillBurn");
    }

    public void updateDescription()
    {
        if (this.amount > 1) {
            this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3]);
        } else {
            this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[3]);
        }
    }

    public void atStartOfTurn()
    {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(OrbUtilityMethods.getSelectiveRandomOrb(AbstractDungeon.relicRng)));

            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        }
    }

    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public AbstractPower makeCopy() {
        return new ChannelOrbPower(this.owner, this.amount);
    }
}