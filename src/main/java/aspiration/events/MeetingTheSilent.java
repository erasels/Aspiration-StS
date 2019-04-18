package aspiration.events;

import aspiration.Aspiration;
import aspiration.cards.CardUtilityMethods;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class MeetingTheSilent extends AbstractImageEvent {
    public static final String ID = "aspiration:MeetingTheSilent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private int chosen_option;
    private boolean cardsSelected = true;

    private static final int CARD_TRADE_AMT = 3;
    private static final float STRONG_HEAL = 0.1f;
    private static final float WEAK_HEAL = 0.05f;

    private MeetingTheSilent.State state;

    public enum State {
        CHOOSING,
        LEAVING
    }

    public MeetingTheSilent() {
        super(NAME, DESCRIPTIONS[0], Aspiration.assetPath("img/events/MeetingTheSilent/start.jpg"));
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1] + MathUtils.round(AbstractDungeon.player.maxHealth*((AbstractDungeon.ascensionLevel >= 15)? WEAK_HEAL:STRONG_HEAL)) + OPTIONS[4] );
        imageEventText.setDialogOption(OPTIONS[2]);

        state = MeetingTheSilent.State.CHOOSING;
    }

    @Override
    protected void buttonEffect(int pressedButton) {
        switch (state) {
            case CHOOSING:
                switch (pressedButton) {
                    //Trade three cards for cards of same type and rarity from Silent
                    case 0:
                        chosen_option = 1;
                        cardsSelected = false;
                        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                            if (card.type == AbstractCard.CardType.ATTACK || card.type == AbstractCard.CardType.SKILL || card.type == AbstractCard.CardType.POWER) {
                                tmp.addToTop(card);
                            }
                        }

                        if (tmp.group.isEmpty()) {
                            this.cardsSelected = true;
                            return;
                        }
                        if (tmp.group.size() <= CARD_TRADE_AMT) {
                            for (AbstractCard card : tmp.group) {
                                AbstractDungeon.player.masterDeck.removeCard(card);
                                AbstractDungeon.player.masterDeck.addToTop(CardUtilityMethods.getSpecificRandomCard(card.type, AbstractCard.CardColor.GREEN, 1).get(0).makeCopy());
                            }
                            this.cardsSelected = true;
                        } else if (!AbstractDungeon.isScreenUp) {
                            AbstractDungeon.gridSelectScreen.open(tmp, CARD_TRADE_AMT, DESCRIPTIONS[4], false, false, false, false);
                        } else {
                            AbstractDungeon.dynamicBanner.hide();
                            AbstractDungeon.previousScreen = AbstractDungeon.screen;
                            AbstractDungeon.gridSelectScreen.open(tmp, CARD_TRADE_AMT, DESCRIPTIONS[4], false, false, false, false);
                        }
                        break;
                    //Heal 10% HP
                    case 1:
                        playSleepJingle();
                        if (AbstractDungeon.ascensionLevel >= 15) {
                            AbstractDungeon.player.heal(MathUtils.round(AbstractDungeon.player.maxHealth * WEAK_HEAL));
                        } else {
                            AbstractDungeon.player.heal(MathUtils.round(AbstractDungeon.player.maxHealth * STRONG_HEAL));
                        }
                        chosen_option = 2;
                        break;
                    //Ignore
                    case 2:
                        chosen_option = 3;
                        break;
                }
                imageEventText.updateBodyText(DESCRIPTIONS[chosen_option]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[3]);
                state = MeetingTheSilent.State.LEAVING;
                break;
            case LEAVING:
                openMap();
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if ((!cardsSelected) && (AbstractDungeon.gridSelectScreen.selectedCards.size() == CARD_TRADE_AMT)) {
            cardsSelected = true;
            float displayCount = 0.0F;
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                card.untip();
                card.unhover();
                AbstractDungeon.player.masterDeck.removeCard(card);
                //AbstractDungeon.player.masterDeck.addToTop(CardUtilityMethods.getSpecificRandomCard(card.rarity, card.type, card.color, 1).get(0));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(CardUtilityMethods.getSpecificRandomCard(card.type, AbstractCard.CardColor.GREEN, 1).get(0).makeCopy(), Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F, false));
                displayCount += Settings.WIDTH / 6.0F;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }
    }

    private void playSleepJingle() {
        final int roll = MathUtils.random(0, 2);
        final String id = AbstractDungeon.id;
        switch (id) {
            case "Exordium": {
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_1-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_1-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_1-3");
                break;
            }
            case "TheCity": {
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_2-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_2-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_2-3");
                break;
            }
            case "TheBeyond": {
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_3-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_3-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_3-3");
                break;
            }
        }
    }
}