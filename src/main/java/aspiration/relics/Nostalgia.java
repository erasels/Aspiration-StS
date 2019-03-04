package aspiration.relics;

import aspiration.relics.abstracts.AspirationRelic;
import aspiration.vfx.ObtainRelicLater;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class Nostalgia extends AspirationRelic{
	public static final String ID = "aspiration:Nostalgia";

	private boolean picked_rarity = false;
	//private static final boolean SETTING_UNCMN = Aspiration.uncommonNostalgia();

	public Nostalgia() {
		super(ID, "Nostalgia.png", RelicTier.SHOP, LandingSound.MAGICAL);
		picked_rarity = false;
	}

    public Nostalgia(boolean uncmn_rarity) {
        super(ID, "Nostalgia.png", (uncmn_rarity) ? RelicTier.UNCOMMON : RelicTier.SHOP, LandingSound.MAGICAL);
        picked_rarity = uncmn_rarity;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void onEquip() {
    	Random rng = AbstractDungeon.relicRng;
    	ArrayList<AbstractRelic> tmp = new ArrayList<AbstractRelic>();
    	//AbstractDungeon.player.loseRelic(ID);
    	for(AbstractRelic r : RelicLibrary.starterList) {
    		if(!AbstractDungeon.player.hasRelic(r.relicId)) {
    			tmp.add(r.makeCopy());
    		}
    	}

    	if(!tmp.isEmpty()) {
			AbstractRelic starter = tmp.get(rng.random(tmp.size() - 1));

			AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(starter));
		}
    }

    public AbstractRelic makeCopy() {
        return new Nostalgia(picked_rarity);
    }
}