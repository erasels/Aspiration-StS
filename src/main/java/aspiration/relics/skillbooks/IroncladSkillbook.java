package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class IroncladSkillbook extends SkillbookRelic implements SkillbookRelicDescription {
    public static final String ID = "aspiration:IroncladSkillbook";

    private static final int STR_THRESHOLD = 15;
    private static final int STR_AMT = 1;
    private static final int HEAL_AMT = 6;

    public IroncladSkillbook() {
        super(ID, "IroncladSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + STR_THRESHOLD + DESCRIPTIONS[1] + STR_AMT +DESCRIPTIONS[2] + HEAL_AMT + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        } else {
            return DESCRIPTIONS[0] + STR_THRESHOLD + DESCRIPTIONS[1] + STR_AMT +DESCRIPTIONS[2] + HEAL_AMT + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if(AbstractDungeon.player.currentBlock >= STR_THRESHOLD) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR_AMT), STR_AMT));
        }
    }

    @Override
    public void onVictory() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        //Base game workaround for Actionmanagaer not working outside of combat
        if (AbstractDungeon.player.currentHealth > 0) {
            AbstractDungeon.player.heal(HEAL_AMT);
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    private void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Ironclad Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(CardLibrary.LibraryType.RED);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof Ironclad) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new IroncladSkillbook();
    }
}