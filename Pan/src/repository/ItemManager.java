package repository;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import character.Item;
import battle.Ability;

public class ItemManager {


	/*============================== INDEX ==================================*/
	
	
		/*=========================== TYPE ==========================*/
	

	public static final int INDEX_SWORD_SHORT = 0;
	public static final int INDEX_SWORD_LONG = 1;
	public static final int INDEX_AXE = 2;
	public static final int INDEX_MACE = 3;
	public static final int INDEX_FLAIL = 4;
	public static final int INDEX_STAFF = 5;
	public static final int INDEX_POLE = 6;
	//public static final int INDEX_BOW = 7; requires other attack anim, so don't?

	public static final int INDEX_SHIRT = 10;
	public static final int INDEX_ARMOR = 11;
	public static final int INDEX_ROBE = 12;
	
	public static final int INDEX_CAP = 20;
	public static final int INDEX_HELM = 21;
	public static final int INDEX_HEADBAND = 22;
	
	public static final int INDEX_GLOVE = 30;
	public static final int INDEX_SHIELD = 31;
	public static final int INDEX_BRACELET = 32;
	
	public static final int INDEX_BOOT = 40;
	public static final int INDEX_GREAVE = 41;
	public static final int INDEX_SANDAL = 42;

	public static final int INDEX_RING = 50;
	public static final int INDEX_NECK = 51;
	
	public static final int INDEX_MISCELLANEOUS = 60;
	
	
	
		/*============================ ID ===========================*/
	
						/*=============================
							0-499	  = WEAPON
							500-699	  = HEAD
							700-899	  = BODY
							900-1099  = ARMS
							1100-1299 = FEET
							1300-1499 = RING
							1500-1699 = NECK
							2000-2999 = MISCELLANEOUS
						==============================*/
	
	
	public static final int INDEX_TEMP1 = 0;
	public static final int INDEX_TEMP2 = 1;
	public static final int INDEX_TEMP3 = 2;
	public static final int INDEX_TEMP4 = 3;
	public static final int INDEX_TEMP5 = 4;
	public static final int INDEX_TEMP6 = 5;
	public static final int INDEX_TEMP7 = 6;
	public static final int INDEX_TEMP8 = 7;
	public static final int INDEX_TEMP9 = 8;
	public static final int INDEX_TEMP10 = 9;
	public static final int INDEX_TEMP11 = 10;
	public static final int INDEX_TEMP12 = 11;
	public static final int INDEX_TEMP13 = 12;
	public static final int INDEX_TEMP14 = 13;
	
	public static final int INDEX_BEAD_OF_LIFE	= 2000;
	
	
	
	
	
	
	/*================================ METHOD ================================*/

	
	// returns the type index of the item that matches given ID
	public static int getItemType(int ID) {
		switch (ID) {
		
			case INDEX_TEMP1: 			return INDEX_SWORD_LONG;
			case INDEX_TEMP2: 			return INDEX_AXE;
			case INDEX_TEMP3: 			return INDEX_SWORD_SHORT;
			case INDEX_TEMP4: 			return INDEX_MACE;
			case INDEX_TEMP5: 			return INDEX_HELM;
			case INDEX_TEMP6: 			return INDEX_SHIRT;
			case INDEX_TEMP7: 			return INDEX_ARMOR;
			case INDEX_TEMP8: 			return INDEX_GREAVE;
			case INDEX_TEMP9: 			return INDEX_SANDAL;
			case INDEX_TEMP10:	 		return INDEX_BOOT;
			case INDEX_TEMP11:	 		return INDEX_RING;
			case INDEX_TEMP12:		 	return INDEX_NECK;
			case INDEX_TEMP13:			return INDEX_SHIELD;
			case INDEX_TEMP14:			return INDEX_MISCELLANEOUS;
			
			case INDEX_BEAD_OF_LIFE:	return INDEX_MISCELLANEOUS;

			
			default: return -1;
		}
	}
	
