package aspiration.relics.boss;

import aspiration.relics.abstracts.AspirationRelic;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.*;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Mageblood extends AspirationRelic {
    public static final String ID = "aspiration:Mageblood";

    public Mageblood() {
        super(ID, "Mageblood.png", AbstractRelic.RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        for(AbstractPotion p : AbstractDungeon.player.potions) {
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

    @Override
    public void onEquip() {
        //Remove rare potions
        for(AbstractPotion p : AbstractDungeon.player.potions) {
            if(!(p instanceof PotionSlot) && p.rarity == AbstractPotion.PotionRarity.RARE) {
                AbstractDungeon.topPanel.destroyPotion(p.slot);
            }
        }
    }

    public void onPotionGet(AbstractPotion potion) {
        if(potion.rarity == AbstractPotion.PotionRarity.RARE) {
            AbstractDungeon.topPanel.destroyPotion(potion.slot);
            flash();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Mageblood();
    }

    //Dynamically patch canUse of potions to check if the player has mageblood
    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class CantUsePotionPatch {
        public static void Raw(CtBehavior ctBehavior) throws NotFoundException {
            ClassFinder finder = new ClassFinder();

            finder.add(new File(Loader.STS_JAR));

            for (ModInfo modInfo : Loader.MODINFOS) {
                if (modInfo.jarURL != null) {
                    try {
                        finder.add(new File(modInfo.jarURL.toURI()));
                    } catch (URISyntaxException e) {
                        // do nothing
                    }
                }
            }

            // Get all classes for AbstractPotion
            ClassFilter filter = new AndClassFilter(
                    new NotClassFilter(new InterfaceOnlyClassFilter()),
                    new ClassModifiersClassFilter(Modifier.PUBLIC),
                    new OrClassFilter(
                            new org.clapper.util.classutil.SubclassClassFilter(AbstractPotion.class),
                            (classInfo, classFinder) -> classInfo.getClassName().equals(AbstractPotion.class.getName())
                    )
            );

            ArrayList<ClassInfo> foundClasses = new ArrayList<>();
            finder.findClasses(foundClasses, filter);

            for (ClassInfo classInfo : foundClasses) {
                CtClass ctClass = ctBehavior.getDeclaringClass().getClassPool().get(classInfo.getClassName());

                try {
                    CtMethod[] methods = ctClass.getDeclaredMethods();
                    for (CtMethod m : methods) {
                        if (m.getName().equals("canUse")) {
                            m.insertAfter("{" +
                                    "$_ = $_ && !" + CantUsePotionPatch.class.getName() + ".hasBlood();" +
                                    "}");
                        }
                    }
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }

        public static boolean hasBlood() {
           return AbstractDungeon.player.hasRelic(Mageblood.ID);
        }
    }
}
