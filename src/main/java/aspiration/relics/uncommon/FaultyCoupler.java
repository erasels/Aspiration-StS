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

public class FaultyCoupler extends AspirationRelic implements OnChannelRelic {
    public static final String ID = "aspiration:FaultyCoupler";
    public static final int AMA_CHANCE = 20;

    public FaultyCoupler() {
        super(ID, "FaultyCoupler.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMA_CHANCE + DESCRIPTIONS[1];
    }

    @Override
    public void onChannel(AbstractOrb orbyBoi) {
        if(AbstractDungeon.player.filledOrbCount() > 1 && !orbyBoi.ID.equals(AmalgamateOrb.ORB_ID) && OrbUtilityMethods.isValidAmalgamateComponent(orbyBoi)) {
            if (AbstractDungeon.cardRandomRng.random(99) <= AMA_CHANCE) {
                AbstractDungeon.actionManager.addToTop(new FaultyCouplerAction(orbyBoi));
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