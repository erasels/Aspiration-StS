package aspiration.actions.unique;

import aspiration.relics.skillbooks.BeakedSkillbook;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BeakedSkillbookAction extends AbstractGameAction
{

    private AbstractCard.CardType card_type;
    private int threshold;

    public BeakedSkillbookAction(AbstractCard.CardType cT, int threshold)
    {
        card_type = cT;
        this.threshold = threshold;
    }

    public void update()
    {
        boolean triggered = true;
        for(AbstractCard c :AbstractDungeon.player.hand.group) {
            if(!(c.type == card_type)) {
                threshold--;
                if(threshold <= 0) {
                    triggered = false;
                    break;
                }
            }
        }

        if(triggered) {
            BeakedSkillbook bsk = (BeakedSkillbook) AbstractDungeon.player.getRelic(BeakedSkillbook.ID);
            bsk.performAction();
        }
        this.isDone = true;
    }
}