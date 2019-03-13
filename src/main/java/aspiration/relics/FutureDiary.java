package aspiration.relics;

import aspiration.actions.unique.FutureDiaryAction;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;

import java.util.ArrayList;

public class FutureDiary extends AspirationRelic {
    public static final String ID = "aspiration:FutureDiary";
    private AbstractCard mustPlay = null;
    private ArrayList<AbstractCard> playabaleCards = new ArrayList<>();

    public FutureDiary() {
        super(ID, "FutureDiary.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new FutureDiaryAction());
    }

    @Override
    public boolean canPlay(AbstractCard card)
    {
        if (mustPlay != null) {
            if (mustPlay.equals(card)) {
                return true;
            }
            card.cantUseMessage = DESCRIPTIONS[1] + mustPlay.name + DESCRIPTIONS[2];
            return false;
        }
        return true;
    }

    @Override
    public void onPlayCard(AbstractCard targetCard, AbstractMonster m){
        if(targetCard != null && targetCard.equals(mustPlay)) {
            mustPlay = null;
        }
    }

    public void setCompulsion(AbstractCard card)
    {
        mustPlay = card;
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f, DESCRIPTIONS[1] + mustPlay.name + DESCRIPTIONS[2], true));
        AbstractDungeon.effectsQueue.add(new CardFlashVfx(card));
    }

    public void setPlayableCards(ArrayList<AbstractCard> pc) {
        playabaleCards = pc;

        Random rng = AbstractDungeon.cardRng;
        if(playabaleCards.size()>0) {
            setCompulsion(playabaleCards.get(rng.random(playabaleCards.size() - 1)));
        }
    }

    @Override
    public void onPlayerEndTurn() {
        mustPlay = null;
    }

    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    public AbstractRelic makeCopy() {
        return new FutureDiary();
    }
}