	// returns the name of the item that matches given ID  [ MAX 14 CHARACTERS ]
	public static String getItemName(int ID) {
		switch (ID) {
											// ".............."
			case INDEX_TEMP1: 			return "Longsword";
			case INDEX_TEMP2: 			return "Whisperer";
			case INDEX_TEMP3: 			return "Assassin's Blade";
			case INDEX_TEMP4: 			return "Griever";
			case INDEX_TEMP5: 			return "Pretty Helmet";
			case INDEX_TEMP6: 			return "Grand Jacket";
			case INDEX_TEMP7: 			return "Orangutan";
			case INDEX_TEMP8: 			return "Heavy Boots";
			case INDEX_TEMP9: 			return "Quick Boots";
			case INDEX_TEMP10:		 	return "Inbetween Boots";
			case INDEX_TEMP11:		 	return "Fire Ring";
			case INDEX_TEMP12:		 	return "Blue Necklace";
			case INDEX_TEMP13:			return "Iron Shield";
			case INDEX_TEMP14:			return "Apple";
			
			case INDEX_BEAD_OF_LIFE:	return "Bead of Life";

			
			default: return null;
		}
	}
	
	// returns description of the item that matches given ID  [ MAX 25 CHARACTERS ]
	public static String getItemDescription(int ID) {
		switch (ID) {
											// "........................."
			case INDEX_TEMP1: 			return "Longsword";
			case INDEX_TEMP2: 			return "Axe";
			case INDEX_TEMP3: 			return "Shortsword";
			case INDEX_TEMP4: 			return "Mace";
			case INDEX_TEMP5: 			return "Helm";
			case INDEX_TEMP6: 			return "Shirt";
			case INDEX_TEMP7: 			return "Armor";
			case INDEX_TEMP8: 			return "Greaves";
			case INDEX_TEMP9: 			return "Sandals";
			case INDEX_TEMP10:			return "Boots";
			case INDEX_TEMP11:			return "Ring";
			case INDEX_TEMP12:			return "Necklace";
			case INDEX_TEMP13:			return "Shield";
			case INDEX_TEMP14:			return "Restores 35 HP";
			
			case INDEX_BEAD_OF_LIFE:	return "Revives fallen ally";

			default: return null;
		}
	}
	
	// returns whether the item that matches given ID can be used in combat
	// possibly redundant if just check whether getItemCombatAbility == null
	public static boolean getItemCombatUse(int ID) {
		switch (ID) {
			case INDEX_TEMP3:			return true;
			case INDEX_TEMP6: 			return true;
			case INDEX_TEMP14:			return true;
			
			case INDEX_BEAD_OF_LIFE:	return true;
			default: return false;
		}
	}
	
	// returns whether the item that matches given ID can be used in the over-world
	public static boolean getItemAreaUse(int ID) {
		switch (ID) {
			case INDEX_TEMP3:			return true;

			case INDEX_TEMP14:			return true;
			
			case INDEX_BEAD_OF_LIFE:	return true;
			default: return false;
		}
	}
	
	// returns the max durability of the item that matches given ID
	public static int getItemDurability(int ID) {
		if (ID != INDEX_MISCELLANEOUS) return 100;
		else return -1;
	}
	
	// returns the combat ability of the item that matches given ID
	public static Ability getItemCombatAbility(int ID) {
		switch (ID) {
			case INDEX_TEMP6: 			return new Ability(AbilityManager.INDEX_TEMP10);
			case INDEX_TEMP14:			return new Ability(AbilityManager.INDEX_MEND1);
			case INDEX_BEAD_OF_LIFE:	return new Ability(AbilityManager.INDEX_ITEMRESS1);

			default: return null;
		}
	}
	
//	// returns the area ability of the item that matches given ID
//	public static Ability getItemAreaAbility(int ID) {
//		switch (ID) {
//			default: return null;
//		}
//	}

	// returns whether the item that matches given ID is important (can't drop/sell)
	public static boolean getItemImportance(int ID) {
		switch (ID) {
		
			default: return false;
		}
	}
	
//	// returns whether the item that matches given ID is cursed
//	public static boolean getItemCurse(int ID) {
//		switch (ID) {
//			default: return false;
//		}
//	}
	
	// returns the price of the item that matches given ID
	public static int getItemPrice(int ID) {
		switch (ID) {
			case INDEX_TEMP1: 	return 1500;
			
			default: return 100;
		}
	}
	
	// returns the icon of the item that matches given ID
	public static BufferedImage getItemIcon(int ID) {
		switch (ID) {
			case INDEX_TEMP1: return GraphicsManager.sword1Icon;
			case INDEX_TEMP14: return GraphicsManager.appleIcon;
			
			default: return GraphicsManager.sword1Icon;
		}
	}
	
//	// returns the battle sprite of the item that matches given ID
//	public static BufferedImage getItemSprite(int ID) {
//		switch (ID) {
//			
//			default: return null;
//		}
//	}
	
	/* ====== STATS ===== */
	
