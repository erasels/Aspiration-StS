package aspiration.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class UnbridledHammerAction extends AbstractGameAction
{
    private boolean freeToPlayOnce;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean upgraded;
    private int damage;
    private DamageInfo.DamageType damageTypeForTurn;

    public UnbridledHammerAction(AbstractPlayer p, int damage, DamageInfo.DamageType damageTypeForTurn, int energyOnUse, boolean upgraded, boolean freeToPlayOnce)
    {
        this.p = p;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.freeToPlayOnce = freeToPlayOnce;
        this.damage = damage;
        this.damageTypeForTurn = damageTypeForTurn;
    }

    public void update()
    {
        if (energyOnUse < EnergyPanel.getCurrentEnergy()) {
            energyOnUse = EnergyPanel.getCurrentEnergy();
        }
        int effect = energyOnUse;
        if(upgraded) effect++;
        if(p.hasRelic(ChemicalX.ID)) {
            p.getRelic(ChemicalX.ID).flash();
            effect += 2;
        }
        if(effect > 0) {
            for (int i = 0; i < effect; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.getMonsters().getRandomMonster(true), new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
            AbstractDungeon.actionManager.addToBottom(new EnhanceRandomCardInHandAction(1, effect));
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}
