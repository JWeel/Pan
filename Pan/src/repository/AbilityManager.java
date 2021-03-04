package repository;

import java.awt.image.BufferedImage;

import battle.Condition;
import foundation.Core;

public class AbilityManager {
	
	// the reach of an ability
	public static final int INDEX_REACH_SINGLE_TARGET = 0;
	public static final int INDEX_REACH_DIMINISHING_THREE = 1;
	public static final int INDEX_REACH_DIMINISHING_FIVE = 2;
	public static final int INDEX_REACH_FULL_THREE = 3;
	public static final int INDEX_REACH_FULL_FIVE = 4;
	public static final int INDEX_REACH_RIGHT_TWO = 5;
	public static final int INDEX_REACH_RIGHT_FOUR = 6;
	public static final int INDEX_REACH_LEFT_TWO = 7;
	public static final int INDEX_REACH_LEFT_FOUR = 8;
	
	
	// 	the ID indexes of abilities are ordered as such:
	/*===========================================
	GENERAL
		0		= BASIC DEFEND
		1		= BASIC ATTACK
	
	SPELLS
		2-199 	= SINGLE TARGET DAMAGE
		200-399 = MULTI TARGET DAMAGE
		400-449	= SINGLE TARGET HEAL
		450-499 = SINGLE TARGET RESTORE
		500-549 = MULTI TARGET HEAL
		550-599 = MULTI TARGET RESTORE
		600-649 = SINGLE TARGET SUPPORT
		650-699 = MULTI TARGET SUPPORT
		700-749 = SINGLE TARGET BURDEN
		750-799 = MULTI TARGET BURDEN
	
	MONSTER
		1000-1799 = MONSTER FOE
		1800-1999 = MONSTER FRIEND
		
	ITEM
		2000-2199 = ITEM FRIEND
		2200-2999 = ITEM FOE
	===========================================*/
	//	now follow the indexes
	
	
	public static final int INDEX_DEFEND = 0;
	public static final int INDEX_ATTACK = 1;
	
	public static final int INDEX_TEMP1 = 2;
	public static final int INDEX_TEMP2 = 3;
	public static final int INDEX_TEMP3 = 4;
	public static final int INDEX_TEMP4 = 5;
	public static final int INDEX_TEMP5 = 6;
	public static final int INDEX_TEMP6 = 7;
	public static final int INDEX_TEMP7 = 8;
	public static final int INDEX_TEMP8 = 9;
	public static final int INDEX_TEMP9 = 10;
	public static final int INDEX_TEMP10 = 11;
	public static final int INDEX_TEMP11 = 12;
	public static final int INDEX_TEMP12 = 13;

	
	
	
	public static final int INDEX_MEND1 		= 400;
	
	public static final int INDEX_TREATBURN1 	= 450;

	public static final int INDEX_DAMNATION1	= 750;
	
	public static final int INDEX_ITEMRESS1 	= 2100;


	
	
	// returns the name of the ability that matches given ID [ MAX 14 CHARACTERS ]
	public static String getAbilityName(int ID) {
		switch (ID) {
										// ".............."
			case INDEX_TEMP1: 		return "Fireball";
			case INDEX_TEMP2: 		return "Raindrop";
			case INDEX_TEMP3: 		return "Ice Blast";
			case INDEX_TEMP4: 		return "Arcane Spheres";
			case INDEX_TEMP5: 		return "Devastation";
			case INDEX_TEMP6: 		return "Supernova";
			case INDEX_TEMP7: 		return "X-Fire";
			case INDEX_TEMP8: 		return "Energy";
			case INDEX_TEMP9: 		return "Pure Energy";
			case INDEX_TEMP10: 		return "Arcane Whip";
			case INDEX_TEMP11: 		return "Black Ice";
			case INDEX_TEMP12: 		return "Boulder Dash";
						
			case INDEX_MEND1:		return "Mend";
			case INDEX_TREATBURN1: 	return "Treat Burn";
			
			case INDEX_DAMNATION1:	return "Damnation";
			
			case INDEX_ITEMRESS1:	return "Bead of Life";
			
			// NOTE: for Item abilities, make sure name is same as item!
			
			default: return null;
		}
	}
	
