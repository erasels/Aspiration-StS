package aspiration.cards.blue;

import aspiration.Aspiration;
import aspiration.actions.FuseValidOrbsAction;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Polymerization extends CustomCard {
    public static final String ID = "aspiration:Polymerization";
    public static final String IMG = Aspiration.assetPath("img/cards/Polymerization.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPG_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int EXH_AMT = 2;

    public Polymerization() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.filledOrbCount() > 1) {
            AbstractDungeon.actionManager.addToTop(new FuseValidOrbsAction());
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPG_DESCRIPTION;
            this.exhaust = false;
            if(timesUpgraded < 1) {
                ExhaustiveVariable.setBaseValue(this, EXH_AMT);
            } else {
                ExhaustiveVariable.setBaseValue(this, EXH_AMT + timesUpgraded - 1);
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Polymerization();
    }
}