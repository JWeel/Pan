package battle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import repository.GraphicsManager;

public class Condition {

	public static final int INDEX_STUN = 0;		// cannot move
	public static final int INDEX_CURSE = 1;	// 15% damage after move, persistent
	public static final int INDEX_POISON = 2;	// 20% damage after turn, persistent
	//public static final int INDEX_TOXIC = 3;	// ?% damage after turn, persistent
	public static final int INDEX_BURN = 4;		// 10% damage after struck, persistent
	public static final int INDEX_REAP = 5;		// death after 5 turns
	public static final int INDEX_DISARM = 6;	// cannot attack
	public static final int INDEX_LOCKSPELL = 7;// cannot cast
	public static final int INDEX_LOCKITEM = 8;	// cannot use items
	//public static final int INDEX_FREEZE = 9;	// cannot move until hit (double DMG)
	//public static final int INDEX_CONFUSION = 10; // random attack
	//public static final int INDEX_MANALEAK = 11; // 10% mana lost after turn
	//public static final int INDEX_MANASTRAIN = 12; // spells cost 3x mana
	
	public static final int INDEX_BONUS_HP = 100;		
	public static final int INDEX_BONUS_MP = 101;	
	public static final int INDEX_BONUS_ARMOR = 102;
	public static final int INDEX_BONUS_ATTACK = 103;
	public static final int INDEX_BONUS_SPEED = 104;
	public static final int INDEX_BONUS_FIREP = 105;
	public static final int INDEX_BONUS_FIRER = 106;
	public static final int INDEX_BONUS_WATERP = 107;
	public static final int INDEX_BONUS_WATERR = 108;
	public static final int INDEX_BONUS_AIRP = 109;
	public static final int INDEX_BONUS_AIRR = 110;	
	public static final int INDEX_BONUS_EARTHP = 111;
	public static final int INDEX_BONUS_EARTHR = 112;
	public static final int INDEX_BONUS_ARCANEP = 113;
	public static final int INDEX_BONUS_ARCANER = 114;
	public static final int INDEX_BONUS_IMMUNITY = 115;	
	public static final int INDEX_BONUS_REGEN_HP = 116;
	public static final int INDEX_BONUS_REGEN_MP = 117;

	public int ID;
	public int duration;
	
	public Condition(int newID, int newDuration){
		this.ID = newID;
		this.duration = newDuration;
	}
	
	public static boolean containsCondition(ArrayList<Condition> conditions, int typeID){
		for (Condition c : conditions) if (c.ID == typeID) return true;
		return false;
	}
	
	public static Condition getCondition(ArrayList<Condition> conditions, int typeID){
		for (Condition c : conditions) if (c.ID == typeID) return c;
		return null;
	}
	
	// returns the name of the condition
	public static String getName(int index){
		switch (index){
			case INDEX_STUN:		return "stunned";
			case INDEX_CURSE:		return "cursed";
			case INDEX_POISON:		return "poisoned";
			case INDEX_BURN:		return "burned";
			case INDEX_REAP:		return "being reaped";
			case INDEX_DISARM:		return "disarmed";
			case INDEX_LOCKSPELL:	return "spell-locked";
			case INDEX_LOCKITEM:	return "item-locked";
			
			case INDEX_BONUS_HP:		return "max health";
			case INDEX_BONUS_MP:		return "max mana";
			case INDEX_BONUS_ARMOR:		return "armor rating";
			case INDEX_BONUS_ATTACK:	return "attack rating";
			case INDEX_BONUS_SPEED:		return "speed";
			case INDEX_BONUS_FIREP:		return "fire power";
			case INDEX_BONUS_FIRER:		return "fire resistance";
			case INDEX_BONUS_WATERP:	return "water power";
			case INDEX_BONUS_WATERR:	return "water resistance";
			case INDEX_BONUS_AIRP:		return "air power";
			case INDEX_BONUS_AIRR:		return "air resistance";
			case INDEX_BONUS_EARTHP:	return "earth power";
			case INDEX_BONUS_EARTHR:	return "earh resistance";
			case INDEX_BONUS_ARCANEP:	return "arcane power";
			case INDEX_BONUS_ARCANER:	return "arcane resistance";
			case INDEX_BONUS_IMMUNITY:	return "immunity";
			case INDEX_BONUS_REGEN_HP:	return "health regeneration";
			case INDEX_BONUS_REGEN_MP:	return "mana regeneration";
			
			default: return null;
		}
	}
	
	// returns whether condition is a statistic modifier
	public static boolean isStatModifier(int index){
		return index >= 100;
	}
	
	public boolean isPersistent(){
		if (this.ID >= INDEX_CURSE && this.ID <= INDEX_BURN) return true;
		else return false;
	}
	
	// returns the icon (or badge) of this condition
	public BufferedImage getIcon(){
		switch (ID){
			case INDEX_STUN:		return GraphicsManager.conditionStun;
			case INDEX_CURSE:		return GraphicsManager.conditionCurse;
			case INDEX_POISON:		return GraphicsManager.conditionPoison;
			case INDEX_BURN:		return GraphicsManager.conditionBurn;
			case INDEX_REAP:		return GraphicsManager.conditionReap;
			case INDEX_DISARM:		return GraphicsManager.conditionDisarm;
			case INDEX_LOCKSPELL:	return GraphicsManager.conditionLockSpell;
			case INDEX_LOCKITEM:	return GraphicsManager.conditionLockItem;
			default: return null;
		}
	}
}
