package aspiration.cards.interfaces;

import aspiration.Aspiration;
import aspiration.patches.Fields.AbstractCardFields;
import aspiration.patches.cards.BranchingUpgradesPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface BranchingUpgradesCard {

    void branchUpgrade();

    default void setBranchDescription() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            int rep = AbstractCardFields.repeats.get(c);
            if(rep > 0) {
                c.rawDescription += Aspiration.RepeatsAddendum + rep;
            }
            c.initializeDescription();
        }
    }

    default void setIsBranchUpgrade() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            BranchingUpgradesPatch.BranchingUpgradeField.isBranchUpgraded.set(c, true);
            branchUpgrade();
            setBranchDescription();
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
                c.block = c.baseBlock;
            }
            if (c.upgradedDamage) {
                c.isDamageModified = true;
                c.damage = c.baseDamage;
            }
            if (c.upgradedMagicNumber) {
                c.isMagicNumberModified = true;
                c.magicNumber = c.baseMagicNumber;
            }
        }
    }
}
