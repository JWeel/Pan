package battle;

import java.util.ArrayList;

import repository.AbilityManager;
import character.Monster;
import character.PlayerCharacter;
import foundation.Core;

//handles what happens during one move in ENGAGEMENT (damage, text and sprite)
class MoveResult {
	
	private Combatant attackingCombatant;
	private Ability move;
	
	private ArrayList<Combatant> affectedCombatants;
	private ArrayList<Monster> monsters;
	private ArrayList<PlayerCharacter> team;
	
	private ArrayList<Integer> HPDamages;
	private ArrayList<Integer> MPDamages;
	private ArrayList<String> damageMessages;
	
	private ArrayList<Integer> HPRestores;
	private ArrayList<Integer> MPRestores;
	private ArrayList<String> HPRestoreMessages;
	private ArrayList<String> MPRestoreMessages;

	private ArrayList<Integer> conditionIndexAfflicts;
	private ArrayList<Integer> conditionIndexRestores;
	private ArrayList<String> conditionAfflictMessages;
	private ArrayList<String> conditionRestoreMessages;

	private String deathMessage;
	private String drainMessage;
	private int totalHPDrain;
	private int totalMPDrain;
	
	private int conditionDamage;
	
	private int progression;
	
	// keep track of the index of the target 
	private int indexOfTarget;
	
	private boolean hasSetUp;
	private boolean curseHasBeenHandled;
	private boolean showMana;

	boolean isStandingBy;
	
	
	MoveResult(Combatant newCombatant, ArrayList<Monster> newMonsters, ArrayList<PlayerCharacter> newTeam){
		this.attackingCombatant = newCombatant;
		this.move = attackingCombatant.getMove();
		this.monsters = newMonsters;
		this.team = newTeam;
		this.affectedCombatants = getAffectedCombatants();
		
		this.HPDamages = new ArrayList<Integer>();
		this.MPDamages = new ArrayList<Integer>();
		this.damageMessages = new ArrayList<String>();
		
		this.HPRestores = new ArrayList<Integer>();
		this.MPRestores = new ArrayList<Integer>();
		this.HPRestoreMessages = new ArrayList<String>();
		this.MPRestoreMessages = new ArrayList<String>();
		
		this.conditionIndexRestores = new ArrayList<Integer>();
		this.conditionRestoreMessages = new ArrayList<String>();
		this.conditionIndexAfflicts = new ArrayList<Integer>();
		this.conditionAfflictMessages = new ArrayList<String>();
		
		this.deathMessage = null;
		this.drainMessage = null;
		this.totalHPDrain = 0;
		this.totalMPDrain = 0;
		this.progression = 0;
		this.hasSetUp = false;
	}
	
