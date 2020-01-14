package aspiration.blights.abstracts;

import aspiration.Aspiration;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;

public class AspirationBlight extends AbstractBlight {

    public static BlightStrings STRINGS;
    public boolean usedUp;
    private String imgUrl;

    public AspirationBlight(String id, String textureString) {
        super(id, "", "", "", true);

        STRINGS = CardCrawlGame.languagePack.getBlightString(id);
        description = getDescription();
        name = STRINGS.NAME;

        imgUrl = textureString;
        img = ImageMaster.loadImage(Aspiration.assetPath("img/blights/" + imgUrl));
        outlineImg = ImageMaster.loadImage(Aspiration.assetPath("img/blights/outline/" + imgUrl));

        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
    }

    public void usedUp() {
        usedUp = true;
        description = getUsedUpMsg();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
        img = ImageMaster.loadImage(Aspiration.assetPath("img/blights/used/" + imgUrl));
    }

    private static String getDescription() {
        return STRINGS.DESCRIPTION[0];
    }
    private static String getUsedUpMsg() {
        return STRINGS.DESCRIPTION[1];
    }
}