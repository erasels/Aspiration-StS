package aspiration.relics.skillbooks;

import The_Scribe.characters.TheScribe;
import The_Scribe.patches.LibraryTypeEnum;
import The_Scribe.powers.*;
import aspiration.Aspiration;
import aspiration.GeneralUtility.WeightedList;
import aspiration.patches.Fields.AbstractPowerScribeBookField;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Arrays;

import static aspiration.Aspiration.logger;

public class ScribeSkillbook extends SkillbookRelic {
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
    public void onApplyPower(AbstractPower pow, AbstractCreature target, AbstractCreature source) {
        AbstractPlayer p = AbstractDungeon.player;
        if (pow.type == AbstractPower.PowerType.BUFF && target == p && !AbstractPowerScribeBookField.ssbTriggered.get(pow) && !(pow instanceof InvisiblePower)) {
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

    public AbstractRelic makeCopy() {
        return new ScribeSkillbook();
    }
}


//boolean havePow = true;
//boolean rarePow = false;
//int tmp;
            /*while(havePow) {
                havePow = false;
                tmp = rng.random(1, SPELL_EFFECT_AMT);
                if(tmp == SPELL_EFFECT_AMT) {
                    if(!rarePow) {
                        rarePow = true;
                        havePow = true;
                        continue;
                    }
                } else {
                    rarePow = false;
                }
                //logger.info("num: " + tmp + "rare: " + rarePow + " triggered: " + AbstractPowerScribeBookField.ssbTriggered.get(pow) + " name: " + pow.name);

                switch (tmp) {
                    case 1:
                        SpellAttack sa = new SpellAttack(p, DMG_SPELL);
                        AbstractPowerScribeBookField.ssbTriggered.set(sa, true);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, sa, DMG_SPELL));
                        break;
                    case 2:
                        SpellBlock sb = new SpellBlock(p, BLOCK_SPELL);
                        AbstractPowerScribeBookField.ssbTriggered.set(sb, true);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, sb, BLOCK_SPELL));
                        break;
                    case 3:
                        SpellPoison sp = new SpellPoison(p, POISON_SPELL);
                        AbstractPowerScribeBookField.ssbTriggered.set(sp, true);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, sp, POISON_SPELL));
                        break;
                    case 4:
                        SpellClarity sc = new SpellClarity(p, ENERGY_SPELL);
                        AbstractPowerScribeBookField.ssbTriggered.set(sc, true);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, sc, ENERGY_SPELL));
                        break;
                    case 5:
                        SpellEffectiveness se = new SpellEffectiveness(p, EFFECT_SPELL);
                        AbstractPowerScribeBookField.ssbTriggered.set(se, true);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, se, EFFECT_SPELL));
                        break;
                    case 6:
                        SpellSplit ss = new SpellSplit(p, SPLIT_SPELL);
                        AbstractPowerScribeBookField.ssbTriggered.set(ss, true);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, ss, SPLIT_SPELL));
                        break;
                }
            }*/