package aspiration.patches.Unique;

public class HubrisSlothHatOfInfinitePowerPatch {
    /*@SpirePatch(
            cls = "com.evacipated.cardcrawl.mod.hubris.cards.curses.Sloth",
            method = "use",
            optional = true
    )
    public static class SlothUseListener {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(Sloth __instance, AbstractPlayer p, AbstractMonster m) {
            if(AbstractDungeon.player.hasRelic(HatOfInfinitePower.ID) && !((HatOfInfinitePower)AbstractDungeon.player.getRelic(HatOfInfinitePower.ID)).isTriggered()) {
                AbstractDungeon.player.energy.energy--;
                ((HatOfInfinitePower)AbstractDungeon.player.getRelic(HatOfInfinitePower.ID)).trigger();
                if (!p.hasPower(__instance.cardID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SlothPower(p)));
                }
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }*/
}
