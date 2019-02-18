package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import beaked.cards.Ceremony;
import beaked.patches.LibraryTypeEnum;
import beaked.characters.BeakedTheCultist;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class BeakedSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:BeakedSkillbook";

    public BeakedSkillbook() {
        super(ID, "BeakedSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if (Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    private void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Beaked Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryTypeEnum.BEAKED_YELLOW);
            mixCardpools(classCards);
            AbstractCard ceremony = new Ceremony();
            AbstractDungeon.commonCardPool.addToTop(ceremony);
            AbstractDungeon.srcCommonCardPool.addToBottom(ceremony);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof BeakedTheCultist) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new BeakedSkillbook();
    }
}