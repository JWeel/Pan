package battle;

import java.util.ArrayList;

import character.Monster;
import character.PlayerCharacter;

// handles what happens at the end of an ENGAGEMENT turn (damage, text and sprite)
public class TurnResult {

	// a list of all alive combatants
	ArrayList<Combatant> combatants;
	
	// a list of conditions that can be removed (because their duration is over)
	ArrayList<Condition> removableConditions;
	ArrayList<String> removableConditionMessages;
	ArrayList<Integer> removableConditionCombatantIndexes;

	// a message to be printed when a combatant dies from poison
	String deathMessage;
	
	// keeps track of what combatant is being handled
	int progression;
	
	// booleans for checking if that part of the TURNRESULT has been handled
	boolean poisonHandled, reapHandled, HPHandled, MPHandled, cycleHandled;

	
	// the turn result object needs combatants in order to handle them
	TurnResult(ArrayList<PlayerCharacter> team, ArrayList<Monster> monsters){
		combatants = new ArrayList<Combatant>();
		removableConditions = new ArrayList<Condition>();
		removableConditionMessages = new ArrayList<String>();
		removableConditionCombatantIndexes = new ArrayList<Integer>();
		for (PlayerCharacter PC : team)	if (PC.HP != 0) combatants.add(PC);
		for (Monster m : monsters) if (m.HP != 0) combatants.add(m);
	}
	
	// handles damage and regeneration, as well as returns text and updates sprite
	String progressAndGetMessage(){
		if (progression < combatants.size()) {
			// check for poison
			if (Condition.containsCondition(
					combatants.get(progression).getConditions(), 
						Condition.INDEX_POISON) && !poisonHandled) {
				
				int poisonDamage = (int)(combatants.get(progression).getMaxHP() * 0.2);
				combatants.get(progression).damageHP(poisonDamage);
				String poisonMessage = combatants.get(progression).getName() + " took " 
						+ Integer.toString(poisonDamage)
						+ " damage from a poison!";
				
				// check for death
				if (combatants.get(progression).getHP() == 0)
					deathMessage = combatants.get(progression).getName() + " yields!";
				
				poisonHandled = true;
				return poisonMessage;
				
			// check for death after poison
			} else if (deathMessage != null) {
				String message = deathMessage;
				deathMessage = null;
				return message;
			
			// check for reap
			} else if (Condition.containsCondition(
					combatants.get(progression).getConditions(), 
						Condition.INDEX_REAP) && !reapHandled
							&& combatants.get(progression).getHP() != 0){
				
				String reapMessage = null;
				Condition reap = Condition.getCondition(
						combatants.get(progression).getConditions(), 
							Condition.INDEX_REAP);
				reap.duration--;
				if (reap.duration == 0) {
					reapMessage = combatants.get(progression).getName()
						+ " was taken by the reap!";
					combatants.get(progression).getConditions().remove(reap);
					combatants.get(progression).damageHP(combatants.get(progression).getMaxHP());
				}
				
				reapHandled = true;
				if (reapMessage != null)
					return reapMessage;

				else return progressAndGetMessage();
				
			// check for regenerating HP
			} else if (!HPHandled && combatants.get(progression).getHP() != 0){
				
				HPHandled = true;
				int HPRegen = combatants.get(progression).getHPRegen()
						+ combatants.get(progression).getBonusHPRegen();
				if (HPRegen != 0){
					combatants.get(progression).restoreHP(HPRegen);
					String message = combatants.get(progression).getName()
							+ " regenerates " +
							Integer.toString(HPRegen) + " health!";
					return message;
				}
				else return progressAndGetMessage();
				
			// check for regenerating MP	
			} else if (!MPHandled && combatants.get(progression).getHP() != 0){
				MPHandled = true;
				int MPRegen = combatants.get(progression).getMPRegen()
						+ combatants.get(progression).getBonusMPRegen();
				if (MPRegen != 0){
					combatants.get(progression).restoreMP(MPRegen);
					String message = combatants.get(progression).getName()
							+ " regenerates " +
							Integer.toString(MPRegen) + " mana!";
					return message;
				}
				else return progressAndGetMessage();
				
			// go to next	
			} else {
				
				poisonHandled = false;
				reapHandled = false;
				HPHandled = false;
				MPHandled = false;
				
				progression++;
				return progressAndGetMessage();
			}

			// if everyone has been handled and no messages left
		} else {
		
			
			// cycle conditions
			if (!cycleHandled) {
				cycleHandled = true;
				for (Combatant com : combatants){
					for (Condition con : com.getConditions())
						if (!con.isPersistent()
								&& con.ID != Condition.INDEX_REAP) {
							con.duration--;
							if (con.duration == 0) {
								removableConditions.add(con);
								String message = com.getName();
								
								if (!Condition.isStatModifier(con.ID))
									message += " is no longer " 
											+ Condition.getName(con.ID) + "!";
								else message += " bla";
								
								removableConditionMessages.add(message);
								removableConditionCombatantIndexes.add(combatants.indexOf(com));
							}
						}
				}
			}
				
			// if conditions are fading away, display message and remove
			if (!removableConditions.isEmpty()) {
				String message = removableConditionMessages.get(0);
				combatants.get(removableConditionCombatantIndexes.get(0)).getConditions().remove(removableConditions.get(0));
				removableConditions.remove(0);
				removableConditionMessages.remove(0);
				removableConditionCombatantIndexes.remove(0);
				return message;
			}
			
			// and set all combatants into their STAND animations
			// TODO
			
			// then return null
			return null;
		}
	}
}
