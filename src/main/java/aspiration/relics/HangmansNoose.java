package aspiration.relics;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ChokePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;

public class HangmansNoose extends AspirationRelic {
    public static final String ID = "aspiration:HangmansNoose";

    private static final int HP_LOSS_AMT = 3;

    public HangmansNoose() {
        super(ID, "HangmansNoose.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_LOSS_AMT + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(info.type == DamageInfo.DamageType.NORMAL && (info.owner.currentHealth<=(info.owner.maxHealth/2))) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new VFXAction(info.owner, new FlashPowerEffect(new ChokePower(info.owner, 0)), 0.0F));
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(info.owner, AbstractDungeon.player, HP_LOSS_AMT, AbstractGameAction.AttackEffect.NONE));
        }
        return damageAmount;
    }

    public AbstractRelic makeCopy() {
        return new HangmansNoose();
    }
}