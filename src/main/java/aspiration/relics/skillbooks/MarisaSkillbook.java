package aspiration.relics.skillbooks;

import ThMod.characters.Marisa;
import ThMod.patches.LibraryTypeEnum;
import ThMod.powers.Marisa.ChargeUpPower;
import aspiration.Aspiration;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class MarisaSkillbook extends SkillbookRelic /*implements OnEnergyUse*/ {
    public static final String ID = "aspiration:MarisaSkillbook";

    private static final int CHARGE_STACK = 1;

    public MarisaSkillbook() {
        super(ID, "MarisaSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        int tmp;
        if(card.freeToPlayOnce) {
            tmp = 0;
        } else if(card.cost == -1) {
            tmp = card.energyOnUse;
        } else {
            tmp = card.costForTurn;
        }

        triggerChargeup(CHARGE_STACK + tmp);
    }

    /*@Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if (source == AbstractDungeon.player && p.type == AbstractPower.PowerType.DEBUFF&& !target.hasPower(ArtifactPower.POWER_ID)) {
            triggerChargeup(CHARGE_STACK);
        }
    }

    @Override
    public void onExhaust(AbstractCard card) {
        triggerChargeup(CHARGE_STACK);
    }*/

    /*@Override
    public int onEnergyUse(int amount) {
        triggerChargeup(amount > EnergyPanel.totalCount ? EnergyPanel.totalCount : amount);
        return amount;
    }*/

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if (Aspiration.skillbookCardpool()) {
            logger.info("Marisa Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards = CardLibrary.getCardList(LibraryTypeEnum.MARISA_COLOR);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn() {
        return !(AbstractDungeon.player instanceof Marisa) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public void triggerChargeup(int i) {
        flash();
        //AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargeUpPower(AbstractDungeon.player, i), i));
    }

    public AbstractRelic makeCopy() {
        return new MarisaSkillbook();
    }
}