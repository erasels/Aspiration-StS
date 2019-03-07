package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import aspiration.relics.abstracts.OnReducePower;
import blackrusemod.characters.TheServant;
import blackrusemod.patches.LibraryTypeEnum;
import blackrusemod.powers.ProtectionPower;
import blackrusemod.powers.SatellitePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class ServantSkillbook extends SkillbookRelic implements OnReducePower {
    public static final String ID = "aspiration:ServantSkillbook";

    private static final int SAT_AMT = 3;
    private static final int SAT_ATK = 4;
    private boolean took_dmg = true;

    public ServantSkillbook() {
        super(ID, "ServantSkillbook.png", RelicTier.BOSS, LandingSound.FLAT, new PowerTip(SatellitePower.NAME, SatellitePower.DESCRIPTIONS[0] + SAT_ATK + SatellitePower.DESCRIPTIONS[1]));
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
    public int OnReducePower(AbstractPower powerInstance, int amount) {
        if(powerInstance.ID.equals(SatellitePower.POWER_ID)) {
            int satDamage = SAT_ATK;
            try {
                Field satDmg = SatellitePower.class.getDeclaredField("damage");
                satDmg.setAccessible(true);
                satDamage = (int) satDmg.get(powerInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ProtectionPower(AbstractDungeon.player, satDamage), satDamage));
        }
        return amount;
    }

    @Override
    public void atTurnStart() {
        if(!took_dmg) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SatellitePower(AbstractDungeon.player, SAT_AMT), SAT_AMT));
        }
        took_dmg = false;
        beginLongPulse();
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
        took_dmg = true;
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
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