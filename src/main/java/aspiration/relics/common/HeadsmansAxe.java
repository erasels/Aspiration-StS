package aspiration.relics.common;

import aspiration.powers.CullingPower;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HeadsmansAxe extends AspirationRelic {
    public static final String ID = "aspiration:HeadsmansAxe";

    private static final int IDGAF = 10;

    private float percentage = 0.1f;

    public HeadsmansAxe() {
        super(ID, "HeadsmansAxe.png", RelicTier.COMMON, LandingSound.SOLID);
        /*this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(CullingPower.NAME, CullingPower.getDesc(percentage)));
        this.initializeTips();*/
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + IDGAF + DESCRIPTIONS[1];
    }

    @Override
    public void atPreBattle() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CullingPower(AbstractDungeon.player, percentage)));
    }


    public AbstractRelic makeCopy() {
        return new HeadsmansAxe();
    }
}