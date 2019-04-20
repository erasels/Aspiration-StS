package aspiration.orbs;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.mod.replay.orbs.*;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.random.Random;
import conspire.orbs.Water;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import vexMod.orbs.GoldenLightning;

import java.util.ArrayList;

public class OrbUtilityMethods {
    public static AbstractOrb getSelectiveRandomOrb(Random rng, ArrayList<AbstractOrb> rareOrbs, boolean forAmalgamate)
    {
        if(rng == null) {
            rng = AbstractDungeon.relicRng;
        }
        ArrayList<AbstractOrb> orbs = getOrbList(false, forAmalgamate);

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

    public static AbstractOrb getSelectiveRandomOrb(Random rng, ArrayList<AbstractOrb> rareOrbs) {
        return getSelectiveRandomOrb(rng, rareOrbs, false);
    }

    public static AbstractOrb getSelectiveRandomOrb(Random rng) {
        return getSelectiveRandomOrb(rng, getRareOrbList());
    }

    public static AbstractOrb getRandomAmalgamate(Random rng, int orbAmt, ArrayList<AbstractOrb> rareOrbs) {
        if(orbAmt < 1) orbAmt = 1;
        ArrayList<AbstractOrb> orbList = new ArrayList<>();
        for(int i = 0; i<orbAmt;i++) {
            orbList.add(getSelectiveRandomOrb(rng, rareOrbs, true));
        }
        return new AmalgamateOrb(orbList);
    }

    public static AbstractOrb getRandomAmalgamate(Random rng, int orbAmt) {
        return getRandomAmalgamate(rng, orbAmt, getRareOrbList());
    }

    public static ArrayList<AbstractOrb> getRareOrbList() {
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

        if (Aspiration.hasVex)
        {
            rareOrbs.add(new GoldenLightning());
        }
        return rareOrbs;
    }

    public static ArrayList<AbstractOrb> getOrbList(boolean withAmalgamate, boolean forAmalgamate) {
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

        if (Aspiration.hasVex)
        {
            orbs.add(new GoldenLightning());
        }

        if(withAmalgamate) {
            orbs.add(new AmalgamateOrb());
        }

        if(!forAmalgamate) {
            if(Aspiration.hasAnimator) {
                orbs.add(new Earth());
                orbs.add(new Fire());
            }
        } else {
            orbs.add(new GlassOrb());
        }

        return orbs;
    }

    public static ArrayList<AbstractOrb> getOrbList(boolean withAmalgamate) {
        return getOrbList(withAmalgamate, false);
    }

    public static ArrayList<AbstractOrb> getOrbList() {
        return getOrbList(false);
    }

    public static boolean isValidAmalgamateComponent(AbstractOrb o) {
        if(o != null) {
            if (o.ID != null) {
                for (AbstractOrb orb : getOrbList(true, true)) {
                    if (o.ID.equals(orb.ID)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
