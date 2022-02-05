package aspiration.powers;

import aspiration.powers.abstracts.AspirationPower;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ASSPower extends AspirationPower implements InvisiblePower, HealthBarRenderPower {
    public static final String POWER_ID = "aspiration:ASSPower";
    private AbstractRelic reference;

    public ASSPower(AbstractRelic ASS) {
        this.name = "";
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.type = NeutralPowertypePatch.NEUTRAL;
        reference = ASS;
    }


    @Override
    public int getHealthBarAmount() {
        return reference.counter;
    }

    @Override
    public Color getColor() {
        if(reference.counter > owner.currentBlock) {
            return Color.WHITE;
        } else {
            return Settings.BLUE_TEXT_COLOR;
        }
    }
}
