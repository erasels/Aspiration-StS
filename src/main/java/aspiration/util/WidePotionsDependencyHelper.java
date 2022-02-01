package aspiration.util;

import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotionSlot;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

public class WidePotionsDependencyHelper {
    public static void useWidePotionSlot() {
        for(AbstractPotion p : WidePotionSlot.Field.widepotions.get(AbstractDungeon.player)) {
            if(!(p instanceof PotionSlot)) {
                if(p.isThrown) {
                    AbstractMonster target = AbstractDungeon.getRandomMonster(null);
                    p.use(target);
                } else {
                    p.use(AbstractDungeon.player);
                }
            }
        }
    }
}
