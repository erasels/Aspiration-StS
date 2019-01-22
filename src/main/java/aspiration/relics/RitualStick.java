package aspiration.relics;

import aspiration.powers.P_RitualPower;
import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.RelicGetSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class RitualStick extends AspirationRelic implements RelicGetSubscriber, CustomSavable<Integer> {
    public static final String ID = "aspiration:RitualStick";
    private boolean wasApplied = false;
    private int ritualAmount = 1;
    private boolean hasCM = false;

    public RitualStick() {
        super(ID, "RitualStick.png", RelicTier.SPECIAL, LandingSound.FLAT);
        updateTip();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ritualAmount + DESCRIPTIONS[1];
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (damageAmount > 0 && !wasApplied) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new P_RitualPower(AbstractDungeon.player, ritualAmount), ritualAmount));
                stopPulse();
                wasApplied = true;
            }

            if (hasCM) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.SFXAction("VO_CULTIST_1A"));
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.animations.TalkAction(true, DESCRIPTIONS[2], 1.0F, 2.0F));
            }
        }
    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {
        if(abstractRelic.relicId.equals(CultistMask.ID)) {
            ritualEasteregg();
        }
    }

    @Override
    public void onEquip() {
        this.description = getUpdatedDescription();
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            ritualEasteregg();
        }
    }

    private void ritualEasteregg() {
        this.ritualAmount = 2;
        this.hasCM = true;
        this.description = getUpdatedDescription();
        updateTip();
        flash();
    }

    @Override
    public void atPreBattle() {
        beginPulse();
    }

    @Override
    public void onVictory() {
        wasApplied = false;
        stopPulse();
    }

    public AbstractRelic makeCopy() {
        return new RitualStick();
    }

    @Override
    public Integer onSave() {
        return ritualAmount;
    }

    @Override
    public void onLoad(Integer ints) {
        ritualAmount = ints;
        if(ints == 1) {
            hasCM = false;
        } else {
            hasCM = true;
        }
    }

    private void updateTip() {
        this.initializeTips();
        this.tips.clear();
        this.tips.add(new PowerTip(name, DESCRIPTIONS[0] + ritualAmount + DESCRIPTIONS[1]));
        this.initializeTips();
        this.description = getUpdatedDescription();
    }
}