package aspiration.relics.skillbooks;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;

public interface SkillbookRelic_Interface {
    class data
    {
        private static final String ID = "aspiration:Skillbook";
        private static final String[] DESCRIPTIONS;
        static
        {
            RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
            DESCRIPTIONS = relicStrings.DESCRIPTIONS;
        }
    }

    default String[] SKILLBOOK_DESCRIPTIONS()
    {
        return data.DESCRIPTIONS;
    }
}
