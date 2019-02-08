package aspiration.relics;

import aspiration.relics.abstracts.AspirationRelic;
import aspiration.ui.screens.RelicSelectScreen;
import aspiration.vfx.ObtainRelicLater;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.lwjgl.Sys;

import java.util.ArrayList;

public class SeaSaltIceCream extends AspirationRelic {
    public static final String ID = "aspiration:SeaSaltIceCream";
    private boolean relicSelected = true;
    private RelicSelectScreen relicSelectScreen;
    private boolean fakeHover = false;

    private ArrayList<RefPoint> oldCoords = new ArrayList<>();

    public SeaSaltIceCream() {
        super(ID, "SeaSaltIceCream.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    //TODO add Tinflute like stuff and remove method of triggering
    @Override
    public void onEquip()
    {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        openRelicSelect();
    }

    private void openRelicSelect()
    {
        relicSelected = false;

        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(AbstractDungeon.player.relics);

        relics.removeIf(r -> r.relicId.equals(this.relicId));

        for(AbstractRelic r : AbstractDungeon.player.relics) {
            if(!r.relicId.equals(this.relicId)) {
                oldCoords.add(new RefPoint(r.name, r.hb.cX, r.hb.cY, r.currentX, r.currentY));
                //System.out.println("Relic:" + r.name + " cX:"+r.currentX+" cY:"+r.currentY);
            }
        }

        relicSelectScreen = new RelicSelectScreen();
        relicSelectScreen.open(relics);
    }

    @Override
    public void update()
    {
        super.update();

        if (!relicSelected) {
            if (relicSelectScreen.doneSelecting()) {
                relicSelected = true;

                AbstractRelic relic = relicSelectScreen.getSelectedRelics().get(0).makeCopy();

                //TODO add this to postinitialize for removing the relic from the next run
                /*
                switch (relic.tier) {
                    case COMMON:
                        AbstractDungeon.commonRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case RARE:
                        AbstractDungeon.rareRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case SHOP:
                        AbstractDungeon.shopRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case BOSS:
                        AbstractDungeon.bossRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                }*/
                AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(relic));

                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if(!r.relicId.equals(this.relicId)) {
                        for(RefPoint rp : oldCoords) {
                            if(r.name.equals(rp.getN())) {
                                r.hb.update(rp.getX(), rp.getY());
                                r.currentX = rp.getrX();
                                r.currentY = rp.getrY();
                                //System.out.println("RP:"+rp.getN()+" x:"+rp.getrX()+" y:"+rp.getrY());
                                r.update();
                                break;
                            }
                        }
                    }
                }

                /*for(AbstractRelic r : AbstractDungeon.player.relics) {
                    System.out.println("FinRelic:" + r.name + " cX:"+r.currentX+" cY:"+r.currentY);
                }*/

                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            } else {
                relicSelectScreen.update();
                if (!hb.hovered) {
                    fakeHover = true;
                }
                hb.hovered = true;
            }
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        if (!relicSelected && fakeHover) {
            relicSelectScreen.render(sb);
        }
        if (fakeHover) {
            fakeHover = false;
            hb.hovered = false;
        } else {
            super.renderTip(sb);
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb)
    {
        super.renderInTopPanel(sb);

        if (!relicSelected && !fakeHover) {
            relicSelectScreen.render(sb);
        }
    }

    public AbstractRelic makeCopy() {
        return new SeaSaltIceCream();
    }

    private class RefPoint {
        private float px;
        private float py;
        private float prx;
        private float pry;
        private String pn;
        public RefPoint(String n, float x, float y, float rx, float ry) {
            pn = n;
            px = x;
            py = y;
            prx = rx;
            pry = ry;
        }
        public float getY() {
            return py;
        }
        public float getX() {
            return px;
        }
        public float getrY() {
            return pry;
        }
        public float getrX() {
            return prx;
        }
        public String getN() {
            return pn;
        }
    }
}