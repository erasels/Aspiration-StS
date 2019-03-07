package aspiration.relics.abstracts;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnReducePower {
    /**
     * @param powerInstance The Instance of the power that is being reduced
     * @param amount        The amount the power should be reduced by
     * @return              The int returned modifies the amount the power should be reduced by
     */
    int OnReducePower(AbstractPower powerInstance, int amount);
}
