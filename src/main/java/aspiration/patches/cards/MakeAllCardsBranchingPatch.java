package aspiration.patches.cards;

import aspiration.cards.interfaces.BranchingUpgradesCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import javassist.*;

public class MakeAllCardsBranchingPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, String.class, int.class, String.class, AbstractCard.CardType.class, AbstractCard.CardColor.class, AbstractCard.CardRarity.class, AbstractCard.CardTarget.class, DamageInfo.DamageType.class}
    )
    public static class InterfaceAndBasicMethod {
        public static void Raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool pool = ctClass.getClassPool();

            CtClass ctCloneablePowerInterface = pool.get(BranchingUpgradesCard.class.getName());
            ctClass.addInterface(ctCloneablePowerInterface);

            CtMethod method = CtNewMethod.make(
                    CtClass.voidType, // Return
                    "branchUpgrade", // Method name
                    new CtClass[]{},
                    null, // Exceptions
                    "if (!upgraded) {" +
                            "            upgradeName();" +
                            "            upgradeDamage(2);" +
                            "            upgradeBlock(2);" +
                            "            upgradeMagicNumber(2);" +
                            "        }",
                    ctClass
            );
            ctClass.addMethod(method);
        }
    }
}
