package aspiration.relics.boss;

import aspiration.Aspiration;
import aspiration.Utility.TextureLoader;
import aspiration.relics.abstracts.AspirationRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
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

    public static Texture cross = TextureLoader.getTexture(Aspiration.assetPath("img/UI/cross.png"));

    @SpirePatch2(clz = AbstractPotion.class, method = "shopRender")
    public static class RarePotionWarningShop {
        @SpirePostfixPatch
        public static void patch(AbstractPotion __instance, SpriteBatch sb) {
            if(__instance.rarity == AbstractPotion.PotionRarity.RARE && AbstractDungeon.player.hasRelic(ID)) {
                Color c = sb.getColor();
                sb.setColor(Color.WHITE);
                sb.draw(cross, __instance.hb.x, __instance.hb.y);
                sb.setColor(c);
            }
        }
    }

    @SpirePatch2(clz = RewardItem.class, method = "render")
    public static class RarePotionWarningReward {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(RewardItem __instance, SpriteBatch sb) {
            if(__instance.potion.rarity == AbstractPotion.PotionRarity.RARE && AbstractDungeon.player.hasRelic(ID)) {
                Color c = sb.getColor();
                sb.setColor(Color.WHITE);
                sb.draw(cross, __instance.potion.posX - __instance.potion.hb.width/2f, __instance.potion.posY - __instance.potion.hb.height/2f);
                sb.setColor(c);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPotion.class, "generateSparkles");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