	// returns description of ability that matches given ID [ MAX 25 CHARACTERS ]
	public static String getAbilityDescription(int ID) {
		switch (ID) {
										// "........................."
			case INDEX_TEMP1: 		return "Hurl a hot ball of fire.";
			case INDEX_TEMP2: 		return "Drop liquid with force.";
			case INDEX_TEMP3: 		return "Explode ice from within.";
			case INDEX_TEMP4: 		return "Quell with arcane disks.";
			case INDEX_TEMP5: 		return "Release incredible force.";
			case INDEX_TEMP6: 		return "Punish with power of sun.";
			case INDEX_TEMP7: 		return "Strike limbs with fire.";
			case INDEX_TEMP8: 		return "Unleash bound energy.";
			case INDEX_TEMP9: 		return "Unleash hidden energy.";
			case INDEX_TEMP10: 		return "Strike with arcane whip.";
			case INDEX_TEMP11: 		return "Freeze with cold horror.";
			case INDEX_TEMP12: 		return "Flatten with giant rock.";	
			
			case INDEX_MEND1:		return "Restore 50H.";
			case INDEX_TREATBURN1: 	return "Remove burn wound.";
			
			case INDEX_DAMNATION1:	return "Call forward the reaper.";
			
			default: return null;
		}
	}
	
	// returns the reach of the ability that matches given ID
	public static int getAbilityReach(int ID) {
		
		//if ((ID >= 0 && ID < 200)) return INDEX_REACH_SINGLE_TARGET;
		// possibly with INDEX_ATTACK check for item? some increase reach? splash?
		
		switch (ID) {
			
			case INDEX_TEMP1: 		return INDEX_REACH_SINGLE_TARGET;
			case INDEX_TEMP2: 		return INDEX_REACH_DIMINISHING_THREE;
			case INDEX_TEMP3: 		return INDEX_REACH_DIMINISHING_FIVE;
			case INDEX_TEMP4: 		return INDEX_REACH_FULL_THREE;
			case INDEX_TEMP5: 		return INDEX_REACH_FULL_FIVE;
			case INDEX_TEMP6: 		return INDEX_REACH_RIGHT_TWO;
			case INDEX_TEMP7: 		return INDEX_REACH_DIMINISHING_FIVE;
			case INDEX_TEMP8: 		return INDEX_REACH_RIGHT_FOUR;
			case INDEX_TEMP9: 		return INDEX_REACH_LEFT_TWO;
			case INDEX_TEMP10: 		return INDEX_REACH_LEFT_FOUR;
			case INDEX_TEMP11: 		return INDEX_REACH_FULL_FIVE;
			case INDEX_TEMP12: 		return INDEX_REACH_DIMINISHING_FIVE;
		
			case INDEX_MEND1:		return INDEX_REACH_SINGLE_TARGET;
			case INDEX_TREATBURN1: 	return INDEX_REACH_SINGLE_TARGET;
			
			case INDEX_DAMNATION1:	return INDEX_REACH_FULL_THREE;
		
			default: return INDEX_REACH_SINGLE_TARGET;
		}
	}
	
	// returns the element index of the ability that matches given ID
	public static int getAbilityElementIndex(int ID) {
		switch (ID) {	
			case INDEX_TEMP1: 		return Core.INDEX_FIRE;
			case INDEX_TEMP2: 		return Core.INDEX_WATER;
			case INDEX_TEMP3: 		return Core.INDEX_WATER;
			case INDEX_TEMP4: 		return Core.INDEX_ARCANE;
			case INDEX_TEMP5: 		return Core.INDEX_EARTH;
			case INDEX_TEMP6: 		return Core.INDEX_FIRE;
			case INDEX_TEMP7: 		return Core.INDEX_FIRE;
			case INDEX_TEMP8: 		return Core.INDEX_ARCANE;
			case INDEX_TEMP9: 		return Core.INDEX_ARCANE;
			case INDEX_TEMP10: 		return Core.INDEX_ARCANE;
			case INDEX_TEMP11: 		return Core.INDEX_WATER;
			case INDEX_TEMP12: 		return Core.INDEX_EARTH;
			
			case INDEX_MEND1:		return Core.INDEX_AIR;
			case INDEX_TREATBURN1: 	return Core.INDEX_WATER;
			
			case INDEX_DAMNATION1:	return Core.INDEX_ARCANE;
			
			// an ability without element (basic attack) returns -1
			default: return -1;
		}
	}
	
	// returns the base HP damage of the ability that matches given ID
	public static int getAbilityBaseHPDamage(int ID) {
		switch (ID) {
			case INDEX_DEFEND: return 0;
			
			case INDEX_MEND1:		return 0;
			case INDEX_TREATBURN1: 	return 0;
			
			case INDEX_DAMNATION1:	return 0;
			
			case INDEX_ITEMRESS1:	return 0;

			default: return 10;
		}
	}
	
	// returns the base MP damage of the ability that matches given ID
	public static int getAbilityBaseMPDamage(int ID) {
		switch (ID) {
			case INDEX_DEFEND: return 0;
			default: return 0;
		}
	}
	
