package aspiration.relics.skillbooks;

import The_Scribe.characters.TheScribe;
import The_Scribe.patches.LibraryTypeEnum;
import The_Scribe.powers.*;
import aspiration.Aspiration;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class ScribeSkillbook extends SkillbookRelic implements OnReceivePowerRelic {
    public static final String ID = "aspiration:ScribeSkillbook";

    private static final int SPELL_EFFECT_AMT = 6;

    private static final int DMG_SPELL = 5;
    private static final int BLOCK_SPELL = 4;
    private static final int POISON_SPELL = 4;
    private static final int ENERGY_SPELL = 1;
    private static final int EFFECT_SPELL = 1;
    private static final int SPLIT_SPELL = 1;

    public ScribeSkillbook() {
        super(ID, "ScribeSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower pow, AbstractCreature source) {
        if(pow.type == AbstractPower.PowerType.BUFF) {
            AbstractPlayer p = AbstractDungeon.player;

            boolean havePow = true;
            boolean rarePow = false;
            Random rng = AbstractDungeon.cardRandomRng;
            int tmp;

            while(havePow) {
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
                //logger.info("num: " + tmp + "rare: " + rarePow);

                switch (tmp) {
                    case 1:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellAttack(p, DMG_SPELL), DMG_SPELL));
                        break;
                    case 2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellBlock(p, BLOCK_SPELL), BLOCK_SPELL));
                        break;
                    case 3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellPoison(p, POISON_SPELL), POISON_SPELL));
                        break;
                    case 4:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellClarity(p, ENERGY_SPELL), ENERGY_SPELL));
                        break;
                    case 5:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p, EFFECT_SPELL), EFFECT_SPELL));
                        break;
                    case 6:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSplit(p, SPLIT_SPELL), SPLIT_SPELL));
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("SpellScribe Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(LibraryTypeEnum.SCRIBE_BLUE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof TheScribe) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new ScribeSkillbook();
    }
}