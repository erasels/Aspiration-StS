package aspiration.powers;

import aspiration.actions.unique.CullingKillCheckAction;
import aspiration.powers.abstracts.AspirationPower;
import aspiration.relics.unfinished.HeadsmansAxe;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;

public class CullingPower extends AspirationPower implements CloneablePowerInterface {
    public static final String POWER_ID = "aspiration:Culling";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private float cullingThreshold;

    public CullingPower(AbstractCreature owner, float cullingThreshold) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.isTurnBased = true;
        this.cullingThreshold = cullingThreshold;
        updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        loadRegion("minion");
    }

    public CullingPower(AbstractCreature owner) {
        this(owner, 0.25f);
    }

    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + MathUtils.round(cullingThreshold*100) + DESCRIPTIONS[1]);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && target != owner) {
            if(target instanceof AbstractMonster) {
                if (((AbstractMonster) target).type != AbstractMonster.EnemyType.BOSS) {
                    if ((target.currentHealth - damageAmount) <= (target.maxHealth * cullingThreshold)) {
                        if(owner == AbstractDungeon.player && AbstractDungeon.player.hasRelic(HeadsmansAxe.ID)) {
                            AbstractDungeon.player.getRelic(HeadsmansAxe.ID).flash();
                        }
                        target.currentHealth = 0;
                        AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(owner, 999, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE, true, true));
                        AbstractDungeon.actionManager.addToTop(new VFXAction(target, new FlashPowerEffect(new MinionPower(target)), 0.0F));
                        AbstractDungeon.actionManager.addToBottom(new CullingKillCheckAction(target));
                    }
                }
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CullingPower(owner, cullingThreshold);
    }

    public static String getDesc(float percentage) {
        return (DESCRIPTIONS[0] + MathUtils.round(percentage*100) + DESCRIPTIONS[1]);
    }
}
