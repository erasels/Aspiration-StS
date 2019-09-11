package aspiration.patches;

import aspiration.patches.Fields.WingBootsCampfireactionField;
import aspiration.ui.campfire.EmpowerWingBootsOption;
import aspiration.ui.campfire.ReadArtOfWarOption;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ArtOfWar;
import com.megacrit.cardcrawl.relics.WingBoots;
import javassist.*;

import java.util.ArrayList;

public class CampfireOptionRelicPatches {
    @SpirePatch(
            clz = WingBoots.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class WingbootsCampfireOption {
        public static void Raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool pool = ctClass.getClassPool();
            CtClass ctArrList = pool.get(ArrayList.class.getName());

            CtMethod method = CtNewMethod.make(
                    CtClass.voidType, // Return
                    "addCampfireOption", // Method name
                    new CtClass[]{ctArrList}, //Paramters
                    null, // Exceptions
                    "{" +
                            "if(!" + CampfireOptionRelicPatches.class.getName() + ".WbOptUsed())" +
                            "$1.add(" + CampfireOptionRelicPatches.class.getName() + ".getWOpt());" +
                            "}",
                    ctClass
            );
            ctClass.addMethod(method);
        }
    }
    public static EmpowerWingBootsOption getWOpt() {return new EmpowerWingBootsOption();}
    public static boolean WbOptUsed() {
        WingBoots wb = (WingBoots) AbstractDungeon.player.getRelic(WingBoots.ID);
        if(wb != null) {
            return WingBootsCampfireactionField.campUsed.get(wb);
        }
        return false;
    }

    @SpirePatch(
            clz = ArtOfWar.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class AoWCampfireOption {
        public static void Raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool pool = ctClass.getClassPool();
            CtClass ctArrList = pool.get(ArrayList.class.getName());

            CtMethod method = CtNewMethod.make(
                    CtClass.voidType, // Return
                    "addCampfireOption", // Method name
                    new CtClass[]{ctArrList}, //Paramters
                    null, // Exceptions
                    "{" +
                            "if(" + CampfireOptionRelicPatches.class.getName() + ".checkAoWOpt())" +
                            "$1.add(" + CampfireOptionRelicPatches.class.getName() + ".getAOpt());" +
                            "}",
                    ctClass
            );
            ctClass.addMethod(method);
        }
    }
    public static ReadArtOfWarOption getAOpt() {return new ReadArtOfWarOption();}
    public static boolean checkAoWOpt() {
        return AbstractDungeon.player.hasRelic(ArtOfWar.ID);
    }
}
