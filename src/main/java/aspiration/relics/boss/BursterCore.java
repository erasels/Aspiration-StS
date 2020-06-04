package aspiration.relics.boss;

import aspiration.relics.abstracts.StatRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.CrackedCore;

public class BursterCore extends StatRelic {
    public static final String ID = "aspiration:BursterCore";

    public static final int EVOKE_AMT = 2;
    public static final String STAT1 = "Additional evokes: ";

    public BursterCore() {
        super(ID, "BursterCore.png", RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[1] + DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, DESCRIPTIONS[0]));
        this.initializeTips();
    }

    //Actual logic is in BursterCorePatch

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(CrackedCore.ID) && !AbstractDungeon.player.hasRelic(Stellarator.ID);
    }

    @Override
    public void statsInit() {
        stats.put(STAT1, 0);
    }
}
