package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class SilentSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:SilentSkillbook";

    private static final int CPTHRESHOLD = 4;
    private static final int START_CHARGE = 0;

    public SilentSkillbook() {
        super(ID, "SilentSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + CPTHRESHOLD + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        } else {
            return DESCRIPTIONS[0] + CPTHRESHOLD + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction uac) {
        manipCharge(1);
        if(counter > 3) {
            beginLongPulse();
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        if(counter >= CPTHRESHOLD) {
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GamblingChipAction(AbstractDungeon.player));
        }
        startingCharges();
    }

    @Override
    public void onVictory() {
        startingCharges();
        stopPulse();
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Silent Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(CardLibrary.LibraryType.GREEN);
            mixCardpools(classCards);
        }
    }

    private void startingCharges()
    {
        setCounter(START_CHARGE);
    }

    private void manipCharge(int amt) {
        if (counter < 0) {
            counter = 0;
        }
        setCounter(counter + amt);
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof TheSilent) && !hasSkillbookRelic(AbstractDungeon.player);
    }
}