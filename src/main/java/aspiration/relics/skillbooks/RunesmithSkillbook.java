package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import aspiration.actions.unique.EnhanceRandomCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import runesmith.character.player.RunesmithCharacter;
import runesmith.patches.LibraryEnum;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class RunesmithSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:RunesmithSkillbook";

    private static final int START_CHARGE = 0;
    private static final int PREP_CHARGE = 1;

    private boolean brokePrep = false;

    public RunesmithSkillbook() {
        super(ID, "RunesmithSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if(!brokePrep) {
            flash();
            manipCharge(PREP_CHARGE);
            stopPulse();
        }
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(c.type == AbstractCard.CardType.ATTACK) {
            brokePrep = true;
            stopPulse();

            if(this.counter > 0) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new EnhanceRandomCardInHandAction(counter, counter));
                //AbstractDungeon.actionManager.addToBottom(new RuneChannelAction(RuneOrb.getRandomRune(AbstractDungeon.relicRng, counter, true)));
                startingCharges();
            }
        }
    }

    @Override
    public void atTurnStart() {
        brokePrep = false;
        beginLongPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
        startingCharges();
    }

    @Override
    public void onEquip() {
        startingCharges();
        modifyCardPool();
    }

    public void modifyCardPool() {
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

    public AbstractRelic makeCopy() {
        return new RunesmithSkillbook();
    }
}