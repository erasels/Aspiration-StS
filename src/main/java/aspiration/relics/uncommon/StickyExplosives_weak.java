package aspiration.relics.uncommon;

import aspiration.powers.AftershockPower_weak;
import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class StickyExplosives_weak extends AspirationRelic{
    public static final String ID = "aspiration:StickyExplosives_weak";

    private boolean applied = false;
    private static final float percentagAftershock = 0.30f;
    private static final int turnAmt = 3;

    public StickyExplosives_weak() {
        super(ID, "StickyExplosives.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
        updateTip();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!applied && info.owner == AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL && !target.isPlayer) {
            stopPulse();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new AftershockPower_weak(target, percentagAftershock, turnAmt), turnAmt));
            applied = true;
        }
    }

    @Override
    public void atBattleStart() {
        beginLongPulse();
        applied = false;
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    private void updateTip() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(AftershockPower_weak.NAME, (AftershockPower_weak.DESCRIPTIONS[0] + turnAmt + AftershockPower_weak.DESCRIPTIONS[1] + MathUtils.round(percentagAftershock*100) + AftershockPower_weak.DESCRIPTIONS[2])));
        initializeTips();
    }
}