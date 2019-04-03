package aspiration.relics.common;

import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class ChemicalBlood extends AspirationRelic {
    public static final String ID = "aspiration:ChemicalBlood";

    private static final int PERCENTAGE_TRIGGER = 25;
    private static final float PERC = PERCENTAGE_TRIGGER/100F;
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
        flash();
        changeCharge(damageAmount);
    }

    @Override
    public void atTurnStart() {
        removePotionsFromRewards();
        potionReward();
    }

    private void startingCharges() {
        setCounter(healthPercent());
    }

    private void changeCharge(int amt) {
        setCounter(counter - amt);

        while(counter <= 0) {
            poverflow++;
            setCounter(counter + healthPercent());
        }
    }

    private void potionReward() {
        int freeSlots = 0;
        for (AbstractPotion p : AbstractDungeon.player.potions) {
            if (p instanceof PotionSlot) {
                freeSlots++;
            }
        }
        boolean openScreen = freeSlots < poverflow;

        for (int i = 0; i < poverflow; i++) {
            if (openScreen) {
                AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion());
            } else {
                AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion());
            }
        }
        if (openScreen) {
            AbstractDungeon.combatRewardScreen.open(DESCRIPTIONS[2]);
            AbstractDungeon.combatRewardScreen.rewards.removeIf(i -> i.type != RewardItem.RewardType.POTION);
            if(AbstractDungeon.combatRewardScreen.rewards.size()>poverflow) {
                int diff = AbstractDungeon.combatRewardScreen.rewards.size() - poverflow;
                if (diff > 0) {
                    AbstractDungeon.combatRewardScreen.rewards.subList(0, diff).clear();
                }
            }
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0;
        }
        flash();
    }

    private static void removePotionsFromRewards() {
        AbstractDungeon.getCurrRoom().rewards.removeIf(i -> i.type == RewardItem.RewardType.POTION);
        AbstractDungeon.combatRewardScreen.rewards.removeIf(i -> i.type == RewardItem.RewardType.POTION);
    }

    private int healthPercent() {
        //System.out.println("MHP: " + AbstractDungeon.player.maxHealth + " PT: " + PERC + " Erg: " + MathUtils.round(((float)AbstractDungeon.player.maxHealth)*PERC));
        return MathUtils.round(((float)AbstractDungeon.player.maxHealth)*PERC);
    }

    public AbstractRelic makeCopy() {
        return new ChemicalBlood();
    }
}