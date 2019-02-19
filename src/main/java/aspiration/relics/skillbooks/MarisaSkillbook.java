package aspiration.relics.skillbooks;

import ThMod.characters.Marisa;
import ThMod.patches.LibraryTypeEnum;
import ThMod.powers.Marisa.ChargeUpPower;
import ThMod.relics.SimpleLauncher;
import aspiration.Aspiration;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class MarisaSkillbook extends SkillbookRelic implements SkillbookRelic_Interface {
    public static final String ID = "aspiration:MarisaSkillbook";

    private static final int CHARGE_STACK = 1;

    public MarisaSkillbook() {
        super(ID, "MarisaSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SKILLBOOK_DESCRIPTIONS()[0], SKILLBOOK_DESCRIPTIONS()[1]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + CHARGE_STACK + DESCRIPTIONS[1] +DESCRIPTIONS[2] ;
        } else {
            return DESCRIPTIONS[0] + CHARGE_STACK + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        Boolean available = true;
        int div = 8;
        if (AbstractDungeon.player.hasRelic(SimpleLauncher.ID)) {
            div = 6;
        }
        if (AbstractDungeon.player.hasPower(ChargeUpPower.POWER_ID)) {
            if (AbstractDungeon.player.getPower(ChargeUpPower.POWER_ID).amount >= div) {
                //available = false;
            }
        }
        if (available) {
            triggerChargeup(CHARGE_STACK);
        }
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (source == AbstractDungeon.player && p.type == AbstractPower.PowerType.DEBUFF&& !target.hasPower(ArtifactPower.POWER_ID)) {
            triggerChargeup(CHARGE_STACK);
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    private void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Marisa Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryTypeEnum.MARISA_COLOR);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof Marisa) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public void triggerChargeup(int i) {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargeUpPower(AbstractDungeon.player, i), i));
    }

    public AbstractRelic makeCopy() {
        return new MarisaSkillbook();
    }

    /*@Override
    public Integer onSave() {
        System.out.println("Save:");
        for(AbstractCard c : AbstractDungeon.srcCommonCardPool.group ) {
            System.out.println(c.name);
        }

        return 1;
    }

    @Override
    public void onLoad(Integer o) {
        System.out.println("Pre load");
        for(AbstractCard c : AbstractDungeon.srcCommonCardPool.group ) {
            System.out.println(c.name);
        }
        modifyCardPool();
        System.out.println("Post-load");
        for(AbstractCard c : AbstractDungeon.srcCommonCardPool.group ) {
            System.out.println(c.name);
        }
    }*/
}