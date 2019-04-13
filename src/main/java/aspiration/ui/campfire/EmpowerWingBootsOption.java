package aspiration.ui.campfire;

import aspiration.Aspiration;
import aspiration.patches.WingBootsCampfireactionField;
import aspiration.vfx.campfire.EmpowerWingBootsAction;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.lang.reflect.Method;

public class EmpowerWingBootsOption extends AbstractCampfireOption
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("aspiration:EmpowerWingBootsOption");
    public static final String[] TEXT = uiStrings.TEXT;

    private static final int CHARGES = 3;

    public EmpowerWingBootsOption() {
        this.label = TEXT[0];
        this.description = TEXT[1] + CHARGES + TEXT[2];
        this.img = ImageMaster.loadImage(Aspiration.assetPath("img/UI/campfire/empowerWB.png"));
    }

    @Override
    public void useOption() {
        WingBoots wb = (WingBoots)AbstractDungeon.player.getRelic(WingBoots.ID);
        if(wb.usedUp) {
            wb.usedUp = false;
            wb.description = RelicLibrary.getRelic(WingBoots.ID).description;
            wb.tips.clear();
            wb.tips.add(new PowerTip(wb.name, wb.description));
            try {
                Method m = wb.getClass().getDeclaredMethod("initializeTips");
                m.setAccessible(true);
                m.invoke(wb);
            } catch(Exception e) {
                Aspiration.logger.info(e);
            }
            //wb.initializeTips();

            ReflectionHacks.setPrivate(wb, AbstractRelic.class, "img", ImageMaster.loadImage(Aspiration.assetPath("img/relics/winged.png")));
            wb.setCounter(CHARGES);
        } else if(wb.counter < 1) {
            wb.setCounter(CHARGES);
        } else {
            wb.counter += CHARGES;
        }
        wb.flash();
        WingBootsCampfireactionField.campUsed.set(wb, true);
        AbstractDungeon.effectList.add(new EmpowerWingBootsAction());
    }
}