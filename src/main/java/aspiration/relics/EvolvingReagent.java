package aspiration.relics;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.BurstPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;

import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;
//https://github.com/The-Evil-Pickle/Replay-the-Spire/tree/master/src/main/java/com/megacrit/cardcrawl/mod/replay/monsters/replay
public class EvolvingReagent extends AspirationRelic implements BetterOnUsePotionRelic, ClickableRelic, CustomSavable<Integer[]>{
	public static final String ID = "aspiration:EvolvingReagent";
	private static final int ENERGY_GAIN = 2;
	private static final int START_CHARGE = 0;
	private static final int EVOLUTION_THRESHOLD = 4;
	private static final int REFUND_CHANCE = 40;
	
	private int[] evolutions = new int[3];
	
	private ArrayList<AbstractPotion> refunded_potions = new ArrayList<AbstractPotion>();
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
		rng = AbstractDungeon.relicRng;
    	//Evolution Stage 1
		switch (evolutions[0]) {
			case 1:
    			//Buff on drink
    			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
    			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
    			break;
			case 2:
				//Round buff on drink						
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BurstPower(AbstractDungeon.player, 1), 1));							
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleTapPower(AbstractDungeon.player, 1), 1));	
				break;
			case 3:
				//Remove Debuffs on drink
				AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));
				break;
		}
		//----------------------------------------------------------------------
		
		//Evolution Stage 2
		switch (evolutions[1]) {
			case 1:
				//Double poison amount for 1 round if imbibed potion
				double_poison = true;
				break;
			case 2:
				//Poison on hit for battle
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnvenomPower(AbstractDungeon.player, 1), 1));
				break;
			case 3:
				//Energy gain
    			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
				break;	
		}
		//----------------------------------------------------------------------
    	
		// Evolution Stage 3
		switch (evolutions[2]) {
			case 1:
				// Triggering all your other potions for free when drinking one
				for (AbstractPotion p : AbstractDungeon.player.potions) {
					AbstractPotion tmp = p.makeCopy();
					if (tmp.ID != PotionSlot.POTION_ID && tmp.canUse()) {
						tmp.use(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng));
					}
				}
				break;
			case 2:
				// Double use
				potion.use(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng));
				break;
			case 3:
				//Chance to refund after turn end
				if(rng.random(100) < REFUND_CHANCE) {
					refunded_potions.add(potion.makeCopy());
				}
				break;
		}
		//----------------------------------------------------------------------
		manipCharge(1);
    }
	
	@Override
	public void onPlayerEndTurn() {
		if(double_poison) {
			double_poison = false;
		}
		if(!refunded_potions.isEmpty()) {
			for(AbstractPotion p : refunded_potions) {
				AbstractDungeon.player.obtainPotion(p);
			}
			refunded_potions.clear();
		}
	}
	
	public boolean isPoisonDoubled() {
		return double_poison;
	}
	
	@Override
	public void onEquip() {
        startingCharges();
    }
    
	@Override
	public void onRightClick() {
		//CYCLE THROUGH DESCRIPTIONS
		//VARIABLE FOR DESCRIPTION CURRENTLY CHOSEN, THAT WILL DETERMINE WHAT IS PICKED onVictory() FOR EVOLUTION PATH
	}
	
	@Override
    public boolean canSpawn() //Only lower than floor 24 and if the player doesn't have Sozu
    {
    	return (AbstractDungeon.floorNum <= 23 && !(AbstractDungeon.player.hasRelic(Sozu.ID)));
    }
	
	private void startingCharges()
    {
        setCounter(START_CHARGE);
    }
    
    private void manipCharge(int amt) {
        if(!(evolutions[2] != 0)) {
        	flash();
			if (counter < 0) {
				counter = 0;
			}
			setCounter(counter + amt);

			if (counter >= EVOLUTION_THRESHOLD) {
				evolve();
				counter -= EVOLUTION_THRESHOLD;
			}
        }
    }
    
    private void evolve() {
    	
    }
    
    @Override
    public Integer[] onSave() {
    	Integer[] tmp = new Integer[3];
    	for(int i = 0; i<tmp.length;i++) {
    		tmp[i] = evolutions[i];
    	}
    	return tmp;
    }
    
    @Override
    public void onLoad(Integer[] p)
    {
    	for(int i = 0; i<p.length;i++) {
    		if (p[i] == null) {
                evolutions[i] = 0;
            } else {
            	evolutions[i] = p[i];
            }
    	}
    }
	
	public AbstractRelic makeCopy() {
        return new EvolvingReagent();
    }
}
