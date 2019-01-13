package aspiration.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import aspiration.relics.abstracts.AspirationRelic;
import aspiration.vfx.ObtainRelicLater;

public class Nostalgia extends AspirationRelic{
	public static final String ID = "aspiration:Nostalgia";
	
	private static final int PRICE = 333;
	private boolean picked_rarity = false;
	//private static final boolean SETTING_UNCMN = Aspiration.uncommonNostalgia();
	
    public Nostalgia(boolean uncmn_rarity) {
        super(ID, "Nostalgia.png", (uncmn_rarity) ? RelicTier.UNCOMMON : RelicTier.SHOP, LandingSound.HEAVY);
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

    	if(tmp.isEmpty()) {
    		tmp.add(new Circlet());
    		tmp.add(new Circlet());
    		tmp.add(new Circlet());
    	}
    	AbstractRelic starter = tmp.get(rng.random(tmp.size() - 1));
    	
    	AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(starter));
    }
    
    @Override
    public int getPrice() {
    	return PRICE;
    }

    public AbstractRelic makeCopy() {
        return new Nostalgia(picked_rarity);
    }
}