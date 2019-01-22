package aspiration.events;

import aspiration.Aspiration;
import aspiration.relics.RitualStick;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.CultistMask;

public class CultistTraining extends AbstractImageEvent {
    public static final String ID = "aspiration:CultistTraining";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private int chosen_option;
    private static final float PERDAMAGE = 0.07f;
    private static final float ASC_PERDAMAGE = 0.1f;

    private State state;

    public enum State{
        CHOOSING,
        LEAVING
    }

    public CultistTraining(){
        super(NAME, DESCRIPTIONS[0], Aspiration.assetPath("img/events/CultistTraining/start.jpg"));
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            imageEventText.setDialogOption(OPTIONS[0], false);
            if (AbstractDungeon.ascensionLevel >= 15) {
                imageEventText.setDialogOption(OPTIONS[1] + MathUtils.round(AbstractDungeon.player.maxHealth*ASC_PERDAMAGE) + OPTIONS[5], true);
            } else {
                imageEventText.setDialogOption(OPTIONS[1] + MathUtils.round(AbstractDungeon.player.maxHealth*PERDAMAGE) + OPTIONS[5], true);
            }

        } else {
            imageEventText.setDialogOption(OPTIONS[3], true);
            if (AbstractDungeon.ascensionLevel >= 15) {
                imageEventText.setDialogOption(OPTIONS[1] + MathUtils.round(AbstractDungeon.player.maxHealth*ASC_PERDAMAGE) + OPTIONS[5], false);
            } else {
                imageEventText.setDialogOption(OPTIONS[1] + MathUtils.round(AbstractDungeon.player.maxHealth*PERDAMAGE) + OPTIONS[5], false);
            }
        }

        imageEventText.setDialogOption(OPTIONS[2]);

        state = CultistTraining.State.CHOOSING;
    }

    @Override
    protected void buttonEffect(int pressedButton) {
        switch(state) {
            case CHOOSING:
                switch (pressedButton) {
                    //CultistMask free relic
                    case 0:
                        CardCrawlGame.sound.play("VO_CULTIST_2B");
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new RitualStick());
                        chosen_option = 1;
                        break;
                    //Lose 7-10% HP to get relic
                    case 1:
                        CardCrawlGame.sound.play("VO_CULTIST_1C");
                        CardCrawlGame.sound.play("BLUNT_FAST");
                        if (AbstractDungeon.ascensionLevel >= 15) {
                            AbstractDungeon.player.damage(new DamageInfo(null, MathUtils.round(AbstractDungeon.player.maxHealth*ASC_PERDAMAGE), DamageInfo.DamageType.HP_LOSS));
                        } else {
                            AbstractDungeon.player.damage(new DamageInfo(null, MathUtils.round(AbstractDungeon.player.maxHealth*PERDAMAGE), DamageInfo.DamageType.HP_LOSS));
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new RitualStick());
                        chosen_option = 2;
                        break;
                    //Ignore
                    case 2:
                        chosen_option = 3;
                        break;
                }
                imageEventText.updateBodyText(DESCRIPTIONS[chosen_option]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[4]);
                state = State.LEAVING;
                break;
            case LEAVING:
                openMap();
                break;
        }
    }
}