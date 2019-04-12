package aspiration.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class ReplaceOrbAction extends AbstractGameAction {
    private AbstractOrb toReplace;
    private AbstractOrb newOrb;

    public ReplaceOrbAction(AbstractOrb toReplace, AbstractOrb newOrb)
    {
        this.toReplace = toReplace;
        this.newOrb = newOrb;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.orbs.contains(toReplace))
        {
            int index = AbstractDungeon.player.orbs.indexOf(toReplace);
            newOrb.cX = newOrb.tX = toReplace.cX;
            newOrb.cY = newOrb.tY = toReplace.cY;
            newOrb.hb.move(newOrb.cX, newOrb.cY);
            AbstractDungeon.player.orbs.set(index, newOrb);

            AbstractDungeon.actionManager.orbsChanneledThisCombat.add(newOrb);
            AbstractDungeon.actionManager.orbsChanneledThisTurn.add(newOrb);

            newOrb.applyFocus();
        }
        this.isDone = true;
    }
}
