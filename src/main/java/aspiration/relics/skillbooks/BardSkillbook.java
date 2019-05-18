package aspiration.relics.skillbooks;

import aspiration.Aspiration;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

import static aspiration.Aspiration.logger;

public class BardSkillbook extends SkillbookRelic {
    public static final String ID = "aspiration:BardSkillbook";

    public BardSkillbook() {
        super(ID, "BardSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
        /*ArrayList<PowerTip> tmp = new ArrayList<>(tips);
        tips.clear();
        tips.addAll(tmp);
        tips.add(new PowerTip(DESCRIPTIONS[2], DESCRIPTIONS[3]));
        initializeTips();
        tmp.clear();*/
    }

    @Override
    public String getUpdatedDescription() {
        if (Aspiration.skillbookCardpool()) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(card);
        for (AbstractNote note : notes) {
            AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(note));
        }
    }

    @Override
    public void onPlayerEndTurn() {
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(WildCardNote.get()));
    }

    @Override
    public void onEquip() {
        modifyCardPool();
        NoteQueue nQ = BardMod.getNoteQueue(AbstractDungeon.player);
        if(nQ != null) {
            nQ.setMasterMaxNotes(Bard.MAX_NOTES);
            nQ.increaseMaxNotes(Bard.MAX_NOTES);
        }
    }

    public void modifyCardPool() {
        if (Aspiration.skillbookCardpool()) {
            logger.info("Bard Skillbook acquired, modifying card pool.");
            ArrayList<AbstractCard> classCards = CardLibrary.getCardList(Bard.Enums.LIBRARY_COLOR);
            mixCardpools(classCards);
        }
    }

    @Override
    public boolean canSpawn() {
        return !(AbstractDungeon.player instanceof Bard) && !hasSkillbookRelic(AbstractDungeon.player);
    }

    public AbstractRelic makeCopy() {
        return new BardSkillbook();
    }
}