	// returns the bonus HP of the item that matches given ID
	public static int getItemHP(int ID) {
		switch (ID) {
		
			case INDEX_TEMP1: 			return 0;
			case INDEX_TEMP2: 			return 0;
			case INDEX_TEMP3: 			return 0;
			case INDEX_TEMP4: 			return 0;
			case INDEX_TEMP5: 			return 5;
			case INDEX_TEMP6: 			return 7;
			case INDEX_TEMP7: 			return 3;
			case INDEX_TEMP8: 			return 6;
			case INDEX_TEMP9: 			return 0;
			case INDEX_TEMP10:		 	return 0;
			case INDEX_TEMP11:		 	return 0;
			case INDEX_TEMP12:		 	return 0;
			case INDEX_TEMP13:			return 0;
		
		
			default: return 0;
		}
	}
	
	// returns the bonus MP of the item that matches given ID
	public static int getItemMP(int ID) {
		switch (ID) {
		
		case INDEX_TEMP1: 			return 0;
		case INDEX_TEMP2: 			return 0;
		case INDEX_TEMP3: 			return 0;
		case INDEX_TEMP4: 			return 0;
		case INDEX_TEMP5: 			return 0;
		case INDEX_TEMP6: 			return 11;
		case INDEX_TEMP7: 			return 3;
		case INDEX_TEMP8: 			return 0;
		case INDEX_TEMP9: 			return 0;
		case INDEX_TEMP10:		 	return 6;
		case INDEX_TEMP11:		 	return 0;
		case INDEX_TEMP12:		 	return 0;
		case INDEX_TEMP13:			return 0;
		
			default: return 0;
		}
	}
	
	// returns the bonus armor of the item that matches given ID
	public static int getItemArmorRating(int ID) {
		switch (ID) {
		
		case INDEX_TEMP1: 			return 0;
		case INDEX_TEMP2: 			return 0;
		case INDEX_TEMP3: 			return 0;
		case INDEX_TEMP4: 			return 0;
		case INDEX_TEMP5: 			return 50;
		case INDEX_TEMP6: 			return 71;
		case INDEX_TEMP7: 			return 0;
		case INDEX_TEMP8: 			return 6;
		case INDEX_TEMP9: 			return 15;
		case INDEX_TEMP10:		 	return 23;
		case INDEX_TEMP11:		 	return 0;
		case INDEX_TEMP12:		 	return 0;
		case INDEX_TEMP13:			return 810;
		
			default: return 0;
		}
	}
	
	// returns the bonus attack of the item that matches given ID
	public static int getItemAttackRating(int ID) {
		switch (ID) {
		
		case INDEX_TEMP1: 			return 15;
		case INDEX_TEMP2: 			return 123;
		case INDEX_TEMP3: 			return 166;
		case INDEX_TEMP4: 			return 155;
		case INDEX_TEMP5: 			return 0;
		case INDEX_TEMP6: 			return 0;
		case INDEX_TEMP7: 			return 82;
		case INDEX_TEMP8: 			return 0;
		case INDEX_TEMP9: 			return 0;
		case INDEX_TEMP10:		 	return 0;
		case INDEX_TEMP11:		 	return 0;
		case INDEX_TEMP12:		 	return 0;
		case INDEX_TEMP13:			return 0;
		
			default: return 0;
		}
	}
	
	// returns the bonus speed of the item that matches given ID
	public static int getItemSpeed(int ID) {
		switch (ID) {
		
		
		
			default: return 0;
		}
	}
	
	// returns the bonus fire power of the item that matches given ID
	public static int getItemFirePower(int ID) {
		switch (ID) {
		
		case INDEX_TEMP1: 			return 0;
		case INDEX_TEMP2: 			return 0;
		case INDEX_TEMP3: 			return 0;
		case INDEX_TEMP4: 			return 0;
		case INDEX_TEMP5: 			return 0;
		case INDEX_TEMP6: 			return 0;
		case INDEX_TEMP7: 			return 50;
		case INDEX_TEMP8: 			return 0;
		case INDEX_TEMP9: 			return 0;
		case INDEX_TEMP10:		 	return 0;
		case INDEX_TEMP11:		 	return 48;
		case INDEX_TEMP12:		 	return 0;
		case INDEX_TEMP13:			return 0;
		
			default: return 0;
		}
	}
	
	// returns the bonus fire resistance of the item that matches given ID
	public static int getItemFireResistance(int ID) {
		switch (ID) {
		
		case INDEX_TEMP11: return 30;
		case INDEX_TEMP12: return 5;
			default: return 0;
		}
	}
	
