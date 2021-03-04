package battle;

import java.awt.image.BufferedImage;

import repository.AbilityManager;

public class Ability {

	public int ID;
	public String name, description;
	
	// if the target is -1 then the target has not yet been set
	public int target = -1;
	
	// reach is how many targets can be hit with this ability.
	public int reach;
	
	public int elementIndex, HPDamage, MPDamage, MPCost, HPCost, 
		HPRestore, MPRestore, statusAfflictDuration;
	
	public double statusAfflictChance;
	
	public int statusIndexAfflict, statusIndexRestore;
	
	// bonuses to statistics during battle
	public int bonusMaxHP, bonusMaxMP, bonusArmorRating, bonusAttackRating, 
	bonusFirePower, bonusFireResistance, bonusWaterPower, bonusWaterResistance, 
	bonusAirPower, bonusAirResistance, bonusEarthPower, bonusEarthResistance, 
	bonusArcanePower, bonusArcaneResistance;
	
	public double HPDrain, MPDrain;
	public boolean targetIsFoe;
	public boolean targetIsDead;
	public BufferedImage icon;
	// public ArrayList<BufferedImage> sprites;

	public Ability(int abilityID){
		this.ID = abilityID;
		this.name = AbilityManager.getAbilityName(ID);
		this.description = AbilityManager.getAbilityDescription(ID);
		this.icon = AbilityManager.getAbilityIcon(ID);

		this.reach = AbilityManager.getAbilityReach(ID);
		this.elementIndex = AbilityManager.getAbilityElementIndex(ID);
		this.HPDamage = AbilityManager.getAbilityBaseHPDamage(ID);
		this.MPDamage = AbilityManager.getAbilityBaseMPDamage(ID);
		this.MPCost = AbilityManager.getAbilityMPCost(ID);
		this.HPCost = AbilityManager.getAbilityHPCost(ID);
		this.HPDrain = AbilityManager.getAbilityHPDrain(ID);
		this.MPDrain = AbilityManager.getAbilityMPDrain(ID);
		this.HPRestore = AbilityManager.getAbilityBaseHPRestore(ID);
		this.MPRestore = AbilityManager.getAbilityBaseMPRestore(ID);
		this.targetIsFoe = AbilityManager.getAbilityTargetIsFoe(ID);
		this.targetIsDead = AbilityManager.getAbilityTargetIsDead(ID);
		this.statusIndexAfflict = AbilityManager.getAbilityStatusIndexAfflict(ID);
		this.statusIndexRestore = AbilityManager.getAbilityStatusIndexRemove(ID);
		this.statusAfflictDuration = AbilityManager.getAbilityStatusAfflictDuration(ID);
		this.statusAfflictChance = AbilityManager.getAbilityStatusAfflictChance(ID);
		
		// buff and debuff effects
		this.bonusMaxHP = AbilityManager.getBonusMaxHP(ID);
		this.bonusMaxMP = AbilityManager.getBonusMaxMP(ID);
		this.bonusArmorRating = AbilityManager.getBonusArmorRating(ID);
		this.bonusAttackRating = AbilityManager.getBonusAttackRating(ID);
		this.bonusFirePower = AbilityManager.getBonusFirePower(ID);
		this.bonusFireResistance = AbilityManager.getBonusFireResistance(ID);
		this.bonusWaterPower = AbilityManager.getBonusWaterPower(ID);
		this.bonusWaterResistance = AbilityManager.getBonusWaterResistance(ID);
		this.bonusAirPower = AbilityManager.getBonusAirPower(ID);
		this.bonusAirResistance = AbilityManager.getBonusAirResistanc(ID);
		this.bonusEarthPower = AbilityManager.getBonusEarthPower(ID);
		this.bonusEarthResistance = AbilityManager.getBonusEarthResistanceP(ID);
		this.bonusArcanePower = AbilityManager.getBonusArcanePower(ID);
		this.bonusArcaneResistance = AbilityManager.getBonusArcaneResistance(ID);
	}
	
	// returns whether this ability is DEFEND: an oft used condition
	public boolean isDefend(){
		return ID == AbilityManager.INDEX_DEFEND;
	}
	
	// returns whether this ability is DEFEND: an oft used condition
	public boolean isAttack(){
		return ID == AbilityManager.INDEX_ATTACK;
	}
}
