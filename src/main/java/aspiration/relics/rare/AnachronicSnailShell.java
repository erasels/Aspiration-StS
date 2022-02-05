package aspiration.relics.rare;

import aspiration.powers.ASSPower;
import aspiration.relics.abstracts.AspirationRelic;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import org.apache.commons.lang3.math.NumberUtils;

public class AnachronicSnailShell extends AspirationRelic implements BetterOnLoseHpRelic {
	public static final String ID = "aspiration:AnachronicSnailShell";
	
	private static final int THRESHOLD = 6;

    public AnachronicSnailShell() {
        super(ID, "AnachronicSnailShell.png", RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], THRESHOLD);
    }

    @Override
    public void atBattleStart() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ASSPower(this)));
        setCounter(0);
    }

    @Override
    public int betterOnLoseHp(DamageInfo info, int Amount) {
        if (info.owner != AbstractDungeon.player && info.owner != null && AbstractDungeon.actionManager.turnHasEnded && info.type == DamageType.NORMAL && Amount > 0 && counter < THRESHOLD) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("POWER_TIME_WARP"));
            beginLongPulse();

            int tmp = NumberUtils.min(Amount, THRESHOLD - counter);
            counter += tmp;
            return Amount - tmp;
        }
        return Amount;
    }

    @Override
    public void onPlayerEndTurn() {
    	if(counter > 0) {
    		//AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), counter, DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new FlashPowerEffect(new TimeWarpPower(AbstractDungeon.player)), 0.0F));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.getMonsters().getRandomMonster(true), counter, DamageType.THORNS), AttackEffect.BLUNT_LIGHT));
    		setCounter(0);
    		stopPulse();
    	}
    }
    
    @Override
    public void onVictory() {
    	setCounter(-1);
    	stopPulse();
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        if (this.counter > -1) {
            if (inTopPanel) {
                Color c = Color.WHITE;
                if(counter >= THRESHOLD) {
                    c = Color.SALMON;
                }
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, counter + "/" + THRESHOLD, ((float)ReflectionHacks.getPrivateStatic(AbstractRelic.class, "offsetX")) + currentX + 30.0F * Settings.scale, currentY - 7.0F * Settings.scale, c);
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(counter), currentX + 30.0F * Settings.scale, currentY - 7.0F * Settings.scale, Color.WHITE);
            }
        }
    }
}