	// returns the percentage HP drained of the ability that matches given ID
	public static double getAbilityHPDrain(int ID){
		switch (ID) {
			case INDEX_TEMP5: return 0.5;
			default: return 0.0;
		}
	}
	
	// returns the percentage MP drained of the ability that matches given ID
	public static double getAbilityMPDrain(int ID){
		switch (ID) {
			default: return 0.0;
		}
	}
	
	// returns the mana cost of the ability that matches given ID
	public static int getAbilityMPCost(int ID) {
		
		switch (ID) {
			case INDEX_DEFEND: return 0;
			case INDEX_ATTACK: return 0;
			case INDEX_TEMP4: return 27;
			
			case INDEX_MEND1:		return 5;
			case INDEX_TREATBURN1: 	return 4;
			
			//case INDEX_DAMNATION1:	return 10;
			
			case INDEX_ITEMRESS1: return 0;
			
			default: return 3;
		}
	}
	
	// returns the health cost of the ability that matches given ID
	public static int getAbilityHPCost(int ID) {
		switch (ID) {
			
			case INDEX_DAMNATION1: return 20;
		
			default: return 0;
		}
	}
	
	// returns whether the target of the ability that matches given ID is a foe
	public static boolean getAbilityTargetIsFoe(int ID) {
		//if (ID < 4) return true;
		//else return false;
		if (ID >= 400 && ID < 700) return false;
		else if (ID >= 1800 && ID < 2200) return false;
		else return true;
	}
	
	// returns whether the target of the ability that matches given ID is dead
	public static boolean getAbilityTargetIsDead(int ID) {
		switch (ID) {
			case INDEX_ITEMRESS1: return true;
			default: return false;
		}
	}
	
	// returns the icon that matches the ability. only needed for spells
	public static BufferedImage getAbilityIcon(int ID){
		switch (ID){
			case INDEX_TEMP1: return GraphicsManager.flame1Icon;
			case INDEX_MEND1: return GraphicsManager.mend1Icon;
			case INDEX_TREATBURN1: return GraphicsManager.treatBurn1Icon;

			default: return null;
		}
	}

	// returns the base HP heal of the ability that matches given ID
	public static int getAbilityBaseHPRestore(int ID) {
		switch (ID) {
		
			case INDEX_MEND1: return 50;

		
			case INDEX_ITEMRESS1: return 50;
			default: return 0;
		}
	}
	
	// returns the base MP heal of the ability that matches given ID
	public static int getAbilityBaseMPRestore(int ID) {
		switch (ID) {
		
			case INDEX_MEND1: return 10;

		
			default: return 0;
		}
	}

	// returns the status effects the ability afflicts
	public static int getAbilityStatusIndexAfflict(int ID) {
		switch (ID) {
		
			case INDEX_TEMP1: return Condition.INDEX_BURN;
			case INDEX_DAMNATION1: return Condition.INDEX_REAP;
			default: return -1;
		}
	}
	
	// returns the status effects the ability afflicts
	public static int getAbilityStatusAfflictDuration(int ID) {
		switch (ID) {
		
			case INDEX_TEMP1: return 1;
			
			case INDEX_DAMNATION1: return 5;

			default: return 0;
		}
	}
	
	// returns the status effects the ability afflicts
	public static double getAbilityStatusAfflictChance(int ID) {
		switch (ID) {
		
			case INDEX_TEMP1: return 1.0;

			case INDEX_DAMNATION1: return 0.5;
		
			default: return 0.0;
		}
	}

	// returns the status effects the ability remove
	public static int getAbilityStatusIndexRemove(int ID) {
		switch (ID) {
		
			case INDEX_TREATBURN1: return Condition.INDEX_BURN;

			default: return -1;
		}
	}
	
	// returns the bonus to health given by ability
	public static int getBonusMaxHP(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to MANA given by ability
	public static int getBonusMaxMP(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to armor given by ability
	public static int getBonusArmorRating(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to attack given by ability
	public static int getBonusAttackRating(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to fire power given by ability
	public static int getBonusFirePower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to fire resistance given by ability
	public static int getBonusFireResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to water power given by ability
	public static int getBonusWaterPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to water resistance given by ability
	public static int getBonusWaterResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to air power given by ability
	public static int getBonusAirPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to air resistance given by ability
	public static int getBonusAirResistanc(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to earth power given by ability
	public static int getBonusEarthPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to earth resistance given by ability
	public static int getBonusEarthResistanceP(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to arcane power given by ability
	public static int getBonusArcanePower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the bonus to arcane resistance given by ability
	public static int getBonusArcaneResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
}
