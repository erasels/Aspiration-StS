package aspiration.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerAmountBasedHealAction extends AbstractGameAction
{
    private String power;

    public PowerAmountBasedHealAction(AbstractCreature target, AbstractCreature source, String power)
    {
        this.target = target;
        this.source = source;
        this.power = power;
        this.duration = 0.5F;
        this.actionType = AbstractGameAction.ActionType.HEAL;
    }

    public void update()
    {
        int healAmt = 0;
        for(AbstractPower p : target.powers) {
            if(p.ID.equals(this.power)) {
                healAmt = p.amount;
            }
        }
        if (this.duration == 0.5F && healAmt > 0) {
            this.target.heal(healAmt);
        }
        tickDuration();
    }
}