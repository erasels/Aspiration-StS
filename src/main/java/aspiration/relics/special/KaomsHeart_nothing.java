package aspiration.relics.special;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class KaomsHeart_nothing extends AspirationRelic {
    public static final String ID = "aspiration:KMNothing";

    public KaomsHeart_nothing() {
        super(ID, "KaomsHeart.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public AbstractRelic makeCopy() {
        return new KaomsHeart_nothing();
    }
}