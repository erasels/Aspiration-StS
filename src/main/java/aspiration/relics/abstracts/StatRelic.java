package aspiration.relics.abstracts;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StatRelic extends AspirationRelic{
    public LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();

    public StatRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
        statsInit();
    }

    public void statsInit() {

    }

    public String getStatsDescription() {
        StringBuilder s = new StringBuilder();
        for(Map.Entry<String, Integer> e : stats.entrySet()) {
            s.append(e.getKey()).append(e.getValue());
        }
        return s.toString();
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        for(Map.Entry<String, Integer> e : stats.entrySet()) {
            stats.put(e.getKey(), 0);
        }
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        for(Map.Entry<String, Integer> e : stats.entrySet()) {
            statsToSave.add(e.getValue());
        }
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for(Map.Entry<String, Integer> e : stats.entrySet()) {
                stats.put(e.getKey(), jsonArray.get(0).getAsInt());
            }
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        AbstractRelic r = null;
        try {
            r = this.getClass().newInstance();
            ((StatRelic)r).stats = this.stats;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }
}
