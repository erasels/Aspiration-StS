package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class UpdateOrbsAction extends AbstractGameAction
{
    public UpdateOrbsAction() {
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        for (final AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o != null) {
                o.updateDescription();
            }
        }
        this.isDone = true;
    }
}