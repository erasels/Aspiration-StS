package aspiration.relics.abstracts;

public interface OnEnergyUse {
    /**
     * @param amount        The amount of energy used
     * @return              The int returned modifies the amount of energy that should be used.
     */
    int onEnergyUse(int amount);
}
