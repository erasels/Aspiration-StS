package aspiration.relics.boss;

import aspiration.relics.abstracts.AspirationRelic;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;

import static java.lang.Math.min;


public class RandomNobGenerator extends AspirationRelic {
    public static final String ID = "aspiration:RandomNobGenerator";

    private static final int ENERGY_AMT = 2;
    private static final int NOB_CHANCE = 40;
    private static final float FINAL_XOFFSET = 150F;
    private static final float FINAL_YOFFSET = 10F;

    public RandomNobGenerator() {
        super(ID, "RandomNobGenerator.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + NOB_CHANCE + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += ENERGY_AMT;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster += ENERGY_AMT;
    }

    //Called before atPreBattle in the applyPreCombatLogic patch
    @Override
    public void onTrigger() {
        int rng = AbstractDungeon.monsterRng.random(99) + 1;
        float offsetX = 0;
        float offsetY = 0;
        if (rng <= NOB_CHANCE) {
            flash();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                offsetX = min(((m.drawX - ((float) Settings.WIDTH * 0.75F)) / Settings.scale), offsetX);
                offsetY = min(m.drawY, offsetY);
            }
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new GremlinNob(offsetX - (FINAL_XOFFSET * Settings.scale), offsetY - (FINAL_YOFFSET * Settings.scale)), false));
        }
    }
}