	// progresses the results of the move and returns message
	String progressAndGetMessage(){
		
		// make sure attacker has move (meaning: it is not dead or immobile)
		if (move == null) return null;
		// OLD CHECK was (attackingCombatant.getHP() == 0)
		
		String message = null;
		while (message == null){
			if (!hasSetUp){
				message = attackingCombatant.getName() + " ";
				
				// check to make sure target hasn't died (or no revive being cast)
				if (affectedCombatants.get(indexOfTarget).getHP() == 0
						&& !move.targetIsDead) {
					message += "is standing by!";
					
					isStandingBy = true;
					
				// if target hasn't died, sets up this move and tailors first message
				} else {
					if (move.isAttack())
						message += "attacks";
					else if (move.ID < 1000) // SPELL
						message += "casts " + move.name;
					else					 // MONSTER SKILL OR ITEM
						message += "uses " + move.name;
					message += "!";
					setup();
					hasSetUp = true;
					
					// PLAY ATTACK / CAST / USE ANIMATION !
					// PUT TARGETS IF TARGETISFOE IN STRUCK ANIMATION !
				}
				
			// message for each hit combatant
			} else if (progression < affectedCombatants.size()) {
				
				// make sure this (side) target isn't already dead
				if ((affectedCombatants.get(progression) != null &&
						affectedCombatants.get(progression).getHP() ==0)
						&& !move.targetIsDead){
					damageMessages.set(progression,null);
//					HPDamages.set(progression,null);
//					MPDamages.set(progression,null);
//					HPRestoreMessages.set(progression,null);
//					MPRestoreMessages.set(progression,null);
//					HPRestores.set(progression,null);
//					MPRestores.set(progression,null);
//					conditionIndexAfflicts.set(progression,null);
//					conditionIndexRestores.set(progression,null);
//					conditionRestoreMessages.set(progression,null);
//					conditionAfflictMessages.set(progression,null);
				}
				
				// message for damage
				if (damageMessages.get(progression) != null) {
					
					Combatant combatant = affectedCombatants.get(progression);
					combatant.damageHP(HPDamages.get(progression));
					combatant.damageMP(MPDamages.get(progression));
		
					// check for condition
					if (Condition.containsCondition(combatant.getConditions(), 
							Condition.INDEX_BURN)){
						conditionDamage = (int)(combatant.getMaxHP() * 0.1);
					}
					
					// check for death
					if (combatant.getHP() == 0)
						deathMessage = combatant.getName() + " yields!";
					
					message = damageMessages.get(progression);
					damageMessages.set(progression, null);
				
				// additional message if suffering from (burn) condition
				} else if (conditionDamage != 0){
					Combatant combatant = affectedCombatants.get(progression);
					combatant.damageHP(conditionDamage);
					message = combatant.getName() + " took " 
							+ Integer.toString(conditionDamage)
							+ " damage from a burn!";
					conditionDamage = 0;
					
				// additional message if death
				} else if (deathMessage != null) {
					message = deathMessage;
					deathMessage = null;
					// if dead, set potential status affliction to null
					conditionAfflictMessages.set(progression,null);
					
				// message for restore health
				} else if (HPRestoreMessages.get(progression) != null) {	
					
					Combatant combatant = affectedCombatants.get(progression);
					combatant.restoreHP(HPRestores.get(progression));
		
					// check for condition
					if (Condition.containsCondition(combatant.getConditions(), 
							Condition.INDEX_BURN)){
						conditionDamage = (int)(combatant.getMaxHP() * 0.1);
					}
					
					// check for death
					if (combatant.getHP() == 0)
						deathMessage = combatant.getName() + " yields!";
					
					message = HPRestoreMessages.get(progression);
					HPRestoreMessages.set(progression, null);
				
				// message for restore MANA
				} else if (MPRestoreMessages.get(progression) != null) {	
					
					Combatant combatant = affectedCombatants.get(progression);
					combatant.restoreMP(MPRestores.get(progression));
		
					// check for condition
					if (Condition.containsCondition(combatant.getConditions(), 
							Condition.INDEX_BURN)){
						conditionDamage = (int)(combatant.getMaxHP() * 0.1);
					}
					
					// check for death
					if (combatant.getHP() == 0)
						deathMessage = combatant.getName() + " yields!";
					
					message = MPRestoreMessages.get(progression);
					MPRestoreMessages.set(progression, null);
					
					showMana = true;
					
				// if MANA was restored previously, set show MANA to false again
				} else if (showMana){
					showMana = false;
					continue;
					
				// message for condition afflict
				} else if (conditionAfflictMessages.get(progression) != null) {	
					Combatant com = affectedCombatants.get(progression);
					com.getConditions().add(
							new Condition(conditionIndexAfflicts.get(progression),
									move.statusAfflictDuration));
					message = conditionAfflictMessages.get(progression);
					conditionAfflictMessages.set(progression,  null);
					
				// message for condition remove
				} else if (conditionRestoreMessages.get(progression) != null) {	
					Combatant com = affectedCombatants.get(progression);
					Condition con = Condition.getCondition(com.getConditions(),
							conditionIndexRestores.get(progression));
					com.getConditions().remove(con);
					message = conditionRestoreMessages.get(progression);
					conditionRestoreMessages.set(progression,null);
					
				// if no message found, go to next affected foe
				} else {
					progression++;
				}
				
			// if all affected foes have been handled
			} else if (drainMessage != null) {
				attackingCombatant.restoreHP(totalHPDrain);
				attackingCombatant.restoreMP(totalMPDrain);
				message = drainMessage;
				drainMessage = null;

				// if attacker is suffering from (curse) condition
			} else if (Condition.containsCondition(attackingCombatant.getConditions(), Condition.INDEX_CURSE) && !curseHasBeenHandled) {
				int curseDamage = (int)(attackingCombatant.getMaxHP() * 0.15);
				attackingCombatant.damageHP(curseDamage);
				String curseMessage = attackingCombatant.getName() + " took " 
						+ Integer.toString(curseDamage)
						+ " damage from a curse!";
				if (attackingCombatant.getHP() == 0)
					deathMessage = attackingCombatant.getName() + " yields!";
				curseHasBeenHandled = true;
				message = curseMessage;
				
			// additional message if attacker succumbed to poison
			} else if (deathMessage != null) {
				message = deathMessage;
				deathMessage = null;
				
			// if no message, return null to start next move result
			} else {
				curseHasBeenHandled = false;
				return null;
			}
		
		}
		return message;
	}
	
