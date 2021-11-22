package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import aspiration.actions.PowerAmountBasedHealAction;
import beaked.cards.Ceremony;
import beaked.characters.BeakedTheCultist;
import beaked.patches.LibraryTypeEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class BeakedSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:BeakedSkillbook";

    private static final int STR_AMT = 1;
    private boolean onlyAttacks = false;

    public BeakedSkillbook() {
        super(ID, "BeakedSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if (Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + STR_AMT + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        } else {
            return DESCRIPTIONS[0] + STR_AMT + DESCRIPTIONS[1];
        }
    }

    @Override
    public void atTurnStart() {
        if(onlyAttacks) {
            performAction();
        }
        onlyAttacks = true;
        beginLongPulse();
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(!(c.type == AbstractCard.CardType.ATTACK)) {
            onlyAttacks = false;
            stopPulse();
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
        onlyAttacks = false;
    }

    public void performAction() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR_AMT), STR_AMT));
        AbstractDungeon.actionManager.addToBottom(new PowerAmountBasedHealAction(AbstractDungeon.player, AbstractDungeon.player, StrengthPower.POWER_ID));
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
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
}