package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import blackrusemod.characters.TheServant;
import blackrusemod.patches.LibraryTypeEnum;
import blackrusemod.powers.SatellitePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class ServantSkillbook extends SkillbookRelic implements SkillbookRelic_Interface {
    public static final String ID = "aspiration:ServantSkillbook";

    private static final int SAT_AMT = 4;
    private static final int SAT_ATK = 4;
    private boolean took_dmg = true;

    public ServantSkillbook() {
        super(ID, "ServantSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        tips.add(new PowerTip(SatellitePower.NAME, SatellitePower.DESCRIPTIONS[0] + SAT_ATK + SatellitePower.DESCRIPTIONS[1]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + SAT_AMT + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        } else {
            return DESCRIPTIONS[0] + SAT_AMT + DESCRIPTIONS[1];
        }
    }

    @Override
    public void atTurnStart() {
        if(!took_dmg) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SatellitePower(AbstractDungeon.player, SAT_AMT), SAT_AMT));
        }
        took_dmg = false;
        beginPulse();
    }

    @Override
    public void onLoseHp(int dmg) {
        if(dmg > 0) {
            took_dmg = true;
            stopPulse();
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public void atPreBattle() {
        if(!took_dmg) {
            beginPulse();
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    private void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Servant Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryTypeEnum.SILVER);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof TheServant) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new ServantSkillbook();
    }
}