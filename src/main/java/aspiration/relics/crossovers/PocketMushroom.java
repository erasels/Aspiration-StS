package aspiration.relics.crossovers;

import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class PocketMushroom extends AspirationRelic implements BetterOnLoseHpRelic {
    public static final String ID = "aspiration:PocketMushroom";

    private boolean hasTriggered = false;
    private static final int DMG_AMT = 12;

    public PocketMushroom() {
        super(ID, "PocketMushroom.png", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DMG_AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        beginPulse();
    }

    @Override
    public int betterOnLoseHp(DamageInfo damageInfo, int dmg) {
        if(!hasTriggered && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) && dmg > 0) {
            hasTriggered = true;
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(damageInfo.owner, this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(damageInfo.owner, new DamageInfo(AbstractDungeon.player, DMG_AMT, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
        }

        return dmg;
    }

    public void onVictory() {
        hasTriggered = false;
        stopPulse();
    }

    public AbstractRelic makeCopy() {
        return new PocketMushroom();
    }
}