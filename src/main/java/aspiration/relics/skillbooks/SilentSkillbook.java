package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.green.Envenom;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class SilentSkillbook extends SkillbookRelic implements SkillbookRelic_Interface {
    public static final String ID = "aspiration:SilentSkillbook";

    private static final int SHIV_AMT = 1;
    private static final int ENV_AMT = 1;

    public SilentSkillbook() {
        super(ID, "SilentSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        tips.add(new PowerTip(Envenom.NAME, Envenom.DESCRIPTION));
        initializeTips();
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
    public void atPreBattle() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnvenomPower(AbstractDungeon.player,ENV_AMT), ENV_AMT));
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Shiv(), SHIV_AMT, false));
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    private void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Silent Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(CardLibrary.LibraryType.GREEN);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof TheSilent) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new SilentSkillbook();
    }
}