	// returns the bonus water power of the item that matches given ID
	public static int getItemWaterPower(int ID) {
		switch (ID) {
		case INDEX_TEMP12: return 5;

			default: return 0;
		}
	}
	
	// returns the bonus water resistance of the item that matches given ID
	public static int getItemWaterResistance(int ID) {
		switch (ID) {
		case INDEX_TEMP12: return 5;

			default: return 0;
		}
	}
	
	// returns the bonus air power of the item that matches given ID
	public static int getItemAirPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the bonus air resistance of the item that matches given ID
	public static int getItemAirResistance(int ID) {
		switch (ID) {
		case INDEX_TEMP12: return 5;

			default: return 0;
		}
	}
	
	// returns the bonus earth power of the item that matches given ID
	public static int getItemEarthPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the bonus earth resistance of the item that matches given ID
	public static int getItemEarthResistance(int ID) {
		switch (ID) {
		case INDEX_TEMP12: return 5;

			default: return 0;
		}
	}
	
	// returns the bonus arcane power of the item that matches given ID
	public static int getItemArcanePower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the bonus arcane resistance of the item that matches given ID
	public static int getItemArcaneResistance(int ID) {
		switch (ID) {
		case INDEX_TEMP12: return 5;

			default: return 0;
		}
	}
	
	// returns the bonus to immunity of the item that matches given ID
	public static int getItemImmunity(int ID) {
		switch (ID) {
		
			case INDEX_TEMP7: 			return 4;

			default: return 0;
		}
	}
	
	// returns the bonus to HP regeneration of the item that matches given ID
	public static int getItemHPRegen(int ID) {
		switch (ID) {
			case INDEX_TEMP3: 			return 6;

			default: return 0;
		}
	}
	
	// returns the bonus to MP regeneration of the item that matches given ID
	public static int getItemMPRegen(int ID) {
		switch (ID) {
		
			case INDEX_TEMP3: 			return 6;

			default: return 0;
		}
	}
	
	// returns a list of strings for all statistic upgrades of the item
	public static ArrayList<String> getItemBonusStrings(Item it){
		ArrayList<String> bonuses = new ArrayList<String>();
				
		if (it.attackRating != 0) bonuses.add(Integer.toString(it.attackRating));
		if (it.armorRating != 0) bonuses.add(Integer.toString(it.armorRating));
		if (it.HP != 0)	bonuses.add(Integer.toString(it.HP));
		if (it.MP != 0)	bonuses.add(Integer.toString(it.MP));
		if (it.speed != 0) bonuses.add(Integer.toString(it.speed));
		if (it.immunity != 0) bonuses.add(Integer.toString(it.immunity));
		if (it.firePower != 0) bonuses.add(Integer.toString(it.firePower));
		if (it.fireResistance != 0)	bonuses.add(Integer.toString(it.fireResistance));
		if (it.waterPower != 0)	bonuses.add(Integer.toString(it.waterPower));
		if (it.waterResistance != 0) bonuses.add(Integer.toString(it.waterResistance));
		if (it.airPower != 0)	bonuses.add(Integer.toString(it.airPower));
		if (it.airResistance != 0) bonuses.add(Integer.toString(it.airResistance));
		if (it.earthPower != 0)	bonuses.add(Integer.toString(it.earthPower));
		if (it.earthResistance != 0) bonuses.add(Integer.toString(it.earthResistance));
		if (it.arcanePower != 0) bonuses.add(Integer.toString(it.arcanePower));
		if (it.arcaneResistance != 0) bonuses.add(Integer.toString(it.arcaneResistance));
		if (it.HPRegen != 0) bonuses.add(Integer.toString(it.HPRegen));
		if (it.MPRegen != 0) bonuses.add(Integer.toString(it.MPRegen));

		return bonuses;
	}
	
