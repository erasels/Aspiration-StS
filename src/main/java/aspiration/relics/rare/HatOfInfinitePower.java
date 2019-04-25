package aspiration.relics.rare;

import aspiration.Aspiration;
import aspiration.relics.abstracts.AspirationRelic;
import aspiration.relics.abstracts.OnEnergyUse;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import conspire.relics.SlowCooker;

public class HatOfInfinitePower extends AspirationRelic implements OnEnergyUse {
    public static final String ID = "aspiration:HatOfInfinitePower";

    private boolean triggered = false;
    private boolean slowCookerSynergy = false;

    public HatOfInfinitePower() {
        super(ID, "HatOfInfinitePower.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int onEnergyUse(int amount) {
        if (amount > 1 && !triggered) {
            trigger();
            return 1;
        }
        return amount;
    }

    @Override
    public void atTurnStart() {
        if (slowCookerSynergy) {
            AbstractDungeon.player.energy.energyMaster--;
            slowCookerSynergy = false;
            return;
        }
        triggered = false;
        beginLongPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    //Conspire SlowCooker synergy
    @Override
    public void atPreBattle() {
        if (Aspiration.hasConspire && AbstractDungeon.player.hasRelic(SlowCooker.ID)) {
            AbstractDungeon.player.energy.energyMaster++;
            trigger();
            slowCookerSynergy = true;
        }
    }

    //Hubris Sloth Synergy in patch (doesn't work)

    public void trigger() {
        triggered = true;
        stopPulse();
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public boolean isTriggered() {
        return triggered;
    }

    public AbstractRelic makeCopy() {
        return new HatOfInfinitePower();
    }
}