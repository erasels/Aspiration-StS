package aspiration.actions;

import aspiration.Aspiration;
import aspiration.orbs.AmalgamateOrb;
import aspiration.orbs.OrbUtilityMethods;
import aspiration.vfx.combat.BetterSmallLaserEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.mod.replay.orbs.CrystalOrb;
import com.megacrit.cardcrawl.mod.replay.orbs.HellFireOrb;
import com.megacrit.cardcrawl.mod.replay.orbs.ManaSparkOrb;
import com.megacrit.cardcrawl.mod.replay.orbs.ReplayLightOrb;
import com.megacrit.cardcrawl.orbs.*;
import conspire.orbs.Water;

import java.util.ArrayList;

public class FuseValidOrbsAction extends AbstractGameAction {
    private float duration;

    public FuseValidOrbsAction() {
        this.actionType = ActionType.SPECIAL;

        if (Settings.FAST_MODE) {
            duration = 0.45F;
        } else {
            duration = 0.6F;
        }
    }

    @Override
    public void update() {
        ArrayList<AbstractOrb> acceptableOrbs = OrbUtilityMethods.getOrbList(true, true);
        boolean hasValidOrb = false;

        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            for (AbstractOrb aorb : acceptableOrbs) {
                if (aorb.getClass().isInstance(orb)) {
                    hasValidOrb = true;
                    break;
                }
            }
            if (hasValidOrb) {
                break;
            }
        }

        if (hasValidOrb) {
            AbstractDungeon.actionManager.addToTop(new ChannelAction(new AmalgamateOrb(AbstractDungeon.player.orbs))); //this triggers last
            AbstractDungeon.player.orbs.forEach(o -> {
                for (AbstractOrb orb : acceptableOrbs) {
                    if (orb.getClass().isInstance(o)) {
                        AbstractDungeon.actionManager.addToTop(new RemoveSpecificOrbAction(o));
                        //AbstractDungeon.actionManager.addToTop(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.orbs.get(0).cX, AbstractDungeon.player.orbs.get(0).cY, o.cX, o.cY)));
                        try {
                            if (orb.ID.equals(Frost.ORB_ID) || orb.ID.equals(Plasma.ORB_ID) || (Aspiration.hasConspire && orb.ID.equals(Water.ORB_ID)) || (Aspiration.hasReplay && (orb.ID.equals(ManaSparkOrb.ORB_ID) || orb.ID.equals(CrystalOrb.ORB_ID)))) {
                                AbstractDungeon.effectList.add(new BetterSmallLaserEffect(AbstractDungeon.player.orbs.get(0).cX, AbstractDungeon.player.orbs.get(0).cY, o.cX, o.cY, duration));
                            } else if (orb.ID.equals(Dark.ORB_ID)) {
                                AbstractDungeon.effectList.add(new BetterSmallLaserEffect(AbstractDungeon.player.orbs.get(0).cX, AbstractDungeon.player.orbs.get(0).cY, o.cX, o.cY, duration, Color.PURPLE));
                            } else if (orb.ID.equals(Lightning.ORB_ID) || (Aspiration.hasReplay && orb.ID.equals(ReplayLightOrb.ORB_ID))) {
                                AbstractDungeon.effectList.add(new BetterSmallLaserEffect(AbstractDungeon.player.orbs.get(0).cX, AbstractDungeon.player.orbs.get(0).cY, o.cX, o.cY, duration, Color.YELLOW));
                            } else if (Aspiration.hasReplay && orb.ID.equals(HellFireOrb.ORB_ID)) {
                                AbstractDungeon.effectList.add(new BetterSmallLaserEffect(AbstractDungeon.player.orbs.get(0).cX, AbstractDungeon.player.orbs.get(0).cY, o.cX, o.cY, duration, Color.RED));
                            }
                        } catch (Exception e) {
                            Aspiration.logger.info(e);
                        }
                    }
                }
            });
        }

        this.isDone = true;
    }
}
