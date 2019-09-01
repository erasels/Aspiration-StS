package aspiration.relics.uncommon;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class QuestionableFork extends AspirationRelic {
    public static final String ID = "aspiration:QuestionableFork";

    public QuestionableFork() {
        super(ID, "QuestionableFork.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }



    public AbstractRelic makeCopy() {
        return new QuestionableFork();
    }
}