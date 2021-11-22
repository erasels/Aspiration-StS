package aspiration.relics.uncommon;

import aspiration.Aspiration;
import aspiration.powers.HoverPower;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HummingbirdHeart extends AspirationRelic {
    public static final String ID = "aspiration:HummingbirdHeart";

    private static final int START_CHARGE = -1;
    private static final int TURN_AMT = 3;

    private final boolean isBalanced = CardCrawlGame.playerName.toLowerCase().equals("Lobbien".toLowerCase()) || CardCrawlGame.playerName.toLowerCase().equals("Jumble".toLowerCase());

    public HummingbirdHeart() {
        super(ID, "HummingbirdHeart.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (TURN_AMT - 1) + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip() {
        startingCharges();
    }

    @Override
    public void atPreBattle() {
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("HEART_SIMPLE"));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HoverPower(AbstractDungeon.player)));
        setCounter(TURN_AMT);
    }

    @Override
    public void atTurnStart() {
        if (isBalanced) {
            Aspiration.logger.info("This balanced now? " + CardCrawlGame.playerName);
            return;
        }

        manipCharge(-1);
        if (GameActionManager.turn >= TURN_AMT - 1 && AbstractDungeon.player.getPower(HoverPower.POWER_ID) != null) {
            manipCharge(-1);
            flash();
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(HoverPower.POWER_ID)));
        }
    }

    @Override
    public void onVictory() {
        startingCharges();
        if (AbstractDungeon.player.hasPower(HoverPower.POWER_ID)) {
            AbstractDungeon.player.getPower(HoverPower.POWER_ID).onRemove();
        }
    }

    private void startingCharges() {
        setCounter(START_CHARGE);
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);
    }
}