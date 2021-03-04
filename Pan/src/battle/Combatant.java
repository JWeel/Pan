package battle;

import java.util.ArrayList;

import character.Item;

//import foundation.Core;

public interface Combatant{
	
    public Integer getSpeed();
    //public int getIndex();
    public boolean isMonster();
    
    public String getName();
    
    public int getMaxHP();
    public int getMaxMP();
    public int getHP();
    public int getMP();
    public int getArmor();
    public int getAttack();
    public int getFirePower();
    public int getFireResistance();
    public int getWaterPower();
    public int getWaterResistance();
    public int getAirPower();
    public int getAirResistance();
    public int getEarthPower();
    public int getEarthResistance();
    public int getArcanePower();
    public int getArcaneResistance();
    public int getImmunity();
    public int getHPRegen();
    public int getMPRegen();
    
    public int getBonusMaxHP();
    public int getBonusMaxMP();
    public int getBonusArmorRating();
    public int getBonusAttackRating();
    public int getBonusFirePower();
    public int getBonusFireResistance();
    public int getBonusWaterPower();
    public int getBonusWaterResistance();
    public int getBonusAirPower();
    public int getBonusAirResistance();
    public int getBonusEarthPower();
    public int getBonusEarthResistance();
    public int getBonusArcanePower();
    public int getBonusArcaneResistance();
    public int getBonusImmunity();
    public int getBonusHPRegen();
    public int getBonusMPRegen();
    
    public ArrayList<Condition> getConditions();
    
    public Ability getMove();
    
    public void damageHP(int damage);
    public void damageMP(int damage);
    
    public void restoreHP(int amount);
    public void restoreMP(int amount);
    
    public Item getUsedItem();
}