package aspiration.relics;

import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class KaomsHeart extends AspirationRelic{
	public static final String ID = "aspiration:KaomsHeart";

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

    	int tmp = AbstractDungeon.bossRelicPool.size();
    	AbstractDungeon.bossRelicPool.clear();
    	for(int i =0; i<tmp;i++) {
			AbstractDungeon.bossRelicPool.add(KaomsHeart_nothing.ID);
		}

    	tmp = AbstractDungeon.commonRelicPool.size();
    	AbstractDungeon.commonRelicPool.clear();
		for(int i =0; i<tmp;i++) {
			AbstractDungeon.commonRelicPool.add(KaomsHeart_nothing.ID);
		}

		tmp = AbstractDungeon.rareRelicPool.size();
    	AbstractDungeon.rareRelicPool.clear();
		for(int i =0; i<tmp;i++) {
			AbstractDungeon.rareRelicPool.add(KaomsHeart_nothing.ID);
		}

		tmp = AbstractDungeon.shopRelicPool.size();
    	AbstractDungeon.shopRelicPool.clear();
		for(int i =0; i<tmp;i++) {
			AbstractDungeon.shopRelicPool.add(KaomsHeart_nothing.ID);
		}

		tmp = AbstractDungeon.uncommonRelicPool.size();
    	AbstractDungeon.uncommonRelicPool.clear();
		for(int i =0; i<tmp;i++) {
			AbstractDungeon.uncommonRelicPool.add(KaomsHeart_nothing.ID);
		}
    }
    
    @Override
    public void onUnequip() {
    	AbstractDungeon.player.decreaseMaxHealth(MAX_LIFE_MANIP);
    }
    
    @Override
    public int getPrice() {
    	if(AbstractDungeon.player.gold < 200) {
    		return 200;
		} else {
			return MathUtils.floor(AbstractDungeon.player.gold * 0.8f);
		}
    }

    @Override
	public boolean canSpawn() {
		return !(AbstractDungeon.floorNum > 49);
	}

    public AbstractRelic makeCopy() {
        return new KaomsHeart();
    }
}