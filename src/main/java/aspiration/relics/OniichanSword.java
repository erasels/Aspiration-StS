package aspiration.relics;

import aspiration.actions.RelicTalkAction;
import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OniichanSword extends AspirationRelic {
    public static final String ID = "aspiration:OniichanSword";

    public OniichanSword() {
        super(ID, "OniichanSword.png", RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }


    @Override
    public void atTurnStart() {
        AbstractDungeon.actionManager.addToBottom(new RelicTalkAction(this, "HAH, take this foul beast!"));
    }

    public AbstractRelic makeCopy() {
        return new OniichanSword();
    }
}