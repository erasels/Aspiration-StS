package aspiration.relics.crossovers;

import aspiration.Aspiration;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.abstracts.Quest;
import infinitespire.actions.AddQuestAction;
import infinitespire.helpers.QuestHelper;
import infinitespire.interfaces.OnQuestRemovedSubscriber;

public class TomeofQuesting extends AspirationRelic implements OnQuestRemovedSubscriber {
    public static final String ID = "aspiration:TomeofQuesting";

    public TomeofQuesting() {
        super(ID, "TomeofQuesting.png", RelicTier.COMMON, LandingSound.MAGICAL);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void receiveQuestRemoved(Quest quest) {
        System.out.println("Questinomicon");
        if (quest.isCompleted()) {
            System.out.println("Tome of question quest was completed");
            try {
                AbstractDungeon.actionManager.addToBottom(new AddQuestAction(QuestHelper.getRandomQuestClass(quest.rarity).createNew()));
            } catch (Exception e) {
                Aspiration.logger.info(e);
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new TomeofQuesting();
    }
}
