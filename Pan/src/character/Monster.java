package character;

import graphics.Animator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import battle.Ability;
import battle.Combatant;
import battle.Condition;
import repository.MonsterManager;

public class Monster implements Combatant {
	
	public Animator animator;

	public ArrayList<BufferedImage> standSprites, strikeSprites, 
									castSprites, struckSprites, downSprites;
	public BufferedImage shadow;
	
	public boolean isStanding, isStriking, isCasting, isStruck;
	
	public String name;
	
	public int monsterID;
	
	public Integer speed;
	
	// base statistics
	public int maxHP, maxMP, HP, MP, armorRating, attackRating, immunity,  
	firePower, fireResistance, waterPower, waterResistance,	airPower, 
	airResistance, earthPower, earthResistance, arcanePower, arcaneResistance,
	HPRegen, MPRegen;
	
	// bonuses to statistics gained during battle
	public int bonusMaxHP, bonusMaxMP, bonusArmorRating, bonusAttackRating, 
	bonusFirePower, bonusFireResistance, bonusWaterPower, bonusWaterResistance, 
	bonusAirPower, bonusAirResistance, bonusEarthPower, bonusEarthResistance, 
	bonusArcanePower, bonusArcaneResistance, bonusImmunity,
	bonusHPRegen, bonusMPRegen;
	
	// rewards
	public int experience, money, itemDropID;
	public double itemDropChance;
	
	// the move the monster is planning to perform in battle
	public Ability move;
	
	// the status effects the monster is suffering from in a battle
	public ArrayList<Condition> conditions;
	
	public Monster(int ID) {
		
		this.monsterID = ID;
		
		this.standSprites = MonsterManager.getStandSprites(ID);
		//strikeSprites = MonsterManager.getStrikeprites(ID);
		//castSprites = MonsterManager.getCastSprites(ID);
		//struckSprites = MonsterManager.getStruckSprites(ID);
		this.downSprites = MonsterManager.getDownSprites(ID);
		this.shadow = MonsterManager.getShadow(ID);
		this.name = MonsterManager.getName(ID);
		
		this.conditions = new ArrayList<Condition>();

		this.maxHP = 			MonsterManager.getMonsterMaxHP(ID);
		this.maxMP = 			MonsterManager.getMonsterMaxMP(ID);
		this.HP = 				this.maxHP;
		this.MP = 				this.maxMP;
		this.armorRating = 		MonsterManager.getMonsterArmorRating(ID);
		this.attackRating = 	MonsterManager.getMonsterAttackRating(ID);
		this.speed = 			MonsterManager.getMonsterSpeed(ID);
		this.firePower = 		MonsterManager.getMonsterFirePower(ID);
		this.fireResistance = 	MonsterManager.getMonsterFireResistance(ID);
		this.waterPower = 		MonsterManager.getMonsterWaterPower(ID);
		this.waterResistance = 	MonsterManager.getMonsterWaterResistance(ID);
		this.airPower = 		MonsterManager.getMonsterAirPower(ID);
		this.airResistance = 	MonsterManager.getMonsterAirResistance(ID);
		this.earthPower = 		MonsterManager.getMonsterEarthPower(ID);
		this.earthResistance = 	MonsterManager.getMonsterEarthResistance(ID);
		this.arcanePower =		MonsterManager.getMonsterArcanePower(ID);
		this.arcaneResistance =	MonsterManager.getMonsterArcaneResistance(ID);
		this.immunity = 		MonsterManager.getMonsterImmunity(ID);
		this.HPRegen = 			MonsterManager.getMonsterHPRegen(ID);
		this.MPRegen = 			MonsterManager.getMonsterMPRegen(ID);
		
		this.experience = 		MonsterManager.getMonsterExperience(ID);
		this.money = 			MonsterManager.getMonsterMoney(ID);
		this.itemDropID = 		MonsterManager.getMonsterItemDropID(ID);
		this.itemDropChance = 	MonsterManager.getMonsterItemDropChance(ID);

		this.HP = 1;
//		this.conditions.add(new Condition(4,4));
	}
	
	public BufferedImage getShadow(){
		if (HP==0) return null;
		else return shadow;
	}
	
	// returns a certain sprite for animating
	public BufferedImage getAnimationSprite(){
		
		if (animator == null) return standSprites.get(0);
		
		if (HP==0) return downSprites.get(0);
		
		if (isStanding) {
			return standSprites.get(animator.getAnimationIndex());
		} else if (isStriking) {
			
		} else if (isCasting) {
			
		} else if (isStruck) {
			
		}
		
		return null;
	}	
	
