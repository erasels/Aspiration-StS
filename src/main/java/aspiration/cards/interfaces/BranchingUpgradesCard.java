package aspiration.cards.interfaces;

import aspiration.patches.cards.BranchingUpgradesPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface BranchingUpgradesCard {

    void branchUpgrade();

    default void setBranchDescription() {}

    default void setIsBranchUpgrade() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            BranchingUpgradesPatch.BranchingUpgradeField.isBranchUpgraded.set(c, true);
            branchUpgrade();
            c.upgraded = true;
        }
    }

    default void displayBranchUpgrades() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            if (c.upgradedCost) {
                c.isCostModified = true;
            }
            if (c.upgradedBlock) {
                c.isBlockModified = true;
                c.baseBlock = c.block;
            }
            if (c.upgradedDamage) {
                c.isDamageModified = true;
                c.baseDamage = c.damage;
            }
            if (c.upgradedMagicNumber) {
                c.isMagicNumberModified = true;
                c.baseMagicNumber = c.magicNumber;
            }
        }
    }
}
