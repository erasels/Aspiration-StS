package aspiration.relics;

import java.util.ArrayList;
import java.util.Iterator;

import com.evacipated.cardcrawl.mod.stslib.relics.SuperRareRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.ThousandCutsPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AfterImagePower;
import com.megacrit.cardcrawl.powers.AmplifyPower;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BerserkPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.BrutalityPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.BurstPower;
import com.megacrit.cardcrawl.powers.CombustPower;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.CreativeAIPower;
import com.megacrit.cardcrawl.powers.DarkEmbracePower;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import com.megacrit.cardcrawl.powers.DrawPower;
import com.megacrit.cardcrawl.powers.EchoPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.EvolvePower;
import com.megacrit.cardcrawl.powers.FeelNoPainPower;
import com.megacrit.cardcrawl.powers.FireBreathingPower;
import com.megacrit.cardcrawl.powers.GrowthPower;
import com.megacrit.cardcrawl.powers.HeatsinkPower;
import com.megacrit.cardcrawl.powers.HelloPower;
import com.megacrit.cardcrawl.powers.InfiniteBladesPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import com.megacrit.cardcrawl.powers.MagnetismPower;
import com.megacrit.cardcrawl.powers.MayhemPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;
import com.megacrit.cardcrawl.powers.PanachePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.RagePower;
import com.megacrit.cardcrawl.powers.RepairPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.SadisticPower;

import aspiration.relics.abstracts.AspirationRelic;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;

public class Legacy_Headhunter extends AspirationRelic implements CustomSavable<Integer[][]>, SuperRareRelic{
	public static final String ID = "aspiration:Legacy_Headhunter";
	
    private static final int BUFF_CARRYOVER_AMOUNT = 7;
    private boolean initialized = true;
    private String powerCollection = DESCRIPTIONS[2];
    
    private ArrayList<AbstractPower> Normal_buffs;
    private ArrayList<AbstractPower> Elite_buffs;
    private ArrayList<AbstractPower> Boss_buffs;
    private ArrayList<timedPower> buff_list;
    private Random rng = AbstractDungeon.relicRng;

    public Legacy_Headhunter() {
        super(ID, "Legacy_Headhunter.png", RelicTier.RARE, LandingSound.MAGICAL);
        
        Normal_buffs = new ArrayList<AbstractPower>();
        //fillBuffListNorm(Normal_buffs);
        Elite_buffs = new ArrayList<AbstractPower>();
        //fillBuffListElite(Elite_buffs);
        Boss_buffs = new ArrayList<AbstractPower>();
        //fillBuffListBoss(Boss_buffs);
        
        buff_list = new ArrayList<timedPower>();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BUFF_CARRYOVER_AMOUNT + DESCRIPTIONS[1];
    }
    
    @Override
    public void atPreBattle()
    {
    	if(initialized) {
    		fillBuffListNorm(Normal_buffs);
            fillBuffListElite(Elite_buffs);
            fillBuffListBoss(Boss_buffs);
            initialized = false;
    	}
    	flash();
    	applyBuffs(false);
    }
    
