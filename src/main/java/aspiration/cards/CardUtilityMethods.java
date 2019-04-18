package aspiration.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRng;

public class CardUtilityMethods {
    public static ArrayList<AbstractCard> getSpecificRandomCard(AbstractCard.CardRarity rarity, AbstractCard.CardType type, AbstractCard.CardColor col, int amount) {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        ArrayList<AbstractCard> cards = new ArrayList<>();

        ArrayList<AbstractCard> allCards = new ArrayList<>(CardLibrary.getAllCards());

        for (AbstractCard c : allCards) {
            if (c.color == col && c.rarity == rarity && c.type == type) {
                temp.addToTop(c);
            }
        }

        for(int i = 0; i<amount; i++) {
            cards.add(temp.getRandomCard(cardRng));
        }

        return cards;
    }

    public static ArrayList<AbstractCard> getSpecificRandomCard(AbstractCard.CardType type, AbstractCard.CardColor col, int amount) {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        ArrayList<AbstractCard> cards = new ArrayList<>();

        ArrayList<AbstractCard> allCards = new ArrayList<>(CardLibrary.getAllCards());

        for (AbstractCard c : allCards) {
            if (c.color == col && c.type == type) {
                temp.addToTop(c);
            }
        }

        for(int i = 0; i<amount; i++) {
            cards.add(temp.getRandomCard(cardRng));
        }

        return cards;
    }
}