	// calculates effects of move and readies messages
	private void setup(){			
		for (int i = 0; i < affectedCombatants.size(); i++){
			if (affectedCombatants.get(i) == null){
				damageMessages.add(null);
				HPDamages.add(null);
				MPDamages.add(null);
				HPRestoreMessages.add(null);
				MPRestoreMessages.add(null);
				HPRestores.add(null);
				MPRestores.add(null);
				conditionIndexAfflicts.add(null);
				conditionIndexRestores.add(null);
				conditionRestoreMessages.add(null);
				conditionAfflictMessages.add(null);
				continue;
			}
			
			// first calculate damage
			// base damage of move
			int baseHPDamage = move.HPDamage;
			// armor and attack is only counted when using regular attacks
			int netArmorRating, netAttackRating;		
			if (move.isAttack()) {
				netArmorRating = affectedCombatants.get(i).getArmor()
						+ affectedCombatants.get(i).getBonusArmorRating();
				netAttackRating = attackingCombatant.getAttack()
						+ attackingCombatant.getBonusAttackRating();
			}
			else {
				netArmorRating = 0;
				netAttackRating = 0;
			}

			// sets elemental power and resistance based on element of the move
			int netFirePower=0, netFireResistance=0, netWaterPower=0, netWaterResistance=0,
					netAirPower=0, netAirResistance=0, netEarthPower=0, netEarthResistance=0,
					netArcanePower=0, netArcaneResistance=0;
			
			// adjust power and resistance only if spell deals damage
			if (baseHPDamage > 0) {
				switch (move.elementIndex){
					case (Core.INDEX_FIRE):
						netFirePower = attackingCombatant.getFirePower()
								+ attackingCombatant.getBonusFirePower();
						netFireResistance = affectedCombatants.get(i).getFireResistance()
								+ affectedCombatants.get(i).getBonusFireResistance();
						break;
					case (Core.INDEX_WATER):
						netWaterPower = attackingCombatant.getWaterPower()
								+ attackingCombatant.getBonusWaterPower();
						netWaterResistance = affectedCombatants.get(i).getWaterResistance()
								+ affectedCombatants.get(i).getBonusWaterResistance();
						break;
					case (Core.INDEX_AIR):
						netAirPower = attackingCombatant.getAirPower()
								+ attackingCombatant.getBonusAirPower();
						netAirResistance = affectedCombatants.get(i).getAirResistance()
								+ affectedCombatants.get(i).getBonusAirResistance();
						break;
					case (Core.INDEX_EARTH):
						netEarthPower = attackingCombatant.getEarthPower()
								+ attackingCombatant.getBonusEarthPower();
						netEarthResistance = affectedCombatants.get(i).getEarthResistance()
								+ affectedCombatants.get(i).getBonusEarthResistance();
						break;
					case (Core.INDEX_ARCANE):
						netArcanePower = attackingCombatant.getArcanePower()
								+ attackingCombatant.getBonusArcanePower();
						netArcaneResistance = affectedCombatants.get(i).getArcaneResistance()
								+ affectedCombatants.get(i).getBonusArcaneResistance();
						break;
				}
			}
			
			// adjusts modifier based on how far target is away from main target
			double distanceModifier = 1.0;
			int distance = Math.abs(indexOfTarget - i);
			if (move.reach != AbilityManager.INDEX_REACH_FULL_THREE
					&& move.reach != AbilityManager.INDEX_REACH_FULL_FIVE)
				distanceModifier -= 0.25 * distance;
			
			// adjusts modifier based on whether target is defending
			double defendingModifier = 1.0;
			if (affectedCombatants.get(i).getMove() != null
					&& affectedCombatants.get(i).getMove().isDefend())
				defendingModifier = 0.5;
			
			// formula (TODO IMPROVE)
			int netHPDamage =  
				(int)
				( ( (baseHPDamage + netAttackRating + netFirePower + netWaterPower
					+ netAirPower + netEarthPower + netArcanePower)
					-
				  (netArmorRating + netFireResistance + netWaterResistance 
					+ netAirResistance + netEarthResistance + netArcaneResistance) )
				* distanceModifier * defendingModifier);
			
			if (netHPDamage < 0) netHPDamage = 0;
			
			int netMPDamage = (int)(move.MPDamage * distanceModifier);
			
			// real damage = adjusted for remaining life (no over-kill), for drain
			int realHPDamage, realMPDamage;
			if (affectedCombatants.get(i).getHP() - netHPDamage < 0)
				realHPDamage = affectedCombatants.get(i).getHP();
			else realHPDamage = netHPDamage;
			if (affectedCombatants.get(i).getMP() - netMPDamage < 0)
				realMPDamage = affectedCombatants.get(i).getMP();
			else realMPDamage = netMPDamage;
			
			int realHPDrain = (int)(realHPDamage * move.HPDrain);
			totalHPDrain += realHPDrain;
			int realMPDrain = (int)(realMPDamage * move.MPDrain);
			totalMPDrain += realMPDrain;
			
			HPDamages.add(netHPDamage);
			MPDamages.add(netMPDamage);
			
			if (netHPDamage > 0 || netMPDamage > 0) {
				// prepare damage message
				String message = affectedCombatants.get(i).getName();
				if (HPDamages.get(i) > 0)
					message += " took "	+ Integer.toString(HPDamages.get(i)) + " damage";
				if (MPDamages.get(i) > 0 && HPDamages.get(i) > 0)
					message += " and";
				if (MPDamages.get(i) > 0)
					message += " lost " + Integer.toString(MPDamages.get(i)) + " mana";
				message += "!";
				damageMessages.add(message);
				
			// if not dealing any damage, add a null string
			} else damageMessages.add(null);
				
			
			// calculate healing
			int baseHPRestore = move.HPRestore;
			int baseMPRestore = move.MPRestore;
			
			// if ability heals
			if (baseHPRestore > 0) {
				int empoweredHPRestore, netHPRestore;
				
				double powerHPRestoreModifier = 0.2;
				
				// formula (TODO improve)
				empoweredHPRestore = baseHPRestore +
					(int)((netFirePower + netWaterPower + netAirPower
									+ netEarthPower + netArcanePower)
					 * powerHPRestoreModifier);
						
				String message = affectedCombatants.get(i).getName();
				
				// if not dead
				if (affectedCombatants.get(i).getHP() != 0) {
					// if no over-healing
					if (affectedCombatants.get(i).getMaxHP() 
							- affectedCombatants.get(i).getHP()
								> empoweredHPRestore) {
						
						netHPRestore = empoweredHPRestore;
						message += " recovers " 
								+ Integer.toString(netHPRestore) +
								"H!";
					
					// if over-healing
					} else {
						netHPRestore = affectedCombatants.get(i).getMaxHP()
								- affectedCombatants.get(i).getHP();
						message += " fully recovers their health!";
					}
					
				// if dead, thus being revived
				} else {
					if (empoweredHPRestore > affectedCombatants.get(i).getMaxHP())
						netHPRestore = affectedCombatants.get(i).getMaxHP();
					else netHPRestore = empoweredHPRestore;
					message += " is revived with " 
							+ Integer.toString(netHPRestore) + " H!";
				}
				HPRestoreMessages.add(message);
				HPRestores.add(netHPRestore);
			
			// if not healing, add null to lists
			} else {
				HPRestoreMessages.add(null);
				HPRestores.add(null);				
			}
			
			// if ability restores MANA
			if (baseMPRestore > 0) {
				int empoweredMPRestore, netMPRestore;
				
				double powerMPRestoreModifier = 0.1;
				
				// formula (TODO improve)
				empoweredMPRestore = baseMPRestore +
					(int)((netFirePower + netWaterPower + netAirPower
									+ netEarthPower + netArcanePower)
					 * powerMPRestoreModifier);
						
				String message = affectedCombatants.get(i).getName();
				
				// if no over-healing
				if (affectedCombatants.get(i).getMaxMP() 
						- affectedCombatants.get(i).getMP()
							> empoweredMPRestore) {
					
					netMPRestore = empoweredMPRestore;
					message += " recovers " 
							+ Integer.toString(netMPRestore) +
							"M!";
				
				// if over-healing
				} else {
					netMPRestore = affectedCombatants.get(i).getMaxMP()
							- affectedCombatants.get(i).getMP();
					message += " fully recovers their mana!";
				}
				MPRestoreMessages.add(message);
				MPRestores.add(netMPRestore);
			
			// if not restoring MANA, add null to lists
			} else {
				MPRestoreMessages.add(null);
				MPRestores.add(null);				
			}
			
			// calculate conditions
			// check if afflict
			if (move.statusIndexAfflict != -1){
				
				// check whether statistic modifier or not
				if (!Condition.isStatModifier(move.statusIndexAfflict)) {
				
					double afflictionChance = move.statusAfflictChance;
					
					if (afflictionChance != 1.0) {
						
						// adjust affliction based on immunity of target
						afflictionChance -= 
								affectedCombatants.get(i).getImmunity() * 0.02;
					}
					
					// TODO if (condition == REAP && target.cannotBeReaped)
					// afflictionChance = 0.0
					
					// check if target is not already afflicted, and if he will be
					if (!Condition.containsCondition(
							affectedCombatants.get(i).getConditions(), move.statusIndexAfflict)
								&& afflictionChance > Core.random.nextDouble()) {
						
						conditionIndexAfflicts.add(move.statusIndexAfflict);
						String message = affectedCombatants.get(i).getName();
						message += " is " 
									+ Condition.getName(move.statusIndexAfflict)
										+ "!";
						conditionAfflictMessages.add(message);
						
					// if not afflicted, add null messages
					} else {
						conditionIndexAfflicts.add(null);
						conditionAfflictMessages.add(null);
					}
				
				// statistic modifier conditions work differently
				} else {
					
					// TODO
					conditionIndexAfflicts.add(null);
					conditionAfflictMessages.add(null);
				}
				
			// if no status afflict, add null messages
			} else {
				conditionIndexAfflicts.add(null);
				conditionAfflictMessages.add(null);
			}
			
			// check if remove
			if (move.statusIndexRestore != -1){
				// check whether statistic modifier or not
				if (!Condition.isStatModifier(move.statusIndexRestore)) {
				
					// check if target is actually afflicted
					if (Condition.containsCondition(
							affectedCombatants.get(i).getConditions(), move.statusIndexRestore)) {
						
						conditionIndexRestores.add(move.statusIndexRestore);
						String message = affectedCombatants.get(i).getName();
						message += " is no longer " 
									+ Condition.getName(move.statusIndexRestore)
										+ "!";
						conditionRestoreMessages.add(message);
						
					// if is not actually afflicted, add null messages
					} else {
						conditionIndexRestores.add(null);
						conditionRestoreMessages.add(null);
					}
				
				// statistic modifier conditions work differently
				} else {
					// TODO
					conditionIndexRestores.add(null);
					conditionRestoreMessages.add(null);
				}
			
			// if no status remove, add null messages
			} else {
				conditionIndexRestores.add(null);
				conditionRestoreMessages.add(null);
			}
			
		}
		
		// prepare drain message
		if (totalHPDrain > 0 || totalMPDrain > 0) {
			drainMessage = attackingCombatant.getName();
			drainMessage += " drained ";
			if (totalHPDrain > 0)
				drainMessage += Integer.toString(totalHPDrain) + " health";
			if (totalHPDrain > 0 && totalMPDrain > 0)
				drainMessage += " and";
			if (totalMPDrain > 0)
				drainMessage += Integer.toString(totalMPDrain) + " mana";
			drainMessage += "!";
		}
	}
	
