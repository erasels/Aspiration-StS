package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;

public class CullingKillCheckAction extends AbstractGameAction
{
    private AbstractCreature target;

    public CullingKillCheckAction(AbstractCreature target) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
    }

    @Override
    public void update() {
        if(target != null && !target.isDead) {
            if(target instanceof  AbstractMonster && !target.isDying) {
                if (!(((AbstractMonster) target).name.equals(Darkling.NAME))) {
                    ((AbstractMonster) target).die();
                    target.hideHealthBar();
                }
            }
        }
        this.isDone = true;
    }
}