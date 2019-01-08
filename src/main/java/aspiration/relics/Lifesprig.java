package aspiration.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;

public class Lifesprig extends AspirationRelic {
public static final String ID = "aspiration:Lifesprig";
	
	private static final int START_CHARGE = 0;
	private static final int ATTACKS_TO_TRIGGER = 6;
	private static final int REGEN_AMT = 1;
	
    public Lifesprig() {
        super(ID, "Lifesprig.png", RelicTier.COMMON, LandingSound.FLAT);
    }
    
    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
    		if(c.type == CardType.ATTACK) {
    			flash();
    			manipCharge(1);
    		}
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ATTACKS_TO_TRIGGER + DESCRIPTIONS[1] + REGEN_AMT + DESCRIPTIONS[2];
    }
    
    @Override
	public void onEquip() {
        startingCharges();
    }
    
    private void startingCharges()
    {
        setCounter(START_CHARGE);
    }
    
    private void manipCharge(int amt) {
		if (counter < 0) {
			counter = 0;
		}
		setCounter(counter + amt);

		if (counter >= ATTACKS_TO_TRIGGER) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, REGEN_AMT), REGEN_AMT));
			counter -= ATTACKS_TO_TRIGGER;
		}
    }
    
    public AbstractRelic makeCopy() {
        return new Lifesprig();
    }
}