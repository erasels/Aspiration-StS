package aspiration.relics;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;

public class HangmansNoose extends AspirationRelic {
    public static final String ID = "aspiration:HangmansNoose";

    private static final int HP_LOSS_AMT = 2;

    public HangmansNoose() {
        super(ID, "HangmansNoose.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_LOSS_AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        for(AbstractMonster m: AbstractDungeon.getMonsters().monsters) {
            if(m.currentHealth<=(m.maxHealth/2)) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(m, new FlashPowerEffect(new ChokePower(m, 0)), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(m, AbstractDungeon.player, HP_LOSS_AMT, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new HangmansNoose();
    }
}