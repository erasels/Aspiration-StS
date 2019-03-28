package aspiration.cards.Runesmith;

import aspiration.Aspiration;
import aspiration.actions.unique.UnbridledHammerAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import runesmith.patches.AbstractCardEnum;

import static runesmith.patches.CardTagEnum.RS_HAMMER;

public class UnbridledHammer extends CustomCard {
    public static final String ID = "aspiration:UnbridledHammer";
    public static final String IMG = Aspiration.assetPath("img/cards/UnbridledHammer.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = -1;
    private static final int DAMAGE = 5;

    public UnbridledHammer() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.RUNESMITH_BEIGE, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = DAMAGE;
        tags.add(RS_HAMMER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new UnbridledHammerAction(p, damage, damageTypeForTurn, energyOnUse, upgraded, freeToPlayOnce));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnbridledHammer();
    }
}