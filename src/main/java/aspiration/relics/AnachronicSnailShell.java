package aspiration.relics;

import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;

public class AnachronicSnailShell extends AspirationRelic implements BetterOnLoseHpRelic {
	public static final String ID = "aspiration:AnachronicSnailShell";
	
	private static final int START_CHARGE = 0;
	private boolean duringTurn = true;

    public AnachronicSnailShell() {
        super(ID, "AnachronicSnailShell.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    /*@Override
    public int onAttacked(DamageInfo info, int damageAmount) {
    	if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) && (damageAmount > 0) && (AbstractDungeon.player.currentBlock < damageAmount) && !(AbstractDungeon.player.endTurnQueued) && info.owner != AbstractDungeon.player) {
    		flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("POWER_TIME_WARP"));
            pulse = true;
            
            manipCharge(damageAmount - AbstractDungeon.player.currentBlock);
            return 0;
    	}
    	return damageAmount;
    }*/

    @Override
    public int betterOnLoseHp(DamageInfo info, int Amount) {
        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) && !duringTurn && info.owner != AbstractDungeon.player && info.type == DamageType.NORMAL) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("POWER_TIME_WARP"));
            beginLongPulse();

            Amount = MathUtils.floor((float)Amount /2);
            manipCharge(Amount);
            return Amount;
        }
        return Amount;
    }

    @Override
    public void onPlayerEndTurn()
    {
        duringTurn = false;
    	if(counter > 0) {
    		//AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), counter, DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new FlashPowerEffect(new TimeWarpPower(AbstractDungeon.player)), 0.0F));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), counter, DamageType.THORNS), AttackEffect.BLUNT_LIGHT));
    		startingCharges();
    		stopPulse();
    	}
    }

    @Override
    public void atTurnStart() {
        duringTurn = true;
    }
    
    @Override
    public void onVictory() {
    	startingCharges();
    	stopPulse();
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