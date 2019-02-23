package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Envenom;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import runesmith.character.player.RunesmithCharacter;
import runesmith.patches.LibraryEnum;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class RunesmithSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:RunesmithSkillbook";

    private static final int SHIV_AMT = 1;
    private static final int ENV_AMT = 1;

    public RunesmithSkillbook() {
        super(ID, "RunesmithSkillbook.png", RelicTier.BOSS, LandingSound.FLAT, new PowerTip(Envenom.NAME, Envenom.DESCRIPTION));
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + SHIV_AMT + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        } else {
            return DESCRIPTIONS[0] + SHIV_AMT + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    private void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Runesmith Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryEnum.RUNESMITH_BEIGE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof RunesmithCharacter) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new RunesmithSkillbook();
    }
}