package repository;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

// store pictures and names of enemies ?
public class MonsterManager {

	
	// index of all monsters (used as IDs)
	public static final int ID_MINION1 = 0;
	public static final int ID_STEM1 = 1;
	public static final int ID_MOLLUSCA1 = 2;

	
	// returns the name of the monster
	public static String getName(int ID){
		String name ="";
		
		switch (ID){
			case ID_MINION1:
				name = "Minion"; break;
			case ID_STEM1:
				name = "Stem"; break;
			case ID_MOLLUSCA1:
				name = "Mollusca"; break;
		}
		
		return name;
	}
	
	// returns the standing sprites of the monster
	public static ArrayList<BufferedImage> getStandSprites(int ID){
		ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
		switch (ID){
			case ID_MINION1:
				sprites.add(GraphicsManager.minion1Stand1);
				sprites.add(GraphicsManager.minion1Stand2);
				sprites.add(GraphicsManager.minion1Stand3);
				break;
			case ID_STEM1:
				sprites.add(GraphicsManager.stem1Stand1);
				sprites.add(GraphicsManager.stem1Stand2);
				break;
			case ID_MOLLUSCA1:
				sprites.add(GraphicsManager.mollusca1Stand1);
				sprites.add(GraphicsManager.mollusca1Stand2);
				sprites.add(GraphicsManager.mollusca1Stand3);
				break;
		}
		return sprites;
	}
	
//	// returns the striking sprites of the monster
//	public static ArrayList<BufferedImage> getStrikeSprites(int ID){
//		ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
//		switch (ID){
//			case ID_MINION1:
//				sprites.add(GraphicsManager.minion1Strike1);
//				break;
//		}
//		return sprites;
//	}
//	
//	// returns the casting sprites of the monster
//	public static ArrayList<BufferedImage> getCastSprites(int ID){
//		ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
//		switch (ID){
//			case ID_MINION1:
//				sprites.add(GraphicsManager.minion1Strike1);
//				break;
//		}
//		return sprites;
//	}
//	
//	// returns the sprites of the monster where it gets struck
//	public static ArrayList<BufferedImage> getStruckSprites(int ID){
//		ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
//		switch (ID){
//			case ID_MINION1:
//				sprites.add(GraphicsManager.minion1Strike1);
//				break;
//		}
//		return sprites;
//	}
	
	// returns the standing sprites of the monster
	public static ArrayList<BufferedImage> getDownSprites(int ID){
		ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
		switch (ID){
			case ID_MINION1:
				sprites.add(GraphicsManager.minion1Down1);
				break;
			case ID_STEM1:
				sprites.add(GraphicsManager.minion1Down1);
				break;
			case ID_MOLLUSCA1:
				sprites.add(GraphicsManager.minion1Down1);
				break;
		}
		return sprites;
	}
	
	// returns the sprites of the monster where it gets struck
	public static ArrayList<Integer> getMoves(int ID){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		switch (ID){
			case ID_MINION1:
				moves.add(AbilityManager.INDEX_DEFEND);
				moves.add(AbilityManager.INDEX_ATTACK);
				break;
			case ID_STEM1:
				moves.add(AbilityManager.INDEX_DEFEND);
				moves.add(AbilityManager.INDEX_ATTACK);
				break;
			case ID_MOLLUSCA1:
				moves.add(AbilityManager.INDEX_DEFEND);
				moves.add(AbilityManager.INDEX_ATTACK);
				break;				
			default:
				moves.add(AbilityManager.INDEX_DEFEND);
				moves.add(AbilityManager.INDEX_ATTACK);
		}
		return moves;
	}
	
	//
	public static BufferedImage getShadow(int ID){
		switch(ID){
			//case ID_MINION1
			case ID_STEM1: return GraphicsManager.resizeImage(GraphicsManager.shadowCharacter1, 0.625, 0.625);
			default: return GraphicsManager.shadowCharacter1;
		}
	}
	
	
	
	
	
	
	/* ====== STATS ===== */
	
	// returns the max HP of the monster that matches given ID
	public static int getMonsterMaxHP(int ID) {
		switch (ID) {
			default: return 100;
		}
	}
	
	// returns the max MP of the monster that matches given ID
	public static int getMonsterMaxMP(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the armor of the monster that matches given ID
	public static int getMonsterArmorRating(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the attack of the monster that matches given ID
	public static int getMonsterAttackRating(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the speed of the monster that matches given ID
	public static int getMonsterSpeed(int ID) {
		switch (ID) {
			case ID_MINION1: 	return 3;
			case ID_STEM1: 		return 4;
			case ID_MOLLUSCA1:	return 8;
			default: return 0;
		}
	}
	
	// returns the fire power of the monster that matches given ID
	public static int getMonsterFirePower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the fire resistance of the monster that matches given ID
	public static int getMonsterFireResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the water power of the monster that matches given ID
	public static int getMonsterWaterPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the water resistance of the monster that matches given ID
	public static int getMonsterWaterResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the air power of the monster that matches given ID
	public static int getMonsterAirPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the air resistance of the monster that matches given ID
	public static int getMonsterAirResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the earth power of the monster that matches given ID
	public static int getMonsterEarthPower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the earth resistance of the monster that matches given ID
	public static int getMonsterEarthResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the arcane power of the monster that matches given ID
	public static int getMonsterArcanePower(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the arcane resistance of the monster that matches given ID
	public static int getMonsterArcaneResistance(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the immunity of the monster that matches given ID
	public static int getMonsterImmunity(int ID) {
		switch (ID) {
			default: return 0;
		}
	}
	
	// returns the health regeneration of the monster that matches given ID
	public static int getMonsterHPRegen(int ID) {
		switch (ID) {
			case ID_MOLLUSCA1: return 5;
			default: return 0;
		}
	}
	
	// returns the MANA regeneration of the monster that matches given ID
	public static int getMonsterMPRegen(int ID) {
		switch (ID) {
			default: return 0;
		}
	}

	// returns the experience gained by defeating this monster
	public static int getMonsterExperience(int ID) {
		switch (ID) {
			default: return 50;
		}
	}
	
	// returns the amount of money gained by defeating this monster
	public static int getMonsterMoney(int ID) {
		switch (ID) {
			default: return 40;
		}
	}
	
	// returns the item that this monster can drop
	public static int getMonsterItemDropID(int ID){
		switch (ID) {
			default: return -1;
		}
	}
	
	// returns the chance for this monster to drop its item
	public static double getMonsterItemDropChance(int ID){
		switch (ID) {
			default: return 0.0;
		}
	}
}
