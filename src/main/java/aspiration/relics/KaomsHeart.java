package aspiration.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class KaomsHeart extends AspirationRelic{
	public static final String ID = "aspiration:KaomsHeart";
	
	private static final int LOW_PRICE = 300;
	private static final int MID_PRICE = 600;
	private static final int HIGH_PRICE = 900;
	private static final int MAX_LIFE_MANIP = 100;
	
    public KaomsHeart() {
        super(ID, "KaomsHeart.png", RelicTier.SHOP, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MAX_LIFE_MANIP + DESCRIPTIONS[1];
    }
    
    @Override
    public void onEquip() {
    	AbstractDungeon.player.increaseMaxHp(MAX_LIFE_MANIP, true);
    	
    	AbstractDungeon.bossRelicPool.clear();
    	AbstractDungeon.bossRelicPool.add("Circlet");
    	
    	AbstractDungeon.commonRelicPool.clear();
    	AbstractDungeon.commonRelicPool.add("Circlet");
    	
    	AbstractDungeon.rareRelicPool.clear();
    	AbstractDungeon.rareRelicPool.add("Circlet");
    	
    	AbstractDungeon.shopRelicPool.clear();
    	AbstractDungeon.shopRelicPool.add("Circlet");
    	
    	AbstractDungeon.uncommonRelicPool.clear();
    	AbstractDungeon.uncommonRelicPool.add("Circlet");
    }
    
    @Override
    public void onUnequip() {
    	AbstractDungeon.player.decreaseMaxHealth(MAX_LIFE_MANIP);
    }
    
    @Override
    public int getPrice() {
    	if(AbstractDungeon.floorNum <= 16) {
    		return LOW_PRICE;
    	} else if (AbstractDungeon.floorNum <= 35) {
    		return MID_PRICE;
    	} else {
    		return HIGH_PRICE + (AbstractDungeon.floorNum * 10);
    	}
    }

    public AbstractRelic makeCopy() {
        return new KaomsHeart();
    }
}