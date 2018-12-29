package aspiration.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import aspiration.relics.abstracts.AspirationRelic;
import basemod.abstracts.CustomSavable;

public class Headhunter extends AspirationRelic implements CustomSavable<Integer>{
	public static final String ID = "aspiration:Headhunter";
	private int x;

    public Headhunter() {
        super(ID, "Headhunter.png", RelicTier.BOSS, LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + x + DESCRIPTIONS[1] + x + DESCRIPTIONS[2];
    }
    
    @Override
    public Integer onSave() {
    	return counter;
    }
    
    @Override
    public void onLoad(Integer p)
    {
        if (p == null) {
            return;
        }
        this.counter = p;
        
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, "Amount of #yPoison applied: #b" + x));
        this.initializeTips();
    }

    public AbstractRelic makeCopy() {
        return new Headhunter();
    }
}