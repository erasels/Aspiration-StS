package aspiration.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.function.Predicate;

public class AddActionPostActionAction extends AbstractGameAction {
    private Predicate<AbstractGameAction> condition;
    private AbstractGameAction toAdd;

    public AddActionPostActionAction(AbstractGameAction toAdd, Predicate<AbstractGameAction> condition) {
        this.toAdd = toAdd;
        this.condition = condition;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            int targetIndex = -1;
            for (int i = 0; i < AbstractDungeon.actionManager.actions.size(); i++) {
                if (condition.test(AbstractDungeon.actionManager.actions.get(i))) {
                    targetIndex = i + 1;
                    break;
                }
            }

            if (targetIndex >= 0) {
                AbstractDungeon.actionManager.actions.add(targetIndex, toAdd);
            } else //If this action has been reached and the target action is not found, that means it was added to top and already occurred.
            {
                AbstractDungeon.actionManager.addToTop(toAdd);
            }
        }

        this.isDone = true;
    }
}