	// returns a list of the combatants affected by this move
	private ArrayList<Combatant> getAffectedCombatants(){
		ArrayList<Combatant> affected = new ArrayList<Combatant>();
		
		ArrayList<Combatant> targetTeam = new ArrayList<Combatant>();
		if (attackingCombatant.isMonster()) {
			if (move.targetIsFoe) targetTeam.addAll(team);
			else targetTeam.addAll(monsters);
		} else {
			if (move.targetIsFoe) targetTeam.addAll(monsters);
			else targetTeam.addAll(team);			
		}
		
		switch (move.reach){
			case AbilityManager.INDEX_REACH_SINGLE_TARGET:
				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 0;
				break;
			case AbilityManager.INDEX_REACH_DIMINISHING_THREE:
			case AbilityManager.INDEX_REACH_FULL_THREE:
				// add left target
				if (move.target > 0) 
					affected.add(targetTeam.get(move.target-1));
				else affected.add(null);
				
				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 1;
				
				// add right target
				if (move.target < targetTeam.size()-1) 
					affected.add(targetTeam.get(move.target+1));
				break;
			case AbilityManager.INDEX_REACH_DIMINISHING_FIVE:
			case AbilityManager.INDEX_REACH_FULL_FIVE:
				// add second left target
				if (move.target > 1) 
					affected.add(targetTeam.get(move.target-2));
				else affected.add(null);
				
				// add first left target
				if (move.target > 0) 
					affected.add(targetTeam.get(move.target-1));
				else affected.add(null);
				
				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 2;
				
				// add first right target
				if (move.target < targetTeam.size()-1) 
					affected.add(targetTeam.get(move.target+1));
				
				// add second right target
				if (move.target < targetTeam.size()-2) 
					affected.add(targetTeam.get(move.target+2));
				break;
			case AbilityManager.INDEX_REACH_RIGHT_TWO:
				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 0;
				
				// add first right target
				if (move.target < targetTeam.size()-1) 
					affected.add(targetTeam.get(move.target+1));
				
				// add second right target
				if (move.target < targetTeam.size()-2) 
					affected.add(targetTeam.get(move.target+2));
				break;
			case AbilityManager.INDEX_REACH_RIGHT_FOUR:
				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 0;
				
				// add first right target
				if (move.target < targetTeam.size()-1) 
					affected.add(targetTeam.get(move.target+1));
				
				// add second right target
				if (move.target < targetTeam.size()-2) 
					affected.add(targetTeam.get(move.target+2));
				
				// add third right target
				if (move.target < targetTeam.size()-3) 
					affected.add(targetTeam.get(move.target+3));
				
				// add fourth right target
				if (move.target < targetTeam.size()-4) 
					affected.add(targetTeam.get(move.target+4));
				break;
			case AbilityManager.INDEX_REACH_LEFT_TWO:
				// add second left target
				if (move.target > 1) 
					affected.add(targetTeam.get(move.target-2));
				else affected.add(null);
				
				// add first left target
				if (move.target > 0) 
					affected.add(targetTeam.get(move.target-1));
				else affected.add(null);
				
				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 2;
				
				break;
			case AbilityManager.INDEX_REACH_LEFT_FOUR:
				// add fourth left target
				if (move.target > 3) 
					affected.add(targetTeam.get(move.target-4));
				else affected.add(null);

				// add third left target
				if (move.target > 2) 
					affected.add(targetTeam.get(move.target-3));
				else affected.add(null);

				// add second left target
				if (move.target > 1) 
					affected.add(targetTeam.get(move.target-2));
				else affected.add(null);

				// add first left target
				if (move.target > 0) 
					affected.add(targetTeam.get(move.target-1));
				else affected.add(null);

				// add main target
				affected.add(targetTeam.get(move.target));
				indexOfTarget = 4;
				break;
		}
		return affected;
	}
	
	// returns whether the character panel on screen should show the MANA bar
	boolean getShowMana(){
		return showMana;
	}
}