package aspiration.relics.special;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ArtOfWar;

public class ArtOfWarUpgrade extends AspirationRelic {
    public static final String ID = "aspiration:ArtOfWarUpgrade";

    private static final int ENERGY_AMOUNT = 1;
    private boolean firstTurn = false;
    private boolean gainEnergyNext = false;

    public ArtOfWarUpgrade() {
        super(ID, "ArtOfWarUpgrade.png", RelicTier.SPECIAL, LandingSound.FLAT);
        this.pulse = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void atPreBattle()
    {
        flash();
        this.firstTurn = true;
        this.gainEnergyNext = true;
        if (!this.pulse)
        {
            beginLongPulse();
            this.pulse = true;
        }
    }

    public void atTurnStart()
    {
        beginLongPulse();
        this.pulse = true;
        if ((this.gainEnergyNext) && (!this.firstTurn))
        {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_AMOUNT));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleDamagePower(AbstractDungeon.player, ENERGY_AMOUNT, false)));
        }
        this.firstTurn = false;
        this.gainEnergyNext = true;
    }

    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            this.gainEnergyNext = false;
            this.pulse = false;
        }
    }

    public void onVictory()
    {
        this.pulse = false;
    }

    public AbstractRelic makeCopy() {
        return new ArtOfWarUpgrade();
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(ArtOfWar.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(ArtOfWar.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        }
        else {
            super.obtain();
        }
    }
}
