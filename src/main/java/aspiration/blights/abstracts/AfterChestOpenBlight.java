package aspiration.blights.abstracts;

import com.megacrit.cardcrawl.rewards.chests.AbstractChest;

public interface AfterChestOpenBlight {
    void onAfterChestOpen(boolean bossChest, AbstractChest chest);
}
