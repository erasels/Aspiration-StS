package aspiration.blights;

import aspiration.blights.abstracts.AfterChestOpenBlight;
import aspiration.blights.abstracts.AspirationBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;

public class ChestSnatcher extends AspirationBlight implements AfterChestOpenBlight {
    public static final String ID = "aspiration:ChestSnatcher";

    public ChestSnatcher() {
        super(ID, "ChestSnatcher.png");
        counter = 1;
    }

    @Override
    public void onAfterChestOpen(boolean bossChest, AbstractChest chest) {
        if(!bossChest && !usedUp) {
            flash();
            setCounter(-1);
            AbstractDungeon.getCurrRoom().rewards.clear();
        }
    }

    @Override
    public void setCounter(int c) {
        super.setCounter(c);
        if(counter<0) {
            usedUp();
        }
    }
}
