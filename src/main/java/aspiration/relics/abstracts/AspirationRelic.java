package aspiration.relics.abstracts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.Aspiration;

public abstract class AspirationRelic extends AbstractRelic
{
    public AspirationRelic(String setId, String imgName, RelicTier tier, LandingSound sfx)
    {
        super(setId, "", tier, sfx);

        imgUrl = imgName;

        if (img == null || outlineImg == null) {
            img = ImageMaster.loadImage(Aspiration.assetPath("img/relics/" + imgName));
            largeImg = ImageMaster.loadImage(Aspiration.assetPath("img/largeRelics/" + imgName));
            outlineImg = ImageMaster.loadImage(Aspiration.assetPath("img/relics/outline/" + imgName));
        }
    }

    @Override
    public void loadLargeImg()
    {
        if (largeImg == null) {
            if (imgUrl.startsWith("test")) {
                largeImg = ImageMaster.loadImage(Aspiration.assetPath("img/largeRelics/" + imgUrl));
            }
            if (largeImg == null) {
                largeImg = ImageMaster.loadImage(Aspiration.assetPath("img/largeRelics/" + imgUrl));
            }
        }
    }
    
    public boolean deckDescriptionSearch(String keyword)
    {
    	for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
    		if(c.rawDescription.toLowerCase().contains(keyword.toLowerCase())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean deckDescriptionSearch(String keyword1, String keyword2)
    {
    	for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
    		if(c.rawDescription.toLowerCase().contains(keyword1.toLowerCase()) || c.rawDescription.toLowerCase().contains(keyword2.toLowerCase())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source) { }
}