    @Override
    public void onVictory() {
    	boolean jump = false;
    	rng = AbstractDungeon.relicRng; //REEEE WHY ARE YOU NULL YOU PIECE OF SHIT
    	powerCollection = DESCRIPTIONS[2];
    	flash();
    	AbstractDungeon.actionManager.addToBottom(new SFXAction("BUFF_3"));
    	
    	
    	if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
    		int i = rng.random(Normal_buffs.size());
    		if(i == Normal_buffs.size()) {
    			jump = true;
    		}
    		if(!jump) {
    			buff_list.add(new timedPower(Normal_buffs.get(i), 1));
    			Normal_buffs.remove(i);
    		}
    	}
    	if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || jump) {
    		jump = false;
    		int i = rng.random(Elite_buffs.size());
    		if(i == Elite_buffs.size()) {
    			jump = true;
    		}
    		if(!jump) {
    			buff_list.add(new timedPower(Elite_buffs.get(i), 2));
    			Elite_buffs.remove(i);
    		}
    	}
    	if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || jump) {
    		int i = rng.random(Boss_buffs.size() - 1);
    		buff_list.add(new timedPower(Boss_buffs.get(i), 3));
			Boss_buffs.remove(i);
    	}
    	
    	updateTip();
    }

    public AbstractRelic makeCopy() {
        return new Legacy_Headhunter();
    }
    
    private void applyBuffs(boolean isOnLoad) {
    	//for(timedPower p : buff_list) {
    	for(Iterator<timedPower> tP = buff_list.iterator(); tP.hasNext(); ) {
    		timedPower p = tP.next();
    		if(!isOnLoad) {
    			if(p.decrement()) {
    				//buff_list.remove(p);
    				tP.remove();
    				switch (p.getTier()) {
    					case 1:
    						Normal_buffs.add(p.getPower());
    						break;
    					case 2:
    						Elite_buffs.add(p.getPower());
    						break;
    					case 3:
    						Boss_buffs.add(p.getPower());
    						break;
    				}
    			continue;
    			}
    		}
    		try {
    			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, p.getPower(), p.getPower().amount));
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private void updateTip() {
    	this.tips.clear();
    	
    	for(timedPower p : buff_list) {
    		powerCollection += FontHelper.colorString((p.getPower().name), "y") + " (" + p.getTurns() +"), ";
    	}
    	
    	this.tips.add(new PowerTip(this.name, powerCollection.substring(0, powerCollection.length() - 2)));
        this.initializeTips();
    }
    
    private void fillBuffListNorm(ArrayList<AbstractPower> pl) {
    	pl.add(new StrengthPower(AbstractDungeon.player, 1));
    	pl.add(new DexterityPower(AbstractDungeon.player, 1));
    	pl.add(new ArtifactPower(AbstractDungeon.player, 1));
    	pl.add(new IntangiblePower(AbstractDungeon.player, 2));	
    	pl.add(new MetallicizePower(AbstractDungeon.player, 3));						//Every turn, Gain 3 block at the end of your turn
    	pl.add(new AmplifyPower(AbstractDungeon.player, 3)); 							//This turn Next 3 powers played twice
    	pl.add(new BurstPower(AbstractDungeon.player, 3));								//This turn Next 3 skills played twice
    	pl.add(new DoubleTapPower(AbstractDungeon.player, 3));							//This turn Next 3 attacks played twice
    	pl.add(new BlurPower(AbstractDungeon.player, 2)); 								//Don't lose block at end of turn for 2 turns
    	pl.add(new DarkEmbracePower(AbstractDungeon.player, 1));						//Draw 1 card when you exhaust a card
    	pl.add(new EnvenomPower(AbstractDungeon.player, 1));							//Whenever you deal unblocked damage apply 2 poison
    	pl.add(new EvolvePower(AbstractDungeon.player, 1));								//Whenever you draw a status/curse draw 1 card
    	pl.add(new FeelNoPainPower(AbstractDungeon.player, 5));							//Gain 5 block when exhausting a card
    	pl.add(new GrowthPower(AbstractDungeon.player, 1));								//Gain 1 strength at the end of each turn after the first
    	pl.add(new HeatsinkPower(AbstractDungeon.player, 1));							//Draw a card if you play a power
    	pl.add(new NoxiousFumesPower(AbstractDungeon.player, 2));						//At the end of your turn apply 2 poison to all enemies
    	pl.add(new PanachePower(AbstractDungeon.player, 3));							//Deal 3 damage for every 5 cards played
    	pl.add(new RagePower(AbstractDungeon.player, 5));								//Gain 5 Block for each attack you play, on your first turn
    	pl.add(new SadisticPower(AbstractDungeon.player, 3));							//Deal 3 damage every time you apply a debuff
    	pl.add(new ThousandCutsPower(AbstractDungeon.player, 2));						//Whenever you play a card deal 2 damage to all enemies
    	
    	//Add card powers
    	pl.add(new HelloPower(AbstractDungeon.player, 1));								//At the start of your turn ad a random common card to your hand.
    	pl.add(new MagnetismPower(AbstractDungeon.player, 1));							//At the start of your turn ad a random colorless card to your hand.
    	
    	//Pseudo-Negative (Ascension)
    	if (AbstractDungeon.ascensionLevel >= 15) {
    		pl.add(new ConfusionPower(AbstractDungeon.player));							//Randomized card costs
    		pl.add(new BrutalityPower(AbstractDungeon.player, 2));						//At start of turn lose 1 health and draw 2 more cards
    	}
    }
    
    private void fillBuffListElite(ArrayList<AbstractPower> pl) {
    	pl.add(new AngryPower(AbstractDungeon.player, 2)); 								//Gain 2 Strength when attacked
    	pl.add(new AfterImagePower(AbstractDungeon.player, 3));							//Gain 3 block when playing a card
    	pl.add(new BufferPower(AbstractDungeon.player, 2));								//Next two times you'd take health damage, don't
    	pl.add(new DrawPower(AbstractDungeon.player, 2)); 								//Draw 2 more cards every turn
    	pl.add(new EchoPower(AbstractDungeon.player, 2));								//Every turn, the first two cards you play are played twice
    	pl.add(new FireBreathingPower(AbstractDungeon.player, 3));						//Deal 3 damage for each attack played this turn
    	pl.add(new JuggernautPower(AbstractDungeon.player, 3));							//Deal 3 damage for each time you gain defense this turn
    	pl.add(new IntangiblePlayerPower(AbstractDungeon.player, 3));
    	pl.add(new MayhemPower(AbstractDungeon.player, 3));								//At the start of your turn play the three top most cards in your draw pile
    	pl.add(new PlatedArmorPower(AbstractDungeon.player, 8));						//Gain 8 plated armor						
    	pl.add(new RitualPower(AbstractDungeon.player, 2));								//Gain 2 Strength at the start of each turn
    	pl.add(new ThornsPower(AbstractDungeon.player, 8));
    	
    	//Add card powers
    	pl.add(new CreativeAIPower(AbstractDungeon.player, 1));							//Add random power into your hand at start of turn
    	
    	//HP Loss powers
    	pl.add(new CombustPower(AbstractDungeon.player, 1, 9));							//At start of turn lose 1 health and deal 9 damage to all enemies.
    }
    
	private void fillBuffListBoss(ArrayList<AbstractPower> pl) {
		pl.add(new BarricadePower(AbstractDungeon.player));								//Don't lose block at end of turn
		pl.add(new BerserkPower(DESCRIPTIONS[3], AbstractDungeon.player, 1));					//1 more energy gain at start of turn
		pl.add(new DemonFormPower(AbstractDungeon.player, 3));							//Gain 3 strength at the start of your turn
		pl.add(new DoubleDamagePower(AbstractDungeon.player, 10, false));				//Deal double damage for the next 10 turns
		pl.add(new InfiniteBladesPower(AbstractDungeon.player, (BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.masterHandSize)));	//At the start of your turn, add as many Shivs as you can have in your hand
		pl.add(new InvinciblePower(AbstractDungeon.player, AbstractDungeon.player.maxHealth /10));	//Cannot take more than 1/10th of your max health as damage per turn
		pl.add(new RepairPower(AbstractDungeon.player, AbstractDungeon.player.maxHealth));			//Gain heal to full life after winning a battle
		pl.add(new TheBombPower(AbstractDungeon.player, 6, 100));						//Deal 100 damage to all enemies after 6 turns
	}
	
	@Override
	public void onLoad(Integer[][] values) {
		fillBuffListNorm(Normal_buffs);
        fillBuffListElite(Elite_buffs);
        fillBuffListBoss(Boss_buffs);
        initialized = false;
        
        java.util.Random r = new java.util.Random();
        
        for(int i = 0; i<values.length;i++) {
        	if(values[i][1] == 3) {
        		int rand = r.nextInt(Boss_buffs.size());
        		buff_list.add(new timedPower(Boss_buffs.get(rand), 3, values[i][0]));
    			Boss_buffs.remove(rand);
        	}
        	if(values[i][1] == 2) {
        		int rand = r.nextInt(Elite_buffs.size());
        		buff_list.add(new timedPower(Elite_buffs.get(rand), 2, values[i][0]));
    			Elite_buffs.remove(rand);
        	}
        	if(values[i][1] == 1) {
        		int rand = r.nextInt(Normal_buffs.size());
        		buff_list.add(new timedPower(Normal_buffs.get(rand), 1, values[i][0]));
    			Normal_buffs.remove(rand);
        	}
        }
		
		applyBuffs(true);
		updateTip();
	}

	@Override
    public boolean canSpawn() //Checked when?
    {
		java.util.Random r = new java.util.Random();
		int rand = r.nextInt(10);
    	return rand == 9;
    }
	
	@Override
	public Integer[][] onSave() {
		Integer[][] values = new Integer[buff_list.size()][2];
		for(int i = 0; i<buff_list.size();i++) {
			values[i][0] = buff_list.get(i).getTurns();
			values[i][1] = buff_list.get(i).getTier();
		}
		
		return values;
	}
	
	private class timedPower {
		private int turns = 0;
		private int tier = 0;
		private AbstractPower power = null;
		
		public timedPower(AbstractPower p, int relic_tier, int leftover_turns) {
			power = p;
			tier = relic_tier;
			turns = leftover_turns;
		}
		
		public timedPower(AbstractPower p, int relic_tier) {
			power = p;
			tier = relic_tier;
			turns = BUFF_CARRYOVER_AMOUNT;
		}
		
		public boolean decrement() {
			turns -= 1;
			return turns < 0;
		}
		
		public AbstractPower getPower() {
			return power;
		}
		
		public int getTurns() {
			return turns;
		}
		
		public int getTier() {
			return tier;
		}
	}
}
