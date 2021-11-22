package aspiration.relics.boss;

import aspiration.actions.FuseValidOrbsAction;
import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.skillbooks.DefectSkillbook;
import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

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
    public void atTurnStart() {
        beginLongPulse();
    }

    @Override
    public void onChannel(AbstractOrb abstractOrb) {
        if (pulse && AbstractDungeon.player.filledOrbCount() == AbstractDungeon.player.orbs.size()) {
            flash();
            stopPulse();
            if(AbstractDungeon.player.orbs.size() > 1) {
                addToTop(new FuseValidOrbsAction());
            }
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public boolean canSpawn() {
        return (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.DEFECT || AbstractDungeon.player.hasRelic(DefectSkillbook.ID));
    }
}