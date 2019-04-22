package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import aspiration.actions.unique.DumbApplyPowerAction;
import aspiration.orbs.OrbUtilityMethods;
import aspiration.powers.ChannelOrbPower;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class DefectSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:DefectSkillbook";

    private static final int ORB_SLOTS = 2;
    private static final int POWER_TURN = 3;
    //private ArrayList<Integer> powerTriggers = new ArrayList<>();

    public DefectSkillbook() {
        super(ID, "DefectSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if (Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + ORB_SLOTS + DESCRIPTIONS[1] + POWER_TURN + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[0] + ORB_SLOTS + DESCRIPTIONS[1] + POWER_TURN + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.POWER) {
            flash();
            //powerTriggers.add(POWER_TURN);
            AbstractDungeon.actionManager.addToBottom(new DumbApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChannelOrbPower(AbstractDungeon.player, POWER_TURN), -1, true));
        }
    }

    /*@Override
    public void atTurnStart() {
        if(powerTriggers.size() > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            for(int i = 0; i<powerTriggers.size(); i++) {
                powerTriggers.set(i, (powerTriggers.get(i)-1));
                AbstractDungeon.actionManager.addToBottom(new ChannelAction(OrbUtilityMethods.getSelectiveRandomOrb(AbstractDungeon.relicRng)));
            }
            powerTriggers.removeIf(power -> power<=0);
        }
    }*/

    /*@Override
    public void onVictory() {
        powerTriggers.clear();
    }*/

    @Override
    public void atPreBattle() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(ORB_SLOTS));
    }

    @Override
    public void atBattleStart() {
        for (int i = 0; i < ORB_SLOTS; i++) {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(OrbUtilityMethods.getWeightedRandomOrb(AbstractDungeon.cardRandomRng, false)));
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if (Aspiration.skillbookCardpool()) {
            logger.info("Defect Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards = CardLibrary.getCardList(CardLibrary.LibraryType.BLUE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn() {
        return !(AbstractDungeon.player instanceof Defect) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new DefectSkillbook();
    }
}