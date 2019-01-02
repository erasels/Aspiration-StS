package aspiration.events;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import aspiration.Aspiration;



public class ElementalEggBirdNest extends AbstractImageEvent {

    //This isn't technically needed but it becomes useful later
    public static final String ID = "aspiration:TheBirdsNest";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
   
    private State state;
    private int chosen_option1 = 0;
    //private int chosen_option2 = 0;
    private String story_so_far = "";
    
    private ArrayList<AbstractRelic> eggs = new ArrayList<AbstractRelic>();
    ArrayList<AbstractRelic> tmp = new ArrayList<AbstractRelic>();
    
    
    
    public enum State{
        CHOOSING1,
        CHOOSING2,
        LEAVING
    }

    public ElementalEggBirdNest(){
        super(NAME, DESCRIPTIONS[0], Aspiration.assetPath("img/events/Bnest/start.jpg"));
    	eggs.add(new ToxicEgg2());
        eggs.add(new MoltenEgg2());
        eggs.add(new FrozenEgg2());
        
        //Set dialogue options from json string delcared in Aspiration.java
    	imageEventText.setDialogOption(OPTIONS[0], AbstractDungeon.player.hasRelic(eggs.get(0).relicId));
    	imageEventText.setDialogOption(OPTIONS[1], AbstractDungeon.player.hasRelic(eggs.get(1).relicId));
    	imageEventText.setDialogOption(OPTIONS[2], AbstractDungeon.player.hasRelic(eggs.get(2).relicId));
    	imageEventText.setDialogOption(OPTIONS[8]);
    	
        state = State.CHOOSING1;
    }

    @Override
    protected void buttonEffect(int pressedButton) {
        switch(state) {
            case CHOOSING1:
            	
                switch (pressedButton) {
                	case 0:
                		imageEventText.loadImage(Aspiration.assetPath("img/events/Bnest/toxic.jpg"));
                		chosen_option1 = 0;
                    break;
                	case 1:
                		imageEventText.loadImage(Aspiration.assetPath("img/events/Bnest/molten.jpg"));
                		chosen_option1 = 1;
                		break;
                	case 2:
                		imageEventText.loadImage(Aspiration.assetPath("img/events/Bnest/frozen.jpg"));
                        chosen_option1 = 2;
                        break; 
                	case 3:
                		chosen_option1 = 3;
                		break;
                }
                
                if(chosen_option1 == 3) {
                	imageEventText.clearAllDialogs();
                	state = State.LEAVING;
                	openMap();
                	break;
                }
                
                story_so_far = DESCRIPTIONS[0].substring(0, DESCRIPTIONS[0].length() - 1) + " " + OPTIONS[chosen_option1] + DESCRIPTIONS[1];
                imageEventText.updateBodyText(story_so_far);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[3] + FontHelper.colorString(eggs.get(chosen_option1).name, "g") + OPTIONS[4]);
                
                String cgroup = "";
                switch (chosen_option1) {
                	case 0:
                		cgroup = "#gSkills";
                		break;
                	case 1:
                		cgroup = "#gAttacks";
                		break;
                	case 2:
                		cgroup = "#gPowers";
                		break;
                }
                
                for(AbstractRelic r : AbstractDungeon.player.relics) {
        			if(r.tier == AbstractRelic.RelicTier.COMMON) {
        				tmp.add(r);
        			}
        		}
                imageEventText.setDialogOption(OPTIONS[5] + cgroup + OPTIONS[6], tmp.isEmpty());
                
                
                imageEventText.setDialogOption(OPTIONS[7]);
                
                state = State.CHOOSING2;
                break;
            case CHOOSING2:
            	switch (pressedButton) {
            	case 0:
            		CardCrawlGame.sound.play("BYRD_DEATH");
            		AbstractCard curse = new Wound();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, Settings.WIDTH / 2, Settings.HEIGHT / 2));
                    
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, eggs.get(chosen_option1));
                    imageEventText.updateBodyText(story_so_far + DESCRIPTIONS[2]);
            		break;
            	case 1:
            		Random rng = AbstractDungeon.miscRng;
            		System.out.println("Size: " + tmp.size());
            		if(!tmp.isEmpty()) {
            			System.out.println("Is in");
            			AbstractDungeon.player.loseRelic(tmp.get(rng.random(tmp.size()-1)).relicId);
            		}
            		AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, eggs.get(chosen_option1));
            		imageEventText.updateBodyText(story_so_far + DESCRIPTIONS[3]);
            		break;
            	case 2:
            		//Add baby byrd relic, make it do sounds like CultistMask and maybe Minion implementation? Or, easier. make byrds flee like hubris:ScarierMask
            		CardCrawlGame.sound.play("BYRD_DEATH");
            		imageEventText.updateBodyText(story_so_far + DESCRIPTIONS[4]);
            		break;
            	
            	}
            	imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[8]);
            	state = State.LEAVING;
            	break;
            case LEAVING:
                openMap();
                break;
        }
    }
}