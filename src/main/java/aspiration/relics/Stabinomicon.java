package aspiration.relics;

import aspiration.Aspiration;
import aspiration.actions.RelicTalkAction;
import aspiration.powers.GrievousWoundsPower;
import aspiration.relics.abstracts.AspirationRelic;
import aspiration.vfx.dialog.RelicSpeechBubble;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Random;

public class Stabinomicon extends AspirationRelic {
    public static final String ID = "aspiration:Stabinomicon";
    private static final int GW_STACK = 2;
    private Random rng = new Random();
    private boolean hasSpoken = false;

    public Stabinomicon() {
        super(ID, "Stabinomicon.png", RelicTier.SPECIAL, LandingSound.FLAT);
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(GrievousWoundsPower.NAME, GrievousWoundsPower.getDesc()));
        initializeTips();
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + GW_STACK + this.DESCRIPTIONS[1];
    }


    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if ((damageAmount > 0) && (target != AbstractDungeon.player) && (info.type == DamageInfo.DamageType.NORMAL)) {
            if (rng.nextInt(20) == 0) {
                try {
                    AbstractDungeon.effectList.add(new RelicSpeechBubble(this.hb.cX, this.hb.cY, new String(new char[this.rng.nextInt(7) + 1]).replace("\0", DESCRIPTIONS[3]).trim()));
                } catch (Exception e) {
                    Aspiration.logger.info(e);
                }
            }
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(target, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new GrievousWoundsPower(target, GW_STACK), GW_STACK));
        }
    }

    public void atBattleStart() {
        if (!this.hasSpoken) {
            AbstractDungeon.actionManager.addToBottom(new RelicTalkAction(this, DESCRIPTIONS[2]));
            this.hasSpoken = true;
        } else if (rng.nextInt(10) == 0) {
            this.hasSpoken = false;
        }
    }


    public AbstractRelic makeCopy() {
        return new Stabinomicon();
    }
}
