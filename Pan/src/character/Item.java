package character;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import repository.ItemManager;
import battle.Ability;

public class Item{

	public int ID, typeIndex, price, durability, quantity;
	public String name, description;
	
	public boolean usableInCombat, usableInArea, isImportant, isEquipped, isCursed;
	public BufferedImage icon; //, sprite;

	public Ability combatAbility;
	//public AreaAbility areaAbility;
	
	// bonuses to statistics
	public int HP, MP, armorRating, attackRating, speed, 
		firePower, fireResistance, waterPower, waterResistance,	airPower, 
		airResistance, earthPower, earthResistance, arcanePower, arcaneResistance,
		immunity, HPRegen, MPRegen;
	// list of strings and badges for the statistic bonuses
	public ArrayList<String> bonusStrings;
	public ArrayList<BufferedImage> bonusBadges;
	public ArrayList<Boolean> bonusComparisons;
	
	public Item(int newID){
		this.ID = newID;
		this.typeIndex = ItemManager.getItemType(ID);
		this.name = ItemManager.getItemName(ID);
		this.description = ItemManager.getItemDescription(ID);
		this.usableInCombat = ItemManager.getItemCombatUse(ID);
		this.usableInArea = ItemManager.getItemAreaUse(ID);
		this.durability = ItemManager.getItemDurability(ID);
		this.combatAbility = ItemManager.getItemCombatAbility(ID);
		this.isImportant = ItemManager.getItemImportance(ID);
		this.price = ItemManager.getItemPrice(ID);
		//this.isCursed = ItemManager.getItemCurse(ID);
		this.icon = ItemManager.getItemIcon(ID);
		//this.sprite;
		
		this.quantity = 1;
		
		this.HP = ItemManager.getItemHP(ID);
		this.MP = ItemManager.getItemMP(ID);
		this.armorRating = ItemManager.getItemArmorRating(ID);
		this.attackRating = ItemManager.getItemAttackRating(ID);
		this.speed = ItemManager.getItemSpeed(ID);
		this.firePower = ItemManager.getItemFirePower(ID);
		this.fireResistance = ItemManager.getItemFireResistance(ID);
		this.waterPower = ItemManager.getItemWaterPower(ID);
		this.waterResistance = ItemManager.getItemWaterResistance(ID);
		this.airPower  = ItemManager.getItemAirPower(ID);
		this.airResistance = ItemManager.getItemAirResistance(ID);
		this.earthPower = ItemManager.getItemEarthPower(ID);
		this.earthResistance = ItemManager.getItemEarthResistance(ID);
		this.arcanePower = ItemManager.getItemArcanePower(ID);
		this.arcaneResistance = ItemManager.getItemArcaneResistance(ID);
		this.immunity = ItemManager.getItemImmunity(ID);
		this.HPRegen = ItemManager.getItemHPRegen(ID);
		this.MPRegen = ItemManager.getItemMPRegen(ID);
		
		this.bonusStrings = ItemManager.getItemBonusStrings(this);
		this.bonusBadges = ItemManager.getItemBonusBadges(this);
	}
	
	// returns whether item is a miscellaneous item (quantity decreases on use)
	public boolean isMisc(){
		return typeIndex == ItemManager.INDEX_MISCELLANEOUS;
	}
}