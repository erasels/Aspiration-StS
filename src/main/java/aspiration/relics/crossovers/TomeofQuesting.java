package aspiration.relics.crossovers;

import aspiration.Aspiration;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.effects.QuestLogUpdateEffect;
import infinitespire.helpers.QuestHelper;

public class TomeofQuesting extends AspirationRelic /*implements OnQuestRemovedSubscriber*/ {
    public static final String ID = "aspiration:TomeofQuesting";

    public TomeofQuesting() {
        super(ID, "TomeofQuesting.png", RelicTier.COMMON, LandingSound.MAGICAL);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    /*public void receiveQuestRemoved(Quest quest) {
        System.out.println("Questinomicon");
        if (quest.isCompleted()) {
            System.out.println("Tome of question quest was completed");
            try {
                AbstractDungeon.actionManager.addToBottom(new AddQuestAction(QuestHelper.getRandomQuestClass(quest.rarity).createNew()));
            } catch (Exception e) {
                Aspiration.logger.info(e);
            }
        }
    }*/

    @Override
    public void onTrigger() {
        try {
            //AbstractDungeon.actionManager.addToBottom(new AddQuestAction(QuestHelper.getRandomQuestClass(quest.rarity).createNew()));
            int infPrevention = 0;
            Quest quest;
            while(true) {
                quest = QuestHelper.getRandomQuest();
                //Aspiration.logger.info(quest.id + " was retrieved.");
                if(infPrevention>40 || (!InfiniteSpire.questLog.hasQuest(quest) && InfiniteSpire.questLog.getAmount(quest.type) < 7)) {
                    //Aspiration.logger.info(this.name + " didn't add new Quest because it failed retrieving a valid one 30 times ina  row.");
                    break;
                }
                infPrevention++;
            }
            Aspiration.logger.info(quest.id + " is valid and will be added. Doesnt have quest: " + String.valueOf(!InfiniteSpire.questLog.hasQuest(quest)) + " | Quest type isnt full: " + String.valueOf(InfiniteSpire.questLog.getAmount(quest.type) < 7));

            if (!InfiniteSpire.questLog.hasQuest(quest) && InfiniteSpire.questLog.getAmount(quest.type) < 7) {
                InfiniteSpire.questLog.add(quest.createNew());
                AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
                InfiniteSpire.publishOnQuestAdded(quest);
            }
        } catch (Exception e) {
            Aspiration.logger.info(e);
        }
    }

    public AbstractRelic makeCopy() {
        return new TomeofQuesting();
    }
}
