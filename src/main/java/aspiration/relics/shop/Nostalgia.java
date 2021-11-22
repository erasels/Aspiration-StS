package aspiration.relics.shop;

import aspiration.relics.abstracts.AspirationRelic;
import aspiration.vfx.ObtainRelicLater;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Arrays;

public class Nostalgia extends AspirationRelic{
	public static final String ID = "aspiration:Nostalgia";

	private static final ArrayList<String> bannedStarters = new ArrayList<>(Arrays.asList("Slimebound:AbsorbEndCombat","champ:ChampionCrown", "hexamod:SpiritBrand", "sneckomod:SneckoSoul", "bronze:BronzeCore", "Uniform", "HollowMod:VesselMask", "TheKombatant:ShinnoksAmuletRelic",
			"animator:LivingPicture", "animator:TheMissingPiece", "TheTemplar:CodeOfChivalry", "theSenshi:TransformationBroochRelic", "bard:PitchPipe", "wariomod:TheHat"));

	public Nostalgia() {
		super(ID, "Nostalgia.png", RelicTier.SHOP, LandingSound.MAGICAL);
	}

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void onEquip() {
    	Random rng = AbstractDungeon.relicRng;
    	ArrayList<String> tmp = new ArrayList<>();
    	//AbstractDungeon.player.loseRelic(ID);
    	for(AbstractRelic r : RelicLibrary.starterList) {
    		if(!AbstractDungeon.player.hasRelic(r.relicId)) {
    			tmp.add(r.relicId);
    		}
    	}

		tmp.removeIf(bannedStarters::contains);

    	if(!tmp.isEmpty()) {
			String starter = tmp.get(rng.random(tmp.size() - 1));

			AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(RelicLibrary.getRelic(starter).makeCopy()));
		}
    }
}