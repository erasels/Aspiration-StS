package aspiration.relics.abstracts;

import aspiration.Aspiration;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

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

    //For mods that use CustomRelics
    public AspirationRelic(String setId, Texture img, Texture outline, RelicTier tier, LandingSound sfx)
    {
        super(setId, "", tier, sfx);
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outline.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.img = img;
        largeImg = img;
        outlineImg = outline;
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

    public void atb(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToBottom(a);
    }

    public void att(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToTop(a);
    }

    public void onRelicGet(AbstractRelic r) { }
}
