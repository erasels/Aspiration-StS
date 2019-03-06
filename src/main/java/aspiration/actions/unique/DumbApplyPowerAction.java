package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;

import java.util.Collections;

public class DumbApplyPowerAction extends AbstractGameAction {
    private AbstractPower powerToApply;
    private float startingDuration;

    public DumbApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        if (Settings.FAST_MODE) {
            this.startingDuration = 0.1F;
        } else if (isFast) {
            this.startingDuration = Settings.ACTION_DUR_FASTER;
        } else {
            this.startingDuration = Settings.ACTION_DUR_FAST;
        }
        setValues(target, source, stackAmount);

        this.duration = this.startingDuration;
        this.powerToApply = powerToApply;

        this.actionType = AbstractGameAction.ActionType.POWER;
        this.attackEffect = effect;
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            this.duration = 0.0F;
            this.startingDuration = 0.0F;
            this.isDone = true;
        }
    }

    public DumbApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast)
    {
        this(target, source, powerToApply, stackAmount, isFast, AbstractGameAction.AttackEffect.NONE);
    }

    public DumbApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply)
    {
        this(target, source, powerToApply, -1);
    }

    public DumbApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount)
    {
        this(target, source, powerToApply, stackAmount, false, AbstractGameAction.AttackEffect.NONE);
    }

    public DumbApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, AbstractGameAction.AttackEffect effect)
    {
        this(target, source, powerToApply, stackAmount, false, effect);
    }

    public void update() {
        if (shouldCancelAction()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startingDuration) {
            if (((this.target instanceof AbstractMonster)) && (this.target.isDeadOrEscaped())) {
                this.duration = 0.0F;
                this.isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));

            if (this.powerToApply.type == AbstractPower.PowerType.DEBUFF) {
                this.target.useFastShakeAnimation(0.5F);
            }

            this.target.powers.add(this.powerToApply);
            Collections.sort(this.target.powers);
            this.powerToApply.onInitialApplication();
            this.powerToApply.flash();

            if (this.powerToApply.type == AbstractPower.PowerType.BUFF) {
                AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name));
            } else {
                AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.powerToApply.name));
            }
            AbstractDungeon.onModifyPower();
        }
        tickDuration();
    }
}