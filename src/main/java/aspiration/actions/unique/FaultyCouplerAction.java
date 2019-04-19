package aspiration.actions.unique;

import aspiration.actions.ReplaceOrbAction;
import aspiration.orbs.AmalgamateOrb;
import aspiration.orbs.OrbUtilityMethods;
import aspiration.relics.uncommon.FaultyCoupler;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.ArrayList;
import java.util.Arrays;

public class FaultyCouplerAction extends AbstractGameAction {

    private AbstractOrb channeled;

    public FaultyCouplerAction(AbstractOrb channeled) {
        this.channeled = channeled;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.orbs.contains(channeled)) {
            if (AbstractDungeon.player.hasRelic(FaultyCoupler.ID)) {
                AbstractDungeon.player.getRelic(FaultyCoupler.ID).flash();
            }

            ArrayList<AbstractOrb> orbs = new ArrayList<>();
            for (AbstractOrb o : AbstractDungeon.player.orbs) {
                if (o != null && !EmptyOrbSlot.ORB_ID.equals(o.ID)) {
                    if (OrbUtilityMethods.isValidAmalgamateComponent(o)) {
                        orbs.add(o);
                    }
                }
            }
            AbstractDungeon.actionManager.addToTop(new ReplaceOrbAction(channeled, new AmalgamateOrb(new ArrayList<>(Arrays.asList(channeled, orbs.get(AbstractDungeon.cardRandomRng.random(orbs.size() - 1)))))));
        }

        this.isDone = true;
    }
}
