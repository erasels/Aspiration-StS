package aspiration.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.Collections;

public class RemoveSpecificOrbAction extends AbstractGameAction {
    private AbstractOrb orb;

    public RemoveSpecificOrbAction(AbstractOrb orb) {
        this.startDuration = this.duration = Settings.ACTION_DUR_FAST;
        this.orb = orb;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            int position = AbstractDungeon.player.orbs.indexOf(this.orb);
            if (position == -1) {
                this.isDone = true;
                return;
            }
            for (int i = position + 1; i < AbstractDungeon.player.orbs.size(); ++i) {
                Collections.swap(AbstractDungeon.player.orbs, i, i - 1);
            }

            AbstractDungeon.player.orbs.set(AbstractDungeon.player.orbs.size() - 1, new EmptyOrbSlot());

            for (int i = position; i < AbstractDungeon.player.orbs.size(); ++i) {
                ((AbstractOrb) AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
            }
        }
        this.isDone = true;
    }

}