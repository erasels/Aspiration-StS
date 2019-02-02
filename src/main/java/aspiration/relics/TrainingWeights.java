package aspiration.relics;

import aspiration.powers.SappedPower;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TrainingWeights extends AspirationRelic {
    public static final String ID = "aspiration:TrainingWeights";

    private static final int SAPPED_STACK = 2;

    public TrainingWeights() {
        super(ID, "TrainingWeights.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SAPPED_STACK + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SappedPower(AbstractDungeon.player, SAPPED_STACK), SAPPED_STACK));
    }

    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    public AbstractRelic makeCopy() {
        return new TrainingWeights();
    }
}