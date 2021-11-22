package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.Watcher;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static aspiration.Aspiration.logger;

public class WatcherSkillbook extends SkillbookRelic implements OnAfterUseCardRelic {
    public static final String ID = "aspiration:WatcherSkillbook";
    private AbstractCard.CardType lastPlayed = null;

    public WatcherSkillbook() {
        super(ID, "WatcherSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if(Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] +  DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard abstractCard, UseCardAction useCardAction) {
        if((lastPlayed == AbstractCard.CardType.SKILL && abstractCard.type == AbstractCard.CardType.ATTACK) || (lastPlayed == AbstractCard.CardType.ATTACK && abstractCard.type == AbstractCard.CardType.SKILL)) {
            flash();
            atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MantraPower(AbstractDungeon.player, 1), 1));
            lastPlayed = null;
            return;
        }

        lastPlayed = abstractCard.type;
    }

    @Override
    public void onVictory() {
        lastPlayed = null;
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTop) {
        super.renderCounter(sb, inTop);
        if(lastPlayed == AbstractCard.CardType.ATTACK || lastPlayed == AbstractCard.CardType.SKILL) {
            String tmp = lastPlayed == AbstractCard.CardType.ATTACK?"SKL":"ATK";
            FontHelper.renderFontRightTopAligned(sb,
                    FontHelper.topPanelInfoFont,
                    tmp, hb.cX - ((hb.x - hb.cX)/2) + (9f*Settings.scale),
                    this.currentY - 7.0F * Settings.scale, lastPlayed == AbstractCard.CardType.ATTACK? Color.GREEN:Color.RED);
        }
    }

    @Override
    public void onEquip() {
        modifyCardPool();
    }

    public void modifyCardPool() {
        if(Aspiration.skillbookCardpool()) {
            logger.info("Watcher Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards= CardLibrary.getCardList(CardLibrary.LibraryType.PURPLE);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return !(AbstractDungeon.player instanceof Watcher) && !hasSkillbookRelic(AbstractDungeon.player);
    }
}