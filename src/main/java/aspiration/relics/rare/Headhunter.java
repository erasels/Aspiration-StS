package aspiration.relics.rare;

import aspiration.relics.abstracts.AspirationRelic;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.SuperRareRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import java.util.ArrayList;

public class Headhunter extends AspirationRelic implements CustomSavable<Integer>, SuperRareRelic, ClickableRelic{
	public static final String ID = "aspiration:Headhunter";
	private static final int START_CHARGE = 0;
	private static final int LOW = 1;
	private static final int MID = 3;
	private static final int HIGH = 5;
	private final String chargeInfo = DESCRIPTIONS[1] + LOW + DESCRIPTIONS[2] + MID + DESCRIPTIONS[3] + HIGH + DESCRIPTIONS[4] + HIGH + DESCRIPTIONS[5] + MID + DESCRIPTIONS[6] + LOW + DESCRIPTIONS[7];
	private String buffsetInfo = DESCRIPTIONS[8];
	
	private int desc_state = 0;

    public Headhunter() {
        super(ID, "Headhunter.png", RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void atPreBattle() {
    	AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    	for(AbstractPower p: getBuffList(counter)) {
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, p, p.amount));
    	}
    	
    	
    }
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
    	flash();
    	if(m.type == AbstractMonster.EnemyType.BOSS) {
    		manipCharge(HIGH);
    	}
    	if(m.type == AbstractMonster.EnemyType.ELITE) {
    		manipCharge(MID);
    	}
    	if(m.type == AbstractMonster.EnemyType.NORMAL) {
    		manipCharge(LOW);
    	}
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room)
    {
    	flash();
        if (room instanceof RestRoom) {
            flash();
            manipCharge(-HIGH);
        }
        if (room instanceof ShopRoom) {
            flash();
            manipCharge(-MID);
        }
        if (room instanceof EventRoom) {
        	if(!room.combatEvent) {
				flash();
				manipCharge(-LOW);
			}
        }
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
        if (counter > 98) {
            counter = 99;
        }
        setCounter(counter + amt);
        
        buffsetInfo = getBuffSetInfo(getBuffList(counter));
        changeTip(buffsetInfo);
    }
    
    @Override
	public void onRightClick() {
    	switch (desc_state % 3) {
    	case 0:
    		changeTip(chargeInfo);
    		break;
    	case 1:
    		changeTip(buffsetInfo);
    		break;
    	default:
    		changeTip(DESCRIPTIONS[0]);
    	}
    	
    	desc_state++;
	}
    
    private void changeTip(String s) {
    	this.tips.clear();
        this.tips.add(new PowerTip(this.name, s));
        this.initializeTips();
    }
    
    private ArrayList<AbstractPower> getBuffList(int charge) {
    	ArrayList<AbstractPower> buffList = new ArrayList<AbstractPower>();
    	if(1 <= charge && charge <=3) {
    		buffList.add(new StrengthPower(AbstractDungeon.player, 1));
    	} else if (4 <= charge && charge <=6) {
    		buffList.add(new StrengthPower(AbstractDungeon.player, 1));
    		buffList.add(new DexterityPower(AbstractDungeon.player, 1));
    	} else if (7 <= charge && charge <=9) {
    		buffList.add(new StrengthPower(AbstractDungeon.player, 2));
    		buffList.add(new DexterityPower(AbstractDungeon.player, 1));
    		buffList.add(new AmplifyPower(AbstractDungeon.player, 3)); 								//This turn Next 3 powers played twice
    		buffList.add(new BurstPower(AbstractDungeon.player, 3));								//This turn Next 3 skills played twice
    		buffList.add(new DoubleTapPower(AbstractDungeon.player, 3));							//This turn Next 3 Attacks played twice
    	} else if (10 <= charge && charge <=12) {
    		buffList.add(new GrowthPower(AbstractDungeon.player, 1));								//Gain 1 strength at the end of each turn after the first
    		buffList.add(new HeatsinkPower(AbstractDungeon.player, 1));								//Draw a card if you play a power
    		buffList.add(new AmplifyPower(AbstractDungeon.player, 3)); 								//This turn Next 3 powers played twice
    		buffList.add(new BurstPower(AbstractDungeon.player, 3));								//This turn Next 3 skills played twice
    		buffList.add(new DoubleTapPower(AbstractDungeon.player, 3));							//This turn Next 3 Attacks played twice
    	} else if (13 <= charge && charge <=15) {
    		buffList.add(new AngryPower(AbstractDungeon.player, 2)); 								//Gain 2 Strength when attacked
    		buffList.add(new HeatsinkPower(AbstractDungeon.player, 1));								//Draw a card if you play a power
    		buffList.add(new MayhemPower(AbstractDungeon.player, 2));								//At the start of your turn play the 2 top most cards in your draw pile
    	} else if (16 <= charge && charge <=18) {
    		buffList.add(new PlatedArmorPower(AbstractDungeon.player, 8));							//Gain 8 plated armor	
    		buffList.add(new ThornsPower(AbstractDungeon.player, 8));
    		buffList.add(new CombustPower(AbstractDungeon.player, 1, 9));							//At start of turn lose 1 health and deal 9 damage to all enemies.
    	} else if (19 <= charge && charge <=21) {
    		buffList.add(new RepairPower(AbstractDungeon.player, AbstractDungeon.player.maxHealth / 10));			//Gain heal 10th of max life after winning a battle
    		buffList.add(new HelloPower(AbstractDungeon.player, 1));								//At the start of your turn ad a random common card to your hand.
    		buffList.add(new MagnetismPower(AbstractDungeon.player, 1));							//At the start of your turn ad a random colorless card to your hand.
    	} else if (22 <= charge && charge <=24) {
    		buffList.add(new BarricadePower(AbstractDungeon.player));								//Don't lose block at end of turn
    		buffList.add(new PanachePower(AbstractDungeon.player, 5));								//Deal 5 damage for every 5 cards played
    	} else if (25 <= charge && charge <=27) {
    		buffList.add(new ArtifactPower(AbstractDungeon.player, 5));
    		buffList.add(new IntangiblePlayerPower(AbstractDungeon.player, 3));	
    		buffList.add(new TheBombPower(AbstractDungeon.player, 6, 100));							//Deal 100 damage to all enemies after 6 turns
    	} else if (28 <= charge && charge <=30) {
    		buffList.add(new ThousandCutsPower(AbstractDungeon.player, 2));							//Whenever you play a card deal 2 damage to all enemies
    		buffList.add(new FireBreathingPower(AbstractDungeon.player, 3));						//Deal 3 damage for each attack played this turn, every turn
    		buffList.add(new EnvenomPower(AbstractDungeon.player, 1));								//Whenever you deal unblocked damage apply 2 poison
    	} else if (31 <= charge && charge <=33) {
    		buffList.add(new BerserkPower(AbstractDungeon.player, 1));					//1 more energy gain at start of turn
    		buffList.add(new EnvenomPower(AbstractDungeon.player, 1));								//Whenever you deal unblocked damage apply 1 poison
    		buffList.add(new NoxiousFumesPower(AbstractDungeon.player, 1));							//At the end of your turn apply 1 poison to all enemies
    		buffList.add(new SadisticPower(AbstractDungeon.player, 3));								//Deal 3 damage every time you apply a debuff
    	} else if (34 <= charge && charge <=36) {
    		buffList.add(new GrowthPower(AbstractDungeon.player, 4));								//Gain 4 strength at the end of each turn after the first
    		buffList.add(new AfterImagePower(AbstractDungeon.player, 2));							//Gain 2 block when playing a card
    	} else if (37 <= charge && charge <=39) {
    		buffList.add(new EchoPower(AbstractDungeon.player, 2));									//Every turn, the first two cards you play are played twice
    		buffList.add(new DoubleDamagePower(AbstractDungeon.player, 2, false));					//Deal double damage for the next 2 turns
    	} else if (40 <= charge && charge <=42) {
    		buffList.add(new CreativeAIPower(AbstractDungeon.player, 2));							//Add 2 random powers into your hand at start of turn
    		buffList.add(new MagnetismPower(AbstractDungeon.player, 1));							//At the start of your turn ad a random colorless card to your hand.
    		buffList.add(new HeatsinkPower(AbstractDungeon.player, 1));								//Draw a card if you play a power
    		buffList.add(new AmplifyPower(AbstractDungeon.player, 3)); 								//This turn Next 3 powers played twice
    	} else if (43 <= charge && charge <=45) {
    		buffList.add(new JuggernautPower(AbstractDungeon.player, 6));							//Deal 6 damage for each time you gain defense this turn
    		buffList.add(new MetallicizePower(AbstractDungeon.player, 5));							//Every turn, Gain 5 block at the end of your turn
    		buffList.add(new BufferPower(AbstractDungeon.player, 3));								//Next 3 times you'd take health damage, don't
    		buffList.add(new AfterImagePower(AbstractDungeon.player, 2));							//Gain 2 block when playing a card
    	} else if (46 <= charge && charge <=48) {
    		buffList.add(new DrawPower(AbstractDungeon.player, 2)); 								//Draw 2 more cards every turn
        	buffList.add(new InvinciblePower(AbstractDungeon.player, AbstractDungeon.player.maxHealth /10));	//Cannot take more than 1/10th of your max health as damage per turn
    	} else if (49 <= charge && charge <=51) {
    		buffList.add(new DemonFormPower(AbstractDungeon.player, 5));							//Gain 3 strength at the start of your turn
    		buffList.add(new PlatedArmorPower(AbstractDungeon.player, 10));							//Gain 10 plated armor	
    		buffList.add(new DexterityPower(AbstractDungeon.player, 5));
    	} else if (52 <= charge && charge <=54) {
    		buffList.add(new InfiniteBladesPower(AbstractDungeon.player, 3));						//At the start of your turn, add 3 Shivs to your hand
    		buffList.add(new DarkEmbracePower(AbstractDungeon.player, 1));							//Draw 1 card when you exhaust a card
    		buffList.add(new FeelNoPainPower(AbstractDungeon.player, 5));							//Gain 5 block when exhausting a card
    		buffList.add(new DoubleTapPower(AbstractDungeon.player, 3));							//This turn Next 3 Attacks played twice
    	} else if (55 <= charge && charge <=57) {
    		buffList.add(new DrawPower(AbstractDungeon.player, 3)); 								//Draw 3 more cards every turn
    		buffList.add(new BerserkPower(AbstractDungeon.player, 2));					//2 more energy gain at start of turn
    	} else if (58 <= charge && charge <=60) {
    		buffList.add(new EchoPower(AbstractDungeon.player, 10));								//Every turn, the first 10 cards you play are played twice
    		buffList.add(new BerserkPower(AbstractDungeon.player, 4));					//4 more energy gain at start of turn
    		buffList.add(new DrawPower(AbstractDungeon.player, (BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.masterHandSize))); 	//Draw cards boi
    		buffList.add(new ConfusionPower(AbstractDungeon.player));								//Randomized card costs
    	} else if (charge > 60) {
    		buffList.add(new StrengthPower(AbstractDungeon.player, charge));
    		buffList.add(new DexterityPower(AbstractDungeon.player, charge));
    		buffList.add(new TheBombPower(AbstractDungeon.player, 1, charge));							//Deal 100 damage to all enemies after 6 turns
    		buffList.add(new MayhemPower(AbstractDungeon.player, Math.round(charge/25)));							//At the start of your turn play the 2 top most cards in your draw pile
    	}
    	
    	return buffList;
    }
    
    private String getBuffSetInfo(ArrayList<AbstractPower> bl) {
    	String tmp = DESCRIPTIONS[8];
    	for(AbstractPower p: getBuffList(counter)) {
    		tmp += FontHelper.colorString((p.name), "b") + " | ";
    	}
    	
    	return tmp.substring(0, tmp.length() - 3);
    }
    
    @Override
    public boolean canSpawn()
    {
    	return ((AbstractDungeon.floorNum <= 48) && (!Settings.isEndless));
    }
    
    //counter is saved automatically, this can be replaced by RNG number for which Buffset to choose once implemented
    @Override
    public Integer onSave() {
    	return counter;
    }
    
    @Override
    public void onLoad(Integer p)
    {
        if (p == null) {
            return;
        }
        setCounter(p);
        
        buffsetInfo = getBuffSetInfo(getBuffList(counter));
        changeTip(buffsetInfo);
    }

    public AbstractRelic makeCopy() {
        return new Headhunter();
    }
}