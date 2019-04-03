package aspiration.cards.green;

import aspiration.Aspiration;
import aspiration.actions.SpawnTolerantDamageAllEnemiesAction;
import aspiration.vfx.combat.LungeEffect;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class Lunge extends CustomCard {
    public static final String ID = "aspiration:Lunge";
    public static final String IMG = Aspiration.assetPath("img/cards/Lunge.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int DAMAGE = 11;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int ATK_AMT = 2;

    public Lunge() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardColor.GREEN, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.exhaust = true;
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int monc = 0;
        for(AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if(!mon.isDeadOrEscaped()) {
                monc++;
            }
        }

        for(int i = 0; i < ATK_AMT; i++) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new LungeEffect(AbstractDungeon.getMonsters().monsters.get(AbstractDungeon.getMonsters().monsters.size()-1).hb.cX + (200F * Settings.scale), AbstractDungeon.getMonsters().monsters.get(AbstractDungeon.getMonsters().monsters.size()-1).hb.cY, Color.GREEN)));
            //AbstractDungeon.actionManager.addToBottom(new VFXAction(new ClashEffect(middleX, middleY), 0.15F));
            AbstractDungeon.actionManager.addToBottom(new SpawnTolerantDamageAllEnemiesAction(p, damage, false, false, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE, false));
        }

        monc*=-1;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, monc), monc, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Lunge();
    }
}