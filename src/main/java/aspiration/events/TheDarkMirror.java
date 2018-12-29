package aspiration.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.*;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import aspiration.Aspiration;



public class TheDarkMirror extends AbstractImageEvent {

    //This isn't technically needed but it becomes useful later
    public static final String ID = "aspiration:TheDarkMirror";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
   
    private State state;
    private int chosen_option = 0;
    private static final int DAMAGE_AMOUNT = 15;
    
    public enum State{
        CHOOSING,
        LEAVING
    }

    public TheDarkMirror(){
        super(NAME, DESCRIPTIONS[0], Aspiration.assetPath("img/events/event1_Startup.jpg"));
    	
        //Set dialogue options from json string delcared in Aspiration.java
        if (AbstractDungeon.player.hasRelic(PrismaticShard.ID)) {
        	AbstractCard c = new Apotheosis();
        	c.upgrade();
        	this.imageEventText.setDialogOption(OPTIONS[1], !AbstractDungeon.player.hasRelic(PrismaticShard.ID), c);
          } else {
            this.imageEventText.setDialogOption(OPTIONS[3], !AbstractDungeon.player.hasRelic(PrismaticShard.ID));
          }
        imageEventText.setDialogOption(OPTIONS[2]+ DAMAGE_AMOUNT + OPTIONS[4], AbstractDungeon.player.hasRelic(PrismaticShard.ID));
        imageEventText.setDialogOption(OPTIONS[0], new Shame());
        
        state = State.CHOOSING;
    }
    
    public void onEnterRoom()
    {
      if (Settings.AMBIANCE_ON) {
        CardCrawlGame.sound.play("EVENT_FORGOTTEN");
      }
    }

    @Override
    protected void buttonEffect(int pressedButton) {
        switch(state) {
            case CHOOSING:
            	
                switch (pressedButton) {
                	//Reflect back
                	case 0:
                		imageEventText.loadImage(Aspiration.assetPath("img/events/event1_Win.jpg"));
                		CardCrawlGame.sound.play("BELL");
                		AbstractCard card = new Apotheosis();
                		card.upgrade();
                		AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2, Settings.HEIGHT / 2));
                		chosen_option = 2;
                    break;
                	//Smash Mirror
                	case 1:
                		imageEventText.loadImage(Aspiration.assetPath("img/events/event1_Shatter.jpg"));
                		CardCrawlGame.sound.play("THUNDERCLAP");
                		if (AbstractDungeon.ascensionLevel >= 15) {
                	     AbstractDungeon.player.damage(new DamageInfo(null, DAMAGE_AMOUNT, DamageInfo.DamageType.HP_LOSS));
                		} else {
                			AbstractDungeon.player.damage(new DamageInfo(null, DAMAGE_AMOUNT - 4, DamageInfo.DamageType.HP_LOSS));
                		}

                	    AbstractRelic r = new PrismaticShard();
                	    AbstractDungeon.getCurrRoom().rewards.clear();
                	    AbstractDungeon.getCurrRoom().addRelicToRewards(r);
                	    AbstractDungeon.combatRewardScreen.open();
                	    
                		chosen_option = 3;
                		break;
                	//Flee
                	case 2:
                		imageEventText.loadImage(Aspiration.assetPath("img/events/event1_Flee.jpg"));
                        CardCrawlGame.sound.play("NECRONOMICON");
                        AbstractCard curse = new Shame();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, Settings.WIDTH / 2, Settings.HEIGHT / 2));
                        chosen_option = 1;
                        break; 
                }
                imageEventText.updateBodyText(DESCRIPTIONS[0] + DESCRIPTIONS[chosen_option]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[5]);
                state = State.LEAVING;
                break;
            case LEAVING:
                openMap();
                break;
        }
    }
}