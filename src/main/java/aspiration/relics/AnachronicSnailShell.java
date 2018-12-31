package aspiration.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import aspiration.relics.abstracts.AspirationRelic;

public class AnachronicSnailShell extends AspirationRelic {
	public static final String ID = "aspiration:AnachronicSnailShell";
	
	private static final int START_CHARGE = 0;

    public AnachronicSnailShell() {
        super(ID, "AnachronicSnailShell.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FontHelper.colorString(DESCRIPTIONS[1], "r");
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
    	if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) && (damageAmount > 0) && (AbstractDungeon.player.currentBlock < damageAmount) && !(AbstractDungeon.player.endTurnQueued) && info.owner != AbstractDungeon.player) {
    		flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("POWER_TIME_WARP"));
            
            manipCharge(damageAmount - AbstractDungeon.player.currentBlock);
            return 0;
    	}
    	return damageAmount;
    }
    
    @Override
    public void onPlayerEndTurn()
    {
    	if(counter > 0) {
    		AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), counter, DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
    		startingCharges();
    	}
    }
    
    @Override
    public void onVictory() {
    	startingCharges();
    }
    
    @Override
    public void onEquip()
    {
        startingCharges();
    }
    
    private void startingCharges()
    {
        setCounter(START_CHARGE);
    }
    
    private void manipCharge(int amt) {
        if (counter < 0) {
            counter = 0;
        }
        setCounter(counter + amt);
    }

    public AbstractRelic makeCopy() {
        return new AnachronicSnailShell();
    }
}