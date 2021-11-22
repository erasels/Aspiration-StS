package aspiration.util;

import aspiration.relics.abstracts.StatRelic;

public class RelicStatsHelper {
    public static void incrementStat(StatRelic r, String s, int i) {
        r.stats.put(s, r.stats.get(s) + i);
    }

    public static void incrementStat(StatRelic r, String s) {
        incrementStat(r, s, 1);
    }
}
