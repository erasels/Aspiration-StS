package aspiration.relics.rare;

import aspiration.Aspiration;
import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.stream.IntStream;

public class DSix extends AspirationRelic implements ClickableRelic {
    public static final String ID = "aspiration:DSix";

    private static final int STARTING = 6;

    public DSix() {
        super(ID, "ChemicalBlood.png", RelicTier.RARE, LandingSound.SOLID);
        counter = STARTING;
    }

    @Override
    public String getUpdatedDescription() {
        return CLICKABLE_DESCRIPTIONS()[1] + DESCRIPTIONS[0] + STARTING + DESCRIPTIONS[1];
    }

    @Override
    public void onRightClick() {
        if(pulse && !Aspiration.dsixTriggered) {
            if(IntStream.of(countRewards()).sum() > 0) {
                Aspiration.dsixTriggered = true;
            } else {
                pulse = false;
            }
        }
    }

    public void replaceRewards() {
        flash();
        for(RewardItem ri : AbstractDungeon.combatRewardScreen.rewards) {
            if(ri.type == RewardItem.RewardType.RELIC) {
                ri.relic = AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
                if(ri.text != null && ri.relic != null) {
                    ri.text = ri.relic.name;
                }
            } else if(ri.type == RewardItem.RewardType.POTION) {
                ri.potion = AbstractDungeon.returnRandomPotion();
                if(ri.text != null && ri.potion != null) {
                    ri.text = ri.potion.name;
                }
            }
        }
        AbstractDungeon.combatRewardScreen.positionRewards();
        reduceCounter();
    }

    public int[] countRewards() {
        int p = 0, r = 0;
        for(RewardItem ri : AbstractDungeon.combatRewardScreen.rewards) {
            if(ri.type == RewardItem.RewardType.POTION) {
                p++;
            } else if(ri.type == RewardItem.RewardType.RELIC) {
                r++;
            }
        }
        return new int[]{p,r};
    }

    private void reduceCounter() {
        counter--;
        if(counter<1) {
            counter = -1;
            usedUp();
            pulse = false;
        }
    }

    @Override
    public void update() {
        super.update();
        if(pulse && IntStream.of(countRewards()).sum() < 1) {
            stopPulse();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom r) {
        if(pulse) {
            stopPulse();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.bossCount < 2 || Settings.isEndless;
    }

    public AbstractRelic makeCopy() {
        return new DSix();
    }
}