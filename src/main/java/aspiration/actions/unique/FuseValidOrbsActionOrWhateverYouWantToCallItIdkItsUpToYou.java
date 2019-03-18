package aspiration.actions.unique;

import aspiration.actions.RemoveSpecificOrbAction;
import aspiration.orbs.AmalgamateOrb;
import aspiration.orbs.OrbUtilityMethods;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;

public class FuseValidOrbsActionOrWhateverYouWantToCallItIdkItsUpToYou extends AbstractGameAction {

    public FuseValidOrbsActionOrWhateverYouWantToCallItIdkItsUpToYou() {
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new ChannelAction(new AmalgamateOrb(AbstractDungeon.player.orbs))); //this triggers last
        //AbstractDungeon.actionManager.addToTop(new WaitAction(10.0f));
        ArrayList<AbstractOrb> orbsToRemove = OrbUtilityMethods.getOrbList();
        AbstractDungeon.player.orbs.forEach(o -> {
            for(AbstractOrb orb : orbsToRemove) {
                if (orb.getClass().isInstance(o)) {
                    //System.out.println("Crash bas?");
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificOrbAction(o));
                }
            }
        });


       /* if (AbstractDungeon.player.hasOrb())
        {
            AmalgamateOrb toAdd = new AmalgamateOrb(AbstractDungeon.player.orbs);

            AbstractDungeon.actionManager.addToTop(new ChannelAction(toAdd)); //this triggers second

            AbstractDungeon.actionManager.addToTop(new RemoveAllOrbsAction()); //this triggers first
        }
        else
        {
            AmalgamateOrb toAdd = new AmalgamateOrb();

            AbstractDungeon.actionManager.addToTop(new ChannelAction(toAdd)); //this triggers second
        }*/

        this.isDone = true;
    }
}
