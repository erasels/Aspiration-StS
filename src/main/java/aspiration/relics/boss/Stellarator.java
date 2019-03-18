package aspiration.relics.boss;

import aspiration.actions.unique.FuseValidOrbsAction;
import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Stellarator extends AspirationRelic implements OnChannelRelic {
    public static final String ID = "aspiration:Stellarator";

    public Stellarator() {
        super(ID, "Stellarator.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onChannel(AbstractOrb abstractOrb) {
        if (AbstractDungeon.player.filledOrbCount() == AbstractDungeon.player.orbs.size()) {
            if(AbstractDungeon.player.orbs.size() > 1) {
                AbstractDungeon.actionManager.addToTop(new FuseValidOrbsAction());
            }
            AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
            AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
        }
    }

    public AbstractRelic makeCopy() {
        return new Stellarator();
    }
}