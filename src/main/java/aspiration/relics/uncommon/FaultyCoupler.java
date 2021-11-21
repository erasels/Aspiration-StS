package aspiration.relics.uncommon;

import aspiration.actions.unique.FaultyCouplerAction;
import aspiration.orbs.AmalgamateOrb;
import aspiration.orbs.OrbUtilityMethods;
import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.skillbooks.DefectSkillbook;
import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

public class FaultyCoupler extends AspirationRelic implements OnChannelRelic {
    public static final String ID = "aspiration:FaultyCoupler";
    public static final int AMA_COUNTER = 5;

    public FaultyCoupler() {
        super(ID, "FaultyCoupler.png", RelicTier.UNCOMMON, LandingSound.CLINK);
        setCounter(0);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], AMA_COUNTER+TopPanel.getOrdinalNaming(AMA_COUNTER));
    }

    @Override
    public void onChannel(AbstractOrb orbyBoi) {
        setCounter(counter + 1);
        if(counter >= AMA_COUNTER) {
            beginLongPulse();
        }
        if(pulse && AbstractDungeon.player.filledOrbCount() > 1 && !orbyBoi.ID.equals(AmalgamateOrb.ORB_ID) && OrbUtilityMethods.isValidAmalgamateComponent(orbyBoi)) {
            flash();
            setCounter(0);
            stopPulse();
            AbstractDungeon.actionManager.addToTop(new FaultyCouplerAction(orbyBoi));
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