	// returns a list of badge images for all statistic upgrades of the item
	public static ArrayList<BufferedImage> getItemBonusBadges(Item it){
		ArrayList<BufferedImage> badges = new ArrayList<BufferedImage>();
				
		if (it.attackRating != 0) badges.add(GraphicsManager.badgeAttack);
		if (it.armorRating != 0) badges.add(GraphicsManager.badgeArmor);
		if (it.HP != 0)	badges.add(GraphicsManager.typographyCapitalH);
		if (it.MP != 0)	badges.add(GraphicsManager.typographyCapitalM);
		if (it.speed != 0) badges.add(GraphicsManager.badgeSpeed);
		if (it.immunity != 0) badges.add(GraphicsManager.badgeImmunity);
		if (it.firePower != 0) badges.add(GraphicsManager.badgeFirePower);
		if (it.fireResistance != 0)	badges.add(GraphicsManager.badgeFireResistance);
		if (it.waterPower != 0)	badges.add(GraphicsManager.badgeWaterPower);
		if (it.waterResistance != 0) badges.add(GraphicsManager.badgeWaterResistance);
		if (it.airPower != 0)	badges.add(GraphicsManager.badgeAirPower);
		if (it.airResistance != 0) badges.add(GraphicsManager.badgeAirResistance);
		if (it.earthPower != 0)	badges.add(GraphicsManager.badgeEarthPower);
		if (it.earthResistance != 0) badges.add(GraphicsManager.badgeEarthResistance);
		if (it.arcanePower != 0) badges.add(GraphicsManager.badgeArcanePower);
		if (it.arcaneResistance != 0) badges.add(GraphicsManager.badgeArcaneResistance);
		if (it.HPRegen != 0) badges.add(GraphicsManager.badgeRegenLife);
		if (it.MPRegen != 0) badges.add(GraphicsManager.badgeRegenMana);

		return badges;
	}
	
	// returns a list of booleans that represent whether an item is an improvement
	public static ArrayList<Boolean> getItemComparisons(Item it1, Item it2){
		ArrayList<Boolean> comparisons = new ArrayList<Boolean>();

		if (it1.attackRating != 0) {
			if (it1.attackRating > it2.attackRating) comparisons.add(true);
			else if (it1.attackRating < it2.attackRating) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.armorRating != 0) {
			if (it1.armorRating > it2.armorRating) comparisons.add(true);
			else if (it1.armorRating < it2.armorRating) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.HP != 0) {
			if (it1.HP > it2.HP) comparisons.add(true);
			else if (it1.HP < it2.HP) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.MP != 0) {
			if (it1.MP > it2.MP)	comparisons.add(true);
			else if (it1.MP < it2.MP)	comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.speed != 0)	{
			if (it1.speed > it2.speed) comparisons.add(true);
			else if (it1.speed < it2.speed) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.immunity != 0) {
			if (it1.immunity > it2.immunity) comparisons.add(true);
			else if (it1.immunity < it2.immunity) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.firePower != 0) {
			if (it1.firePower > it2.firePower) comparisons.add(true);
			else if (it1.firePower < it2.firePower) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.fireResistance != 0) {
			if (it1.fireResistance > it2.fireResistance) comparisons.add(true);
			else if (it1.fireResistance < it2.fireResistance) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.waterPower != 0) {
			if (it1.waterPower > it2.waterPower) comparisons.add(true);
			else if (it1.waterPower < it2.waterPower) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.waterResistance != 0) {
			if (it1.waterResistance > it2.waterResistance) comparisons.add(true);
			else if (it1.waterResistance < it2.waterResistance) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.airPower != 0) {
			if (it1.airPower > it2.airPower) comparisons.add(true);
			else if (it1.airPower < it2.airPower) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.airResistance != 0)	{
			if (it1.airResistance > it2.airResistance) comparisons.add(true);
			else if (it1.airResistance < it2.airResistance) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.earthPower != 0) {
			if (it1.earthPower > it2.earthPower) comparisons.add(true);
			else if (it1.earthPower < it2.earthPower) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.earthResistance != 0) {
			if (it1.earthResistance > it2.earthResistance) comparisons.add(true);
			else if (it1.earthResistance < it2.earthResistance) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.arcanePower != 0) {
			if (it1.arcanePower > it2.arcanePower) comparisons.add(true);
			else if (it1.arcanePower < it2.arcanePower) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.arcaneResistance != 0){
			if (it1.arcaneResistance > it2.arcaneResistance) comparisons.add(true);
			else if (it1.arcaneResistance < it2.arcaneResistance) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.HPRegen != 0) {
			if (it1.HPRegen > it2.HPRegen) comparisons.add(true);
			else if (it1.HPRegen < it2.HPRegen) comparisons.add(false);
			else comparisons.add(null);
		}
		if (it1.MPRegen != 0) {
			if (it1.MPRegen > it2.MPRegen) comparisons.add(true);
			else if (it1.MPRegen < it2.MPRegen) comparisons.add(false);
			else comparisons.add(null);
		}
		return comparisons;
	}
}
