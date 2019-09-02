package aspiration.ui.campfire;

import aspiration.Aspiration;
import aspiration.relics.special.ArtOfWarUpgrade;
import aspiration.vfx.campfire.CampfireReadArtOfWarEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ArtOfWar;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class ReadArtOfWarOption extends AbstractCampfireOption
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("aspiration:ArtOfWarUpgradeOption");
    public static final String[] TEXT = uiStrings.TEXT;

    private static int NS = ArtOfWarUpgrade.NEEDED_SESSIONS;

    public ReadArtOfWarOption() {
        this.label = TEXT[0];
        this.description = TEXT[1] + TEXT[2] + (NS - AbstractDungeon.player.getRelic(ArtOfWar.ID).counter) + TEXT[3];
        this.img = ImageMaster.loadImage(Aspiration.assetPath("img/UI/campfire/readAoW.png"));
    }

    @Override
    public void useOption() {
        AbstractRelic aow = AbstractDungeon.player.getRelic(ArtOfWar.ID);
        if(aow.counter >= NS - 1) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ArtOfWarUpgrade());
        } else {
            if(aow.counter < 1) {
                aow.counter = 1;
            } else {
                aow.counter++;
            }
        }
        AbstractDungeon.effectList.add(new CampfireReadArtOfWarEffect());
    }
}