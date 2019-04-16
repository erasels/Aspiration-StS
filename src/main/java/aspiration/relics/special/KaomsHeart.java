package aspiration.relics.special;

import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.FlameBallParticleEffect;

public class KaomsHeart extends AspirationRelic{
	public static final String ID = "aspiration:KaomsHeart";

	private static final int MAX_LIFE_MANIP = 100;
	private static final float COOLDOWN_AMT = 0.15F;
	private float cooldown = COOLDOWN_AMT;
	
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
	public void update() {
		cooldown -= Gdx.graphics.getDeltaTime();
		if (cooldown < 0.0f) {
			cooldown = COOLDOWN_AMT;
			AbstractDungeon.effectsQueue.add(new FlameBallParticleEffect(MathUtils.random(hb.x + (25*Settings.scale), ((hb.x + hb.width)-(25*Settings.scale))), MathUtils.random(hb.y + (10*Settings.scale), ((hb.y + hb.height) - (15*Settings.scale))), 5));
		}
		super.update();
	}

	@Override
	public void renderInTopPanel(SpriteBatch sb)
	{
		if (Settings.hideRelics) {
			return;
		}
		//renderOutline(sb, true);
		sb.setColor(Color.WHITE);
		sb.draw(this.img, this.currentX - 64.0F, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 0, 0, 0, 128, 128, false, false);
		renderCounter(sb, true);
		renderFlash(sb, true);
		this.hb.render(sb);
	}
    
    @Override
    public void onUnequip() {
    	AbstractDungeon.player.decreaseMaxHealth(MAX_LIFE_MANIP);
    }

	@Override
	public int getPrice() {
		if (AbstractDungeon.player.gold < 250) {
			return 250;
		}
		if (AbstractDungeon.ascensionLevel > 15) {
			return MathUtils.floor(AbstractDungeon.player.gold * 0.8f);
		}
		return MathUtils.floor(AbstractDungeon.player.gold * 0.9f);
	}

    @Override
	public boolean canSpawn() {
		return !(AbstractDungeon.floorNum > 49);
	}

    public AbstractRelic makeCopy() {
        return new KaomsHeart();
    }
}