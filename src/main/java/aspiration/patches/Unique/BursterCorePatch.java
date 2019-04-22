package aspiration.patches.Unique;

import aspiration.relics.boss.BursterCore;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import javassist.CtBehavior;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="channelOrb"
)
public class BursterCorePatch
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void Insert(AbstractPlayer __intance, AbstractOrb orbToSet) {
        if(AbstractDungeon.player.hasRelic(BursterCore.ID)) {
            AbstractDungeon.actionManager.addToTop(new EvokeWithoutRemovingOrbAction(1));
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.NewExprMatcher(AnimateOrbAction.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    /*
    ...
    else
      {
        AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
        AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
   ---->
        AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
      }
     ...
     */
}