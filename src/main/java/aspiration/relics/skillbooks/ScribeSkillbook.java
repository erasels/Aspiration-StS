package aspiration.relics.skillbooks;

import The_Scribe.characters.TheScribe;
import The_Scribe.patches.LibraryTypeEnum;
import The_Scribe.powers.*;
import aspiration.Aspiration;
import aspiration.GeneralUtility.WeightedList;
import aspiration.patches.Fields.AbstractPowerScribeBookField;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Arrays;

import static aspiration.Aspiration.logger;

public class ScribeSkillbook extends SkillbookRelic implements OnReceivePowerRelic {
    public static final String ID = "aspiration:ScribeSkillbook";

    private static final int DMG_SPELL = 5;
    private static final int BLOCK_SPELL = 4;
    private static final int POISON_SPELL = 4;
    private static final int ENERGY_SPELL = 1;
    private static final int EFFECT_SPELL = 1;
    private static final int SPLIT_SPELL = 1;

    private boolean firstTrigger = true;
    private WeightedList<SpellsInterface> weightedEFFECTS;

    public ScribeSkillbook() {
        super(ID, "ScribeSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
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
    public boolean onReceivePower(AbstractPower pow, AbstractCreature source) {
        AbstractPlayer p = AbstractDungeon.player;
        if (pow.type == AbstractPower.PowerType.BUFF && !AbstractPowerScribeBookField.ssbTriggered.get(pow) && !(pow instanceof InvisiblePower)) {
            if (firstTrigger) {
                weightedEFFECTS = new WeightedList<>();
                weightedEFFECTS.addAll(new ArrayList<>(Arrays.asList(new SpellAttack(p, DMG_SPELL), new SpellBlock(p, BLOCK_SPELL), new SpellPoison(p, POISON_SPELL))), WeightedList.WEIGHT_COMMON);
                weightedEFFECTS.addAll(new ArrayList<>(Arrays.asList(new SpellEffectiveness(p, EFFECT_SPELL), new SpellClarity(p, ENERGY_SPELL))), WeightedList.WEIGHT_UNCOMMON);
                weightedEFFECTS.add(new SpellSplit(p, SPLIT_SPELL), WeightedList.WEIGHT_RARE);

                firstTrigger = false;
            }

            AbstractPower spell = ((CloneablePowerInterface) weightedEFFECTS.getRandom(AbstractDungeon.cardRandomRng)).makeCopy();
            AbstractPowerScribeBookField.ssbTriggered.set(spell, true);

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, spell, spell.amount));

        }
        return true;
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if (Aspiration.skillbookCardpool()) {
            logger.info("SpellScribe Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards = CardLibrary.getCardList(LibraryTypeEnum.SCRIBE_BLUE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn() {
        return !(AbstractDungeon.player instanceof TheScribe) && !hasSkillbookRelic(AbstractDungeon.player);
    }
}