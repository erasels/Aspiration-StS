package aspiration.relics.uncommon;

import aspiration.actions.RemoveSpecificOrbAction;
import aspiration.orbs.AmalgamateOrb;
import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.skillbooks.DefectSkillbook;
import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Arrays;

public class FaultyCoupler extends AspirationRelic implements OnChannelRelic {
    public static final String ID = "aspiration:FaultyCoupler";

    private static final int AMA_CHANCE = 20;

    public FaultyCoupler() {
        super(ID, "FaultyCoupler.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMA_CHANCE + DESCRIPTIONS[1];
    }

    @Override
    public void onChannel(AbstractOrb orbyBoi) {
        if(AbstractDungeon.player.filledOrbCount() > 1 && !orbyBoi.ID.equals(AmalgamateOrb.ORB_ID)) {
            //System.out.println(AbstractDungeon.player.filledOrbCount() + " filled orbs and " + orbyBoi.ID + " was cahnneled.");
            if(AbstractDungeon.cardRandomRng.random(99) < AMA_CHANCE) {
                ArrayList<AbstractOrb> orbs = new ArrayList<>();
                for(AbstractOrb o : AbstractDungeon.player.orbs) {
                    if(o != null && !EmptyOrbSlot.ORB_ID.equals(o.ID)) {
                        if(o.ID != null) {
                            orbs.add(o);
                            //System.out.println(o.ID + " is in the lsit");
                        }
                    }
                }
                //System.out.println(orb.ID + " was picked.");
                AbstractDungeon.actionManager.addToTop(new ChannelAction(new AmalgamateOrb(new ArrayList<AbstractOrb>(Arrays.asList(orbyBoi, orbs.get(AbstractDungeon.cardRandomRng.random(orbs.size() - 1)))))));
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificOrbAction(orbyBoi));
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.DEFECT || AbstractDungeon.player.hasRelic(DefectSkillbook.ID));
    }

    public AbstractRelic makeCopy() {
        return new FaultyCoupler();
    }
}