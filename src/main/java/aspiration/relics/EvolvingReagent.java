package aspiration.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AmplifyPower;
import com.megacrit.cardcrawl.powers.BurstPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class EvolvingReagent extends AspirationRelic implements BetterOnUsePotionRelic{
	public static final String ID = "aspiration:EvolvingReagent";
	public static final int ENERGY_GAIN = 1;
	private boolean double_poison = false;
	private Random rng = null;
	
	
	public EvolvingReagent() {
        super(ID, "EvolvingReagent.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
	
	@Override
    public void betterOnUsePotion(AbstractPotion potion) {
		//Double use
    	potion.use(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng));
    	
    	//Keep after use
    	if(!(AbstractDungeon.player.potions.size() == AbstractDungeon.player.potionSlots)) {
    		AbstractDungeon.player.obtainPotion(potion.slot,  potion);
    	}
    	
    	//Energy gain
    	AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
    	
    	//Buff On Drink
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
		
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AmplifyPower(AbstractDungeon.player, 1), 1)); 							
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BurstPower(AbstractDungeon.player, 1), 1));							
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleTapPower(AbstractDungeon.player, 1), 1));	
		
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnvenomPower(AbstractDungeon.player, 1), 1));	
		//Remove after turn end
		
		//Remove Debuffs on use
		AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));
		
		//Triggering all your other potions for free when drinking one
		for(AbstractPotion p : AbstractDungeon.player.potions) {
			AbstractPotion tmp = p.makeCopy();
			if(tmp.canUse()) {
				tmp.use(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng));
			}
		}
		
		//Double poison amount if imbibed potion
		if(rng.random(100) < 33) {
			double_poison = true;
			
		}
		
		//Power tip
    }
	
	@Override
	public void onPlayerEndTurn() {
		if(double_poison) {
			double_poison = false;
		}
	}
	
	public boolean isPoisonDoubled() {
		return double_poison;
	}
	
	public AbstractRelic makeCopy() {
        return new EvolvingReagent();
    }
}
