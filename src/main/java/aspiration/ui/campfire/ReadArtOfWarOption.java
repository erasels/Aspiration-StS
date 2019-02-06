package aspiration.ui.campfire;

import aspiration.Aspiration;
import aspiration.relics.ArtOfWarUpgrade;
import aspiration.vfx.campfire.CampfireReadArtOfWarEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class ReadArtOfWarOption extends AbstractCampfireOption
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("aspiration:ArtOfWarUpgradeOption");
    public static final String[] TEXT = uiStrings.TEXT;

    public ReadArtOfWarOption() {
        this.label = TEXT[0];
        this.description = TEXT[1];
        this.img = ImageMaster.loadImage(Aspiration.assetPath("img/UI/campfire/readAoW.png"));
    }

    @Override
    public void useOption() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ArtOfWarUpgrade());
        AbstractDungeon.effectList.add(new CampfireReadArtOfWarEffect());
    }
}