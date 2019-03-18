package aspiration.orbs;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.mod.replay.orbs.CrystalOrb;
import com.megacrit.cardcrawl.mod.replay.orbs.HellFireOrb;
import com.megacrit.cardcrawl.mod.replay.orbs.ManaSparkOrb;
import com.megacrit.cardcrawl.mod.replay.orbs.ReplayLightOrb;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.random.Random;
import conspire.orbs.Water;

import java.util.ArrayList;

public class OrbUtilityMethods {
    public static AbstractOrb getSelectiveRandomOrb(Random rng, ArrayList<AbstractOrb> rareOrbs)
    {
        if(rng == null) {
            rng = AbstractDungeon.relicRng;
        }
        ArrayList<AbstractOrb> orbs = getOrbList();

        if(rareOrbs != null && !rareOrbs.isEmpty()) {
            boolean goodOrb = true;
            AbstractOrb tmp = null;
            AbstractOrb lastRare = null;
            while (goodOrb) {
                goodOrb = false;

                tmp = orbs.get(rng.random(orbs.size() - 1));
                for (AbstractOrb orb : rareOrbs) {
                    if (tmp.name.equals(orb.name)) {
                        if(lastRare != null && (lastRare.name.equals(tmp.name))) {
                            continue;
                        }
                        lastRare = tmp.makeCopy();
                        tmp = orbs.get(rng.random(orbs.size() - 1));
                        goodOrb = true;
                    }
                }

            }
            return tmp;
        }


        return orbs.get(rng.random(orbs.size() - 1));
    }

    public static AbstractOrb getSelectiveRandomOrb(Random rng) {
        ArrayList<AbstractOrb> rareOrbs = new ArrayList<>();
        rareOrbs.add(new Plasma());

        if(Aspiration.hasConspire) {
            rareOrbs.add(new Water());
        }

        if(Aspiration.hasReplay) {
            rareOrbs.add(new ReplayLightOrb());

            if(Aspiration.hasMarisa) {
                rareOrbs.add(new ManaSparkOrb());
            }
        }

        return getSelectiveRandomOrb(rng, rareOrbs);
    }

    public static ArrayList<AbstractOrb> getOrbList() {
        ArrayList<AbstractOrb> orbs = new ArrayList<>();
        orbs.add(new Dark());
        orbs.add(new Frost());
        orbs.add(new Lightning());
        orbs.add(new Plasma());

        if(Aspiration.hasConspire) {
            orbs.add(new Water());
        }

        if(Aspiration.hasReplay) {
            orbs.add(new CrystalOrb());
            orbs.add(new HellFireOrb());
            orbs.add(new ReplayLightOrb());

            if(Aspiration.hasMarisa) {
                orbs.add(new ManaSparkOrb());
            }
        }

        return orbs;
    }
}
