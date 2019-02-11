package aspiration.actions;

import aspiration.relics.FutureDiary;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class FutureDiaryAction extends AbstractGameAction
{
    private AbstractPlayer p;
    private ArrayList<AbstractCard> playabaleCards = new ArrayList<>();

    public FutureDiaryAction()
    {
        p = AbstractDungeon.player;
    }

    public void update()
    {
        for(AbstractCard c : AbstractDungeon.player.hand.group) {
            if(c.canUse(p, (AbstractMonster) target)) {
                playabaleCards.add(c);
            }
        }
        FutureDiary fd = (FutureDiary) p.getRelic(FutureDiary.ID);
        fd.setPlayableCards(playabaleCards);
        this.isDone = true;
    }
}