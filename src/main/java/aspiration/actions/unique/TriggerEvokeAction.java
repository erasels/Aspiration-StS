package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class TriggerEvokeAction extends AbstractGameAction {
    private AbstractOrb toEvoke;

    public TriggerEvokeAction(AbstractOrb toEvoke) {
        this.actionType = ActionType.DAMAGE;
        this.toEvoke = toEvoke;
    }

    @Override
    public void update() {
        toEvoke.onEvoke();
        this.isDone = true;
    }
}