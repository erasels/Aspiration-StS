package aspiration.relics.common;

import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;
import org.apache.commons.lang3.math.NumberUtils;

public class ChemicalBlood extends AspirationRelic {
    public static final String ID = "aspiration:ChemicalBlood";

    private static final int PERCENTAGE_TRIGGER = 25;
    private static final float PERC = PERCENTAGE_TRIGGER / 100F;
    private int poverflow = 0;

    public ChemicalBlood() {
        super(ID, "ChemicalBlood.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PERCENTAGE_TRIGGER + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip() {
        startingCharges();
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            flash();
            changeCharge(damageAmount);
        }
    }

    @Override
    public void atTurnStart() {
        if (poverflow > 0) {
            potionReward();
        }
    }

    private void startingCharges() {
        setCounter(healthPercent());
    }

    private void changeCharge(int amt) {
        setCounter(counter - amt);

        while (counter <= 0) {
            poverflow++;
            setCounter(counter + healthPercent());
            if (poverflow <= freePSlots()) {
                potionReward();
            }
        }
    }

    private void potionReward() {
        AbstractRelic sozu = AbstractDungeon.player.getRelic(Sozu.ID);
        if (sozu != null) {
            sozu.flash();
            flash();
            return;
        }

        for (int i = 0; i < freePSlots(); i++) {
            AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion());
        }
        poverflow = 0;
        flash();
    }

    private int freePSlots() {
        int freeSlots = 0;
        for (AbstractPotion p : AbstractDungeon.player.potions) {
            if (p instanceof PotionSlot) {
                freeSlots++;
            }
        }
        return freeSlots;
    }

    private int healthPercent() {
        //System.out.println("MHP: " + AbstractDungeon.player.maxHealth + " PT: " + PERC + " Erg: " + MathUtils.round(((float)AbstractDungeon.player.maxHealth)*PERC));
        return NumberUtils.max(MathUtils.round(((float) AbstractDungeon.player.maxHealth) * PERC), 1);
    }
}