	// creates animation thread and starts it
	public void startAnimation(){
		if (animator != null) stopAnimation();
		
		animator = new Animator(standSprites.size(), 400);
		animator.thread = new Thread(animator);
		animator.thread.start();
	}
	
	// destroys the animation thread
	public void stopAnimation(){
		animator.thread.interrupt();
		animator = null;
	}

	// these methods adjust health and MANA during battle
	@Override
	public void damageHP(int damage){
		this.HP -= damage;
		if (this.HP <= 0) {
			this.HP = 0;
			this.conditions.clear();
		}
	}
	
	@Override
	public void damageMP(int damage){
		this.MP -= damage;
		if (this.MP <= 0) this.MP = 0;
	}
	
	@Override
	public void restoreHP(int amount){
		this.HP += amount;
		if (this.HP > this.maxHP) this.HP = this.maxHP;
	}
	
	@Override
	public void restoreMP(int amount){
		this.MP += amount;
		if (this.MP > this.maxMP) this.MP = this.maxMP;
	}
	
	// these methods return objects used during battle
	@Override
	public Ability getMove() {
		return this.move;
	}
	
	@Override
	public Item getUsedItem(){
		return null;
	}
	
	@Override
	public ArrayList<Condition> getConditions() {
		return this.conditions;
	}
	
	// these methods return statistics for use during battle
	@Override
	public Integer getSpeed() {
		return this.speed;
	}

	@Override
	public boolean isMonster() {
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getHP() {
		return this.HP;
	}

	@Override
	public int getMP() {
		return this.MP;
	}

	@Override
	public int getArmor() {
		return this.armorRating;
	}

	@Override
	public int getAttack() {
		return this.attackRating;
	}

	@Override
	public int getFirePower() {
		return this.fireResistance;
	}

	@Override
	public int getFireResistance() {
		return this.fireResistance;
	}

	@Override
	public int getWaterPower() {
		return this.waterPower;
	}

	@Override
	public int getWaterResistance() {
		return this.waterResistance;
	}

	@Override
	public int getAirPower() {
		return this.airPower;
	}

	@Override
	public int getAirResistance() {
		return this.airResistance;
	}

	@Override
	public int getEarthPower() {
		return this.earthPower;
	}

	@Override
	public int getEarthResistance() {
		return this.earthResistance;
	}

	@Override
	public int getArcanePower() {
		return this.arcanePower;
	}

	@Override
	public int getArcaneResistance() {
		return this.arcaneResistance;
	}

	@Override
	public int getBonusMaxHP() {
		return this.bonusMaxHP;
	}

	@Override
	public int getBonusMaxMP() {
		return this.bonusMaxMP;
	}

	@Override
	public int getBonusArmorRating() {
		return this.bonusArmorRating;
	}

	@Override
	public int getBonusAttackRating() {
		return this.bonusAttackRating;
	}

	@Override
	public int getBonusFirePower() {
		return this.bonusFirePower;
	}

	@Override
	public int getBonusFireResistance() {
		return this.bonusFireResistance;
	}

	@Override
	public int getBonusWaterPower() {
		return this.bonusWaterPower;
	}

	@Override
	public int getBonusWaterResistance() {
		return this.bonusWaterResistance;
	}

	@Override
	public int getBonusAirPower() {
		return this.bonusAirPower;
	}

	@Override
	public int getBonusAirResistance() {
		return this.bonusAirResistance;
	}

	@Override
	public int getBonusEarthPower() {
		return this.bonusEarthPower;
	}

	@Override
	public int getBonusEarthResistance() {
		return this.bonusEarthResistance;
	}

	@Override
	public int getBonusArcanePower() {
		return this.bonusArcanePower;
	}

	@Override
	public int getBonusArcaneResistance() {
		return this.bonusArcaneResistance;
	}
	
	@Override
	public int getImmunity() {
		return this.immunity;
	}
	
	@Override
	public int getBonusImmunity() {
		return this.bonusImmunity;
	}

	@Override
	public int getHPRegen() {
		return this.HPRegen;
	}

	@Override
	public int getMPRegen() {
		return this.MPRegen;
	}

	@Override
	public int getBonusHPRegen() {
		return this.bonusHPRegen;
	}

	@Override
	public int getBonusMPRegen() {
		return this.bonusMPRegen;
	}

	@Override
	public int getMaxHP() {
		return this.maxHP;
	}

	@Override
	public int getMaxMP() {
		return this.maxMP;
	}
}
