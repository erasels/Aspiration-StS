package aspiration.relics.shop;

import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Earplugs extends AspirationRelic implements OnReceivePowerRelic {
    public static final String ID = "aspiration:Earplugs";

    public Earplugs() {
        super(ID, "Earplugs.png", RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean onReceivePower(AbstractPower p, AbstractCreature target) {
        if (target == AbstractDungeon.player && p instanceof InspirationPower) {
            int amt = calcAmt((InspirationPower) p);
            for (int i = 0; i < p.amount; i++) {
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, amt, false), amt));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, amt, false), amt));
            }
            return false;
        }
        return true;
    }

    private int calcAmt(InspirationPower p) {
        return (p.amount2 / 25);
    }

    public AbstractRelic makeCopy() {
        return new Earplugs();
    }
}