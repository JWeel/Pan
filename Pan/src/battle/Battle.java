package battle;

import foundation.Core;
import graphics.Animator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import repository.AbilityManager;
import repository.GraphicsManager;
import repository.ItemManager;
import repository.MonsterManager;
import sorting.SpeedComparator;
import character.Item;
import character.Monster;
import character.PlayerCharacter;

public class Battle {

	private static final int AMOUNT_OF_BROWSE_ANIMATIONS = 2;
	private static final int MENU_ANIMATION_INTERVAL = 450;
	private static final int POINTER_ANIMATION_INTERVAL = 350;
	
	private static final int INDEX_STAGE_INTRODUCTION = 0;
	private static final int INDEX_STAGE_PREPARATION = 1;
	private static final int INDEX_STAGE_ENGAGEMENT = 2;
	private static final int INDEX_STAGE_CONCLUSION = 3;
	
	private static final int BATTLE_MENU_ITEM_SIZE = 32;
	private static final int BATTLE_SUB_ITEM_HEIGHT = 16;
		
	private static final int INDEX_SUB_SPELLBOOK = 0;
	private static final int INDEX_SUB_INVENTORY = 1;
		
	private int stageIndex;
	private int stageEnd;
	private int stageProgression;
	private int subMenuIndex;

	private int preparationCharacter, chosenBattleMenuItem;
	
	// whether to show the MANA bar during the engagement stage
	private boolean engagementShowMana;
	
	private ArrayList<Integer> monsterIDs;
	private ArrayList<Integer> monsterAmounts;
	
	private ArrayList<Item> combatInventory;
	
	// keep track of team and monster during engagement stage
	private ArrayList<Combatant> combatants;
	private int combatantIndex;
	private MoveResult moveResult; 
	private TurnResult turnResult;
	private boolean finishedCombatantMove;

	private String combatMessage;

	public ArrayList<Monster> monsters;
	
	private boolean succesfulBattle;
	private ArrayList<String> conclusionMessages;
	
	// used in the battle menu
	public int battleMenuItem;
	public int battleSubItem;
	public int battleSubPage;
	public int battleMenuTarget;
	public boolean justCycledRight, justCycledLeft, justCycledUp, justCycledDown;

	public Animator cursorAnimator;
	public Animator pointerAnimator;
	
	public boolean inTransition;
	public boolean justRetrogressed;
	
	PlayerCharacter leader;
		
	public Battle(ArrayList<Integer> encounter, PlayerCharacter character) {
		monsters = new ArrayList<Monster>();
		for (int i = 0; i < encounter.size(); i++){
			Monster monster = new Monster(encounter.get(i));
			monster.isStanding = true;
			monsters.add(monster);
		}
		
		//leader = character;

		preparationCharacter = getFirstPreparationCharacter();
		setStage(-2);
	}

	// performs an action so that the next message or menu is shown
	public void performBattleAction(){
		
		switch (stageIndex){
			case INDEX_STAGE_INTRODUCTION:
			
				progressStage(1);
				
				
				break;
			case INDEX_STAGE_PREPARATION:

				switch (stageProgression){
					case 0: // fight or flight
						switch (battleMenuItem){
							case 0:
								progressStage(1);
								break;
							case 1:
								// check if can flee?
								Core.endBattle();
								break;
						}
						break;
					case 1: // pick fight method
						
						switch (battleMenuItem){
						
							case 0:
								
								// if not disarmed
								if (!Condition.containsCondition(
										Core.team.get(preparationCharacter).conditions,
											Condition.INDEX_DISARM)) {
									// set move = attack
									Core.team.get(preparationCharacter).move
										= new Ability(AbilityManager.INDEX_ATTACK);
									chosenBattleMenuItem = 0;
									
									// progressStage by 2 (pick target)
									progressStage(2);
									setInitialTarget();
								}
								break;
							case 1:
								// set move = defend
								Core.team.get(preparationCharacter).move
									= new Ability(AbilityManager.INDEX_DEFEND);
								chosenBattleMenuItem = 1;
								
								// progressStage by 3 (next character or stage)
								progressStage(3);
								break;
							case 2:
								
								// if not spells locked
								if (!Condition.containsCondition(
										Core.team.get(preparationCharacter).conditions,
											Condition.INDEX_LOCKSPELL)) {
									// progressStage (to pick ability)
									progressStage(1);
									chosenBattleMenuItem = 2;
									subMenuIndex = INDEX_SUB_SPELLBOOK;
								}
								break;
							case 3:
								
								// set move = chain ?????
								
								chosenBattleMenuItem = 3;
								//subMenuIndex = INDEX_SUB_CHAIN;
								// progressStage by one?
								break;
							case 4:
								
								// if not items locked
								if (!Condition.containsCondition(
										Core.team.get(preparationCharacter).conditions,
											Condition.INDEX_LOCKITEM)) {
									// progressStage (to pick item)
									progressStage(1);
									chosenBattleMenuItem = 4;
									subMenuIndex = INDEX_SUB_INVENTORY;
								}
								break;						
						}
						break;						
					case 2: // pick item or ability (or chain?)

						// set move = chosen spell/item
						if (subMenuIndex == INDEX_SUB_SPELLBOOK){
							if (!Core.team.get(preparationCharacter).spellbook.isEmpty())
								// check if enough energy
								if (hasSufficientEnergy(getRealSubIndex(battleSubItem)))
									Core.team.get(preparationCharacter).move
										= new Ability(
												Core.team.get(preparationCharacter).spellbook.get(getRealSubIndex(battleSubItem)).ID);
						} else if (subMenuIndex == INDEX_SUB_INVENTORY) {
							if (!combatInventory.isEmpty()) {
								Core.team.get(preparationCharacter).usedItem
									= combatInventory.get(getRealSubIndex(battleSubItem));
								Core.team.get(preparationCharacter).move
									= new Ability(
											Core.team.get(preparationCharacter).usedItem.combatAbility.ID);
							}
						}
						//if move was made, progress
						if (Core.team.get(preparationCharacter).move != null) {
							// progressStage by 1 (pick target)
							progressStage(1);
							setInitialTarget();
						}
						break;
					case 3: // select target
						
						Core.team.get(preparationCharacter).move.target = battleMenuTarget;
						progressStage(1);
						break;

				}
				break;
			case INDEX_STAGE_ENGAGEMENT:
				engage();
				break;
			case INDEX_STAGE_CONCLUSION:
				conclude();
				break;
		}
	}
	
	// progresses the stage in a way that depends on what stage the battle is in
	private void progressStage(int amount){
		stageProgression += amount;
		if (stageIsPreparation()){
			if (stageProgression == stageEnd){
				preparationCharacter = getNextPreparationCharacter();
				if (preparationCharacter == getAmountOfAvailablePreparationCharacters()
						|| preparationCharacter == -1) 
					advanceStage();
				else { 
					stageProgression = 1;
					battleMenuItem = 0;
					battleSubItem = 0;
					setUpCombatInventory();
				}
			}
		} else if (stageIsEngagement()){
			if (stageProgression == stageEnd) {
				// check if monsters or team is dead
				boolean victory = true;
				boolean defeat = true;
				for (Monster monster: monsters) if (monster.HP != 0) victory = false;
				for (int i=0; i<Core.team.size();i++) if (Core.team.get(i).HP != 0) defeat = false;
				if (victory || defeat)
					advanceStage();
				else {
					preparationCharacter = getFirstPreparationCharacter();
					battleMenuItem = 0;
					battleSubPage = 0;
					battleSubItem = 0;
					combatantIndex = 0;
					for (int i = 0; i < Core.team.size(); i++)
						Core.team.get(i).move = null;
					setStage(INDEX_STAGE_PREPARATION);
				}
			}
		} else if (stageProgression == stageEnd) advanceStage();
	}
	
	// retrogresses through current stage
	public void retrogressStage(){
		if (!stageIsPreparation()) return;
		if (stageProgression == 0) return;
		
		// going back from PICK TARGET state
		if (stageProgression == 3) {
			Core.team.get(preparationCharacter).move = null;
			Core.team.get(preparationCharacter).usedItem = null;

			if (getChosenItem() == 0){
				stageProgression = 1;
				return;
			} else if (getChosenItem() == 2){
				stageProgression = 2;
				return;
			//} else if (getChosenItem() == 3){
			} else if (getChosenItem() == 4){
				stageProgression = 2;
				return;
			}
		}
		// going back from PICK SPELL / ITEM state
		else if (stageProgression ==2){
			stageProgression--;
			battleSubItem = 0;
			battleSubPage = 0;
		}
		// going back from PICK MOVE state
		else if (stageProgression == 1 && preparationCharacter != getFirstPreparationCharacter()){
			preparationCharacter = getPreviousPreparationCharacter();
			battleMenuItem = 0;
			setUpCombatInventory();
			return;
		}
		// going back from PICK MOVE as first character
		else stageProgression -= 1;
		
		if (stageProgression < 0) stageProgression =0;
		
		if (stageProgression !=0) battleMenuItem = getChosenItem();
		else battleMenuItem = 0;
	}
	
	// returns the first available character. returns -1 if none can move
	private int getFirstPreparationCharacter(){
		int index = 0;
		for (int i = index; i < Core.team.size(); i++){
			if (Core.team.get(i).HP != 0 && 
					!Condition.containsCondition(Core.team.get(i).getConditions(),
							Condition.INDEX_STUN))
				return i;
		}
		return -1;
	}
	
	// returns the next available character. returns -1 if none found
	private int getNextPreparationCharacter(){
		int index = preparationCharacter + 1;
		for (int i = index; i < Core.team.size(); i++){
			if (Core.team.get(i).HP != 0 && 
					!Condition.containsCondition(Core.team.get(i).getConditions(),
							Condition.INDEX_STUN))
				return i;
		}
		return -1;
	}
	
	// returns the previous available character. returns -1 if none found
	private int getPreviousPreparationCharacter(){
		int index = preparationCharacter - 1;
		for (int i = index; i >= 0; i--){
			if (Core.team.get(i).HP != 0 && 
					!Condition.containsCondition(Core.team.get(i).getConditions(),
							Condition.INDEX_STUN))
				return i;
		}
		return -1;
	}
	
	// returns the amount of available characters. returns 0 if none can move
	private int getAmountOfAvailablePreparationCharacters(){
		int amount = 0;
		for (PlayerCharacter pc : Core.team){
			if (pc.HP != 0 && 
					!Condition.containsCondition(pc.getConditions(),
							Condition.INDEX_STUN))
				amount++;
		}
		return amount;
	}
	
	// returns the item in the battle menu that was chosen to progress said menu
	private int getChosenItem(){
		return chosenBattleMenuItem;
	}
	
	// returns whether battle is in preparation stage
	public boolean stageIsPreparation(){
		return stageIndex == INDEX_STAGE_PREPARATION;
	}
	
	// returns whether battle is in engagement stage
	public boolean stageIsEngagement(){
		return stageIndex == INDEX_STAGE_ENGAGEMENT;
	}
	
	// sets variables for new stage
	private void setStage(int newStageIndex){
		stageIndex = newStageIndex;
		stageEnd = getStageEnd();
		stageProgression = 0;
		if (stageIsPreparation()) {
			// checks if team can act
			if (getAmountOfAvailablePreparationCharacters() != 0)
				setUpCombatInventory();
			else advanceStage();
		} else if (stageIsEngagement()) {
			prepareEngagement();
			engage();
		} else if (stageIndex == INDEX_STAGE_CONCLUSION){
			prepareConclusion();
			//conclude();
		}
	}
	
	// advances to next stage
	public void advanceStage(){
		setStage(stageIndex+1);
	}

	// calculates the end state of current battle stage
	private int getStageEnd(){
		switch(stageIndex){
			case INDEX_STAGE_INTRODUCTION:
				analyzeMonsterTypes();
				return monsterIDs.size();
			case INDEX_STAGE_PREPARATION:
				return 4;
			case INDEX_STAGE_ENGAGEMENT:
				return 1;
			case INDEX_STAGE_CONCLUSION:
				break;
		}
		return -1;
	}
	
	// prepares monsters for battle by setting up names and amounts
	private void analyzeMonsterTypes(){
		monsterIDs = new ArrayList<Integer>();
		monsterAmounts = new ArrayList<Integer>();
		
		for (Monster monster : monsters) {
			if (!monsterIDs.contains(monster.monsterID)){
				monsterIDs.add(monster.monsterID);
				monsterAmounts.add(1);
			} else {
				int indexID = monsterIDs.indexOf(monster.monsterID);
				monsterAmounts.set(indexID, monsterAmounts.get(indexID)+1);
				
				// adds # to name of monster
				monster.name = monster.name + " " + Integer.toString(monsterAmounts.get(indexID));
				
				// also adds it to first one if that has not yet been done
				if (monsters.get(indexID).name.equals(MonsterManager.getName(monsters.get(indexID).monsterID)))
					monsters.get(indexID).name = monsters.get(indexID).name + " 1";
			}
		}
	}

	// sets up the engagement stage
	private void prepareEngagement(){
		
		// creates list of combined monsters and team members, or empties
		if (combatants != null)	combatants.clear();
		else combatants = new ArrayList<Combatant>();
		
		// adds monsters and team members to list, if they are alive
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).HP == 0) continue;
			combatants.add(monsters.get(i));
		}
		for (int i = 0; i < Core.team.size(); i++) {
			if (Core.team.get(i).HP == 0) continue;
			combatants.add(Core.team.get(i));
		}

		// set up moves for monsters
		for (Monster monster : monsters) 
			monster.move = AI.getMove(monster.monsterID);

		// TODO : possibly some moves have higher priority
		// in that case before sorting add to the combatant object 's speed
		// some sort of added priority (AbilityManager.getSpeedModifier(ID))
		// this will automatically change the speed of combatant without
		// permanently changing speed of monster or team member
		// TODO also add defense if they are defending?
		
		// sorts list with highest speed coming first
		Collections.sort(combatants, Collections.reverseOrder(new SpeedComparator()));
	}
	
	// performs an action in the engagement stage
	private void engage(){
		
		// if not finished with all combatants
		if (combatantIndex < combatants.size()) {
			
			engagementShowMana = false;
		
			// if not yet done with current combatant
			if (!finishedCombatantMove) {
				
				// if a move is not yet being handled, set it up
				if (moveResult == null){
					
					Combatant combatant = combatants.get(combatantIndex);
					
					// make sure combatant isn't dead or immobile
					if (combatant.getHP() == 0
							|| Condition.containsCondition(combatant.getConditions(), 
									Condition.INDEX_STUN)){
						finishedCombatantMove = true;
						engage();
						
					// if alive, set up combat message and applicable move result
					} else {
						Ability move = combatant.getMove();
						
						// remove HP or MP because of energy cost
						combatant.damageHP(move.HPCost);
						combatant.damageMP(move.MPCost);
						
						// remove used item
						if (combatant.getUsedItem() != null
								&& combatant.getUsedItem().isMisc()) {
							combatant.getUsedItem().quantity--;
						}
						
						// setup message depending on type of move
						if (move.isDefend()) {
							String message = combatant.getName() + " is defending!";
							combatMessage = message;
							finishedCombatantMove = true;
							
						// if not defending, move has result, so set it up
						} else {
							moveResult = new MoveResult(combatant, monsters, Core.team);
							combatMessage = moveResult.progressAndGetMessage();
							
							// extra check for if attacker was standing by
							if (moveResult.isStandingBy) finishedCombatantMove = true;
						}
					}
				
				// otherwise continue handling a move
				} else {
					String message = moveResult.progressAndGetMessage();
					
					// if get a message, attempt to continue as normal
					if (message != null){
						combatMessage = message;
						engagementShowMana = moveResult.getShowMana();
						
					// if no longer get a message, finish up
					} else {
						finishedCombatantMove = true;
						
						// since no message, call this method to try to get one
						engage();
					}
				}
			// if done with combatant move
			} else {
				moveResult = null;
				combatantIndex++;
				finishedCombatantMove = false;
				
				// since no message, call this method to try to get one
				engage();
			}
		// if done with all combatants
		} else {
			
			engagementShowMana = true;
			if (turnResult == null) turnResult = new TurnResult(Core.team, monsters);				
			String message = turnResult.progressAndGetMessage();
			if (message != null)
				combatMessage = message;
			else {
				turnResult = null;
				progressStage(1);
			}
		}
	}
	
	// sets up conclusion stage
	private void prepareConclusion(){
		succesfulBattle = false;
		for (PlayerCharacter pc : Core.team) if (pc.HP != 0) succesfulBattle = true;

		conclusionMessages = new ArrayList<String>();
		
		if (succesfulBattle){
			
			int totalExp =0, totalMoney = 0;
			ArrayList<Item> loot = new ArrayList<Item>();

			for (Monster monster : monsters){
				totalExp += monster.experience;
				totalMoney += monster.money;
				
				// if monster drops item, and RNG decides an item will be dropped
				if (monster.itemDropID != -1 &&
						Core.random.nextDouble() > monster.itemDropChance){
					loot.add(new Item(monster.itemDropID));
				}
			}
			
			for (PlayerCharacter pc : Core.team)
				if (pc.HP != 0) {
					String message = pc.name + " gained " 
							+ Integer.toString(totalExp) + " experience.";
					conclusionMessages.add(message);
					
					// if (pc.gainedNewLevel ?? )
					// set new statistics. possibly in ^
					// message = " pc.name + " has reached level " + level + ".";
					// conclusionMessages.add(message);
					
					// if (pc.learnedSpell)
					// pc.spellbook.add(new spell ( spellID) <- possibly in ^
					// message = " pc.name + " has learned " + spell.name + ".";
					// conclusionMessages.add(message);	
				}
			
			if (totalMoney > 0) {
				String message = Integer.toString(totalMoney) + " " 
						+ Core.MONEYNAME + " was found.";
				conclusionMessages.add(message);
			}
				
			while (!loot.isEmpty()){
				// check from battle position 0 to party size if there is space
				if (Core.team.get(0).inventory.size() < PlayerCharacter.INVENTORY_CAP){
					Core.team.get(0).inventory.add(loot.get(0));
					String message = Core.team.get(0).name + " found " 
							+ loot.get(0).name + ".";
					conclusionMessages.add(message);
					loot.remove(0);
				} else if (Core.team.size() > 1 
						&& Core.team.get(1).inventory.size() < PlayerCharacter.INVENTORY_CAP) {
					Core.team.get(1).inventory.add(loot.get(0));
					String message = Core.team.get(1).name + " found " 
							+ loot.get(0).name + ".";
					conclusionMessages.add(message);
					loot.remove(0);
				} else if (Core.team.size() > 2 
						&& Core.team.get(2).inventory.size() < PlayerCharacter.INVENTORY_CAP) {
					Core.team.get(2).inventory.add(loot.get(0));
					String message = Core.team.get(2).name + " found " 
							+ loot.get(0).name + ".";
					conclusionMessages.add(message);
					loot.remove(0);
				} else if (Core.team.size() > 3 
						&& Core.team.get(3).inventory.size() < PlayerCharacter.INVENTORY_CAP) {
					Core.team.get(3).inventory.add(loot.get(0));
					String message = Core.team.get(3).name + " found " 
							+ loot.get(0).name + ".";
					conclusionMessages.add(message);
					loot.remove(0);
				} else if (Core.team.size() > 4 
						&& Core.team.get(4).inventory.size() < PlayerCharacter.INVENTORY_CAP) {
					Core.team.get(4).inventory.add(loot.get(0));
					String message = Core.team.get(4).name + " found " 
							+ loot.get(0).name + ".";
					conclusionMessages.add(message);
					loot.remove(0);
				
				// if no one has space in inventory, don't add item
				} else {
					break;
				}
			}
			
		} //for (String s : conclusionMessages) System.err.println(s);
	}
	
	//
	private void conclude(){		
		// if beat the monsters, get message. if no messages, end battle
		if (succesfulBattle) {
			if (!conclusionMessages.isEmpty()) {
				conclusionMessages.remove(0);
			}
			if (conclusionMessages.isEmpty()) Core.endBattle();
			
		// if defeat, GAME OVER
		} else {
			// to title screen TODO
		}
	}
	
	//
	public void cycleRightThroughMenu(){
		if (!justCycledRight && stageIndex == 1){
			if (stageProgression == 2){
				cycleRightThroughSub();
			} else if (stageProgression != 3){
				if (++battleMenuItem >= getAmountOfMenuItems()) battleMenuItem = 0;
			} else {
				if (++battleMenuTarget >= getAmountOfMenuItems()) battleMenuTarget = 0;
			}
			stopSelectionAnimation();
			justCycledRight = true;
		}
	}
	
	//
	public void cycleLeftThroughMenu(){
		if (!justCycledLeft && stageIndex == 1){
			if (stageProgression == 2){
				cycleLeftThroughSub();
			} else if (stageProgression != 3){
				if (--battleMenuItem < 0) battleMenuItem = getAmountOfMenuItems()-1;
			} else{
				if (--battleMenuTarget < 0) battleMenuTarget = getAmountOfMenuItems()-1;
			}
			stopSelectionAnimation();
			justCycledLeft = true;
		}
	}
	
	//
	private void cycleRightThroughSub(){
		if (++battleSubPage > getAmountOfSubPages()-1) battleSubPage = 0;
		if (battleSubPage == getAmountOfSubPages()-1) {
			if (subMenuIndex == INDEX_SUB_SPELLBOOK) {
				if (battleSubItem >= 
					Core.team.get(preparationCharacter).spellbook.size() % 5)
						battleSubItem = 0;
			} else if (subMenuIndex == INDEX_SUB_INVENTORY) {
				if (battleSubItem >= combatInventory.size() % 5) battleSubItem = 0;
			} else {
				// chain menu
			}
		}
	}	
	
	//
	private void cycleLeftThroughSub(){
		if (--battleSubPage < 0) battleSubPage = getAmountOfSubPages() -1;
		if (battleSubPage == getAmountOfSubPages() -1) {
			if (subMenuIndex == INDEX_SUB_SPELLBOOK) {
				if (battleSubItem >= 
					Core.team.get(preparationCharacter).spellbook.size() % 5)
						battleSubItem = 0;
			} else if (subMenuIndex == INDEX_SUB_INVENTORY) {
				if (battleSubItem >= combatInventory.size() % 5) battleSubItem = 0;
			} else {
				// chain menu
			}
		}
	}
	
	//
	public void cycleUpThroughSub(){
		if (!justCycledUp && stageIndex == 1 && stageProgression == 2){
			
			int menuItemAmount = getAmountOfMenuItems();
			if (menuItemAmount == 0) return;
			
			if (--battleSubItem < 0) battleSubItem = menuItemAmount-1;

			stopSelectionAnimation();
			justCycledUp = true;
		}
	}
	
	//
	public void cycleDownThroughSub(){
		if (!justCycledDown && stageIndex == 1 && stageProgression == 2){
			
			int menuItemAmount = getAmountOfMenuItems();
			if (menuItemAmount == 0) return;
			
			if (++battleSubItem >= menuItemAmount) battleSubItem = 0;

			stopSelectionAnimation();
			justCycledDown = true;
		}
	}
	
	// creates a list of items that are usable in combat in a specific order
	private void setUpCombatInventory(){
		
		// change for specific given order perhaps ? TODO
		// ^ possibly an order for misc first then weapons:
		//   would be first for loop of category misc, then for loop of weapons
		
		// clears any existing list and creates new one
		if (combatInventory != null) combatInventory.clear();
		combatInventory = new ArrayList<Item>();
		
		// removes used item if it now has a quantity of 0
		if (Core.team.get(preparationCharacter).usedItem != null
				&& Core.team.get(preparationCharacter).usedItem.quantity == 0)
			Core.team.get(preparationCharacter).inventory.remove(Core.team.get(preparationCharacter).usedItem);
		
		Core.team.get(preparationCharacter).usedItem = null;
		
		// adds all combat items in regular (ID) order to list
		for (Item item : Core.team.get(preparationCharacter).inventory){
			if (item.usableInCombat) combatInventory.add(item); 	
		}
	}
	
	// sets the initial target to be the middle of the targeted team
	private void setInitialTarget(){
		// amount of targets depends on who is targeted (foe or team)
		int amountOfTargets;
		if (Core.team.get(preparationCharacter).move.targetIsFoe)
			amountOfTargets = monsters.size();
		else
			amountOfTargets = Core.team.size();
		
		// reach from move is needed for further calculation
		int reach = Core.team.get(preparationCharacter).move.reach;

		// if casting on team and not multi reach, start on self, except if status
		if (!Core.team.get(preparationCharacter).move.targetIsFoe) {
			if (reach == 0){
				if (Core.team.get(preparationCharacter).move.targetIsDead)
					battleMenuTarget = findDeadAllyTarget();
				else if (Core.team.get(preparationCharacter).move.statusIndexRestore != -1)
					battleMenuTarget = findAfflictedAllyTarget();
				else
					battleMenuTarget = preparationCharacter;
				return;
			}
		}
		// start RIGHT TWO on the left if only three or less targets
		if (reach == 5 && amountOfTargets <= 3){
			battleMenuTarget = 0;
			return;
		}
		// start RIGHT FOUR all the way to the left
		if (reach == 6){
			battleMenuTarget = 0;
			return;
		}
		// start LEFT TWO on the right if only four or less targets
		if (reach == 7 && amountOfTargets <= 4){
			battleMenuTarget = amountOfTargets -1;
			
			// start less far on right to get closer to center if exactly four
			if (amountOfTargets == 4) battleMenuTarget--;
			return;
		}
		// start LEFT FOUR all the way to the right
		if (reach == 8){
			battleMenuTarget = amountOfTargets - 1;
			return;	
		}
		
		// all other cases, start at center-most living target
		battleMenuTarget = findLivingTarget();
	}
	
	// returns the index of a living target, for easy targeting, ideally in center
	private int findLivingTarget(){
		int amountOfTargets, targetIndex;
		if (Core.team.get(preparationCharacter).move.targetIsFoe)
			amountOfTargets = monsters.size();
		else
			amountOfTargets = Core.team.size();
		
		if (amountOfTargets %2 == 1) targetIndex = amountOfTargets /2;
		else targetIndex = amountOfTargets / 2 -1;
		
		if (Core.team.get(preparationCharacter).move.targetIsFoe) {
			if (monsters.get(targetIndex).HP != 0) return targetIndex;
			else {
				do {
					targetIndex++;
					if (targetIndex >= monsters.size()) targetIndex = 0;
				} while (monsters.get(targetIndex).HP == 0);
				return targetIndex;
			}
		} else {
			if (Core.team.get(targetIndex).HP != 0) return targetIndex;
			else {
				do {
					targetIndex++;
					if (targetIndex >= Core.team.size()) targetIndex = 0;
				} while (Core.team.get(targetIndex).HP == 0);
				return targetIndex;
			}
		}
	}
	
	// returns the index in the team of a dead ally, for easy revive-targeting
	private int findDeadAllyTarget(){
		for (PlayerCharacter character : Core.team){
			if (character.HP == 0) return Core.team.indexOf(character); 
		}
		// if no one found, return self
		return preparationCharacter;
	}
	
	// returns the index in the team of a status afflicted ally, for easy targeting
	private int findAfflictedAllyTarget(){
		for (PlayerCharacter character : Core.team){
			if (Condition.containsCondition(character.getConditions(),
					Core.team.get(preparationCharacter).move.statusIndexRestore))
				return Core.team.indexOf(character);
		}
		// if no one found, return self
		return preparationCharacter;
	}
	
	// returns whether character has enough health or MANA for specific move
	public boolean hasSufficientEnergy(int index){
		return ((Core.team.get(preparationCharacter).MP 
					>= AbilityManager.getAbilityMPCost(Core.team.get(preparationCharacter).spellbook.get(index).ID))
						&& (Core.team.get(preparationCharacter).HP 
								> AbilityManager.getAbilityHPCost(Core.team.get(preparationCharacter).spellbook.get(index).ID)));
	}
	
	//
	public int getAmountOfMenuItems(){
		switch (stageProgression){
			case 0: return 2;
			case 1: return 5;
			case 2:
				if (subMenuIndex == INDEX_SUB_SPELLBOOK){ 
					if (battleSubPage == getAmountOfSubPages()-1 
							&& Core.team.get(preparationCharacter).spellbook.size() % 5 != 0){
						return Core.team.get(preparationCharacter).spellbook.size() % 5;
					} else if (!Core.team.get(preparationCharacter).spellbook.isEmpty())
						return 5;
					else return 0;
				} else if (subMenuIndex == INDEX_SUB_INVENTORY) {
					if (battleSubPage == getAmountOfSubPages()-1
							&& combatInventory.size() %5 != 0){
						return combatInventory.size() %5;
					} else if (!combatInventory.isEmpty())
						return 5;
					else return 0;
				} else {
					
				}
			case 3: 
				if (Core.team.get(preparationCharacter).move.targetIsFoe) return monsters.size();
				else return Core.team.size();
			default: return 0;
		}
	}
	
	//
	public int getAmountOfSubColumns(){
		if (subMenuIndex == INDEX_SUB_SPELLBOOK) return 1;
		else if (subMenuIndex == INDEX_SUB_INVENTORY) return 1;
		else return 1;
	}

	// returns text to display at bottom screen. replaced by menu in PREP stage.
	public String getBattleText(){
		
		switch(stageIndex){
			case INDEX_STAGE_INTRODUCTION:
				String introString = "You encounter ";
				if (monsterAmounts.get(stageProgression) == 1)
					introString += "a ";
				introString += MonsterManager.getName(monsterIDs.get(stageProgression));
				if (monsterAmounts.get(stageProgression) != 1) 
					introString += " x" + Integer.toString(monsterAmounts.get(stageProgression));
				introString += "!";
				return introString;
			case INDEX_STAGE_PREPARATION:
				return null;
			case INDEX_STAGE_ENGAGEMENT:
				return combatMessage;
			case INDEX_STAGE_CONCLUSION:
				if (conclusionMessages != null && !conclusionMessages.isEmpty())
					return conclusionMessages.get(0);
				else return null;
		}
		
		return null;
	}
	
	// returns text to display in bottom left panel
	public String getBattleMenuText(){
		switch(stageProgression){
			case 0: 
				switch (battleMenuItem){
					case 0: return "Fight";
					case 1: return "Flee";
				}
			case 1:
				switch (battleMenuItem){
					case 0: return "Attack";
					case 1: return "Defend";
					case 2: return "Cast";
					case 3: return "Chain";
					case 4: return "Items";
				}
			case 2:
			case 3:
			case 4:
				switch (getChosenItem()){
					case 0: return "Attack";
					case 1: return "Defend";
					case 2: return "Cast";
					case 3: return "Chain";
					case 4: return "Items";
				}
		}
		return null;
	}
	
	// returns index adjusted towards what page is being browsed
	public int getRealSubIndex(int index){
		return index + (battleSubPage * getAmountOfSubColumns()*5);
	}

	// returns text to display on sub menu item
	public String getSubMenuText(int index){
		if (subMenuIndex == INDEX_SUB_SPELLBOOK){
			return Core.team.get(preparationCharacter).spellbook.get(index).name;
		} else if (subMenuIndex == INDEX_SUB_INVENTORY)  {
			return combatInventory.get(index).name;
		} else {
			
		}
		return null;
	}
	
	// returns image to display for sub menu item
	public BufferedImage getSubMenuIcon(int index){
		if (subMenuIndex == INDEX_SUB_SPELLBOOK){	
			return Core.team.get(preparationCharacter).spellbook.get(index).icon;
		} else if (subMenuIndex == INDEX_SUB_INVENTORY)  {
			return combatInventory.get(index).icon;
		} else {
			return null;
		}
	}
	
	// returns amount of pages in sub menu
	public int getAmountOfSubPages(){
		int amountOfItems;
		if (subMenuIndex == INDEX_SUB_SPELLBOOK) 
			amountOfItems = Core.team.get(preparationCharacter).spellbook.size();
		else if (subMenuIndex == INDEX_SUB_INVENTORY) 
			amountOfItems = combatInventory.size();
		else amountOfItems = 0; // chain
		
		if (amountOfItems == 0) return 1;
		
		int amountOfPages;
		if (amountOfItems % (getAmountOfSubColumns()*5) == 0)
			amountOfPages = amountOfItems / (getAmountOfSubColumns()*5);
		else amountOfPages = amountOfItems / (getAmountOfSubColumns()*5) +1;
		
		return amountOfPages;
	}
		
	
	// returns the main part of the battle menu
	public BufferedImage getMainBattleMenu(){
		switch (stageProgression) {
			case 0: return GraphicsManager.battleMenu1;
			default: return GraphicsManager.battleMenu2;
		}
	}

	// returns icon to display for choosing type of sub menu
	public BufferedImage getBattleMenuIcon(int index){
		switch (stageProgression){
			case 0:
				switch (index){
					case 0: return GraphicsManager.battleMenuIcon1;
					case 1: return GraphicsManager.battleMenuIcon2;
				}
			case 1:
				switch (index){
					
				}
		}
		return null;
	}
	
	// returns the avatar of the current character
	public BufferedImage getBattleMenuAvatar(){
		if (stageProgression == 0) return null;
		
		else return Core.team.get(preparationCharacter).avatar;
	}
	
	// returns image of tool-tip when it is needed
	public BufferedImage getBattleMenuTooltip(){
		
		switch (stageProgression){
			case 3:  if (getChosenItem() == 0) return null;
			case 2:
				return GraphicsManager.battleMenu6;
			default: return null;
		}
	}
	
	// returns text for the battle menu tool-tip
	public String getBattleMenuTooltipText(){
		// in the sub menu, description is given
		if (stageProgression == 2) {
			if (subMenuIndex == INDEX_SUB_SPELLBOOK){
				if (!Core.team.get(preparationCharacter).spellbook.isEmpty())
					return Core.team.get(preparationCharacter).spellbook.get(getRealSubIndex(battleSubItem)).description;
			} else if (subMenuIndex == INDEX_SUB_INVENTORY){
				if (!combatInventory.isEmpty())
					return combatInventory.get(getRealSubIndex(battleSubItem)).description;
			} else return null;
			
		// if targeting, summary is given, so this returns just name
		} else {
			if (subMenuIndex == INDEX_SUB_SPELLBOOK)
				return Core.team.get(preparationCharacter).move.name;
			else if (subMenuIndex == INDEX_SUB_INVENTORY)
				return combatInventory.get(getRealSubIndex(battleSubItem)).name;
			else return null;
		}
		return null;
	}
	
	// returns fitting x coordinate for the start of tool-tip text
	public int getBattleMenuTooltipTextX(){
		switch (stageProgression){
			case 2: return (int)(65 * Core.multiplier);
			case 3: return (int)(82 * Core.multiplier);
			default: return 0;
		}
	}
	
	// returns an icon for the tool-tip
	public BufferedImage getBattleMenuTooltipIcon(){
		if (stageProgression != 3) return null;
		if (getChosenItem() == 2) return Core.team.get(preparationCharacter).move.icon;
		if (getChosenItem() == 4) return combatInventory.get(getRealSubIndex(battleSubItem)).icon;
		return null;
	}
	
	// returns cost for the tool-tip
	public String getBattleMenuTooltipCost(){
		if (stageProgression != 3) return null;
		if (subMenuIndex != INDEX_SUB_SPELLBOOK) return null;
		int hp = Core.team.get(preparationCharacter).spellbook.get(getRealSubIndex(battleSubItem)).HPCost;
		int mp = Core.team.get(preparationCharacter).spellbook.get(getRealSubIndex(battleSubItem)).MPCost;
		
		if (hp > 0) return hp + Core.getHealthName();
		else return mp + Core.getManaName();
	}
	
	// returns image of type of sub menu
	public BufferedImage getBattleMenuSub(){
		
		switch (stageProgression){
			case 2:
				if (subMenuIndex == INDEX_SUB_SPELLBOOK)	return GraphicsManager.battleMenu5;
				else if (subMenuIndex == INDEX_SUB_INVENTORY)  return GraphicsManager.battleMenu5;
				//else return chain menu
			default: return null;
		}
		
	}
	
	// returns the fade out image for menu icons
	public BufferedImage getBattleMenuFadeOut(){
		switch (stageProgression){
			case 1:
			case 2:
			case 3:return GraphicsManager.battleMenuFadeOut1;
			default: return null;
		}
	}
	
	// returns whether a battle menu item is locked
	public boolean battleMenuItemIsLocked(int battleMenuItemIndex){
		// during stage 1 checks for conditions
		if (stageProgression == 1) {
			switch (battleMenuItemIndex){
				case 0:
					if (Condition.containsCondition(Core.team.get(preparationCharacter).conditions, Condition.INDEX_DISARM))
						return true;
					else break;
				case 1:
					break;
				case 2:
					if (Condition.containsCondition(Core.team.get(preparationCharacter).conditions, Condition.INDEX_LOCKSPELL))
						return true;
					else break;
				case 3:
//					if (Condition.containsCondition(team.get(preparationCharacter).conditions, Condition.INDEX_LOCKSPELL))
//						return true;
//					else break;
					return true; // TODO check of chain is unlocked ?
				case 4:
					if (Condition.containsCondition(Core.team.get(preparationCharacter).conditions, Condition.INDEX_LOCKITEM))
						return true;
					else break;
			}
			return false;
		// during stage 2 and 3 checks which battle menu item is not being used
		} else {
			if (getChosenItem() != battleMenuItemIndex)
				return true;
			else return false;
		}
	}
	
	// returns whether the conditions of combatants should be shown
	public boolean showConditions(){
		return stageIndex == INDEX_STAGE_PREPARATION;
	}
	
	// returns whether an enlarged character bar that displays MANA will be shown
	public boolean useLargerCharacterBar(){		
		switch (stageIndex){
			case INDEX_STAGE_INTRODUCTION:
				return false;
			case INDEX_STAGE_PREPARATION:
				switch (stageProgression){
					case 0: return false;
					default: return true;
				}	
			case INDEX_STAGE_ENGAGEMENT:
				return engagementShowMana;
			case INDEX_STAGE_CONCLUSION:
				return true;
			default: return false;
		}
	}	
	
	// returns a character bar of which the size depends on necessity
	public BufferedImage getBattleMenuCharacterBar(){
		if (stageIndex < INDEX_STAGE_INTRODUCTION) return null;
		if (useLargerCharacterBar()) return GraphicsManager.battleMenu3;
		else return GraphicsManager.battleMenu7;

	}
	
	// returns an extra bar to display chain buildup
	public BufferedImage getBattleMenuExtraBar(){
		// if chain unlocked? story manager? else return null?
		// if minimum 1 chain open? else return null?
		switch (stageProgression){
			case 2:
			case 3: return GraphicsManager.battleMenuFadeOut1;
			default: return null;
		}
	}
	
	// returns a panel that displays the page number of the sub menu
	public BufferedImage getBattleMenuSubPagePanel(){
		if (stageProgression != 2) return null;
		if (getAmountOfSubPages() == 1) return null;
		return GraphicsManager.battleMenu8;
	}
	
	// returns the cost of an ability shown in a sub-menu item
	public String getBattleMenuSubCost(int index){
		if (subMenuIndex != INDEX_SUB_SPELLBOOK) return null;
		int hp = Core.team.get(preparationCharacter).spellbook.get(index).HPCost;
		int mp = Core.team.get(preparationCharacter).spellbook.get(index).MPCost;
		
		if (hp > 0) return Integer.toString(hp) + Core.getHealthName();
		else return Integer.toString(mp) + Core.getManaName();
	}
	
	// returns the reach of an ability shown in a sub-menu item
	public BufferedImage getBattleMenuSubItemReachIcon(int index){
		if (subMenuIndex != INDEX_SUB_SPELLBOOK) return null;
		switch (Core.team.get(preparationCharacter).spellbook.get(index).reach){
			case 0: return GraphicsManager.reachSingle;
			case 1: return GraphicsManager.reachDimThree;
			case 2: return GraphicsManager.reachDimFive;
			case 3: return GraphicsManager.reachPureThree;
			case 4: return GraphicsManager.reachPureFive;
			case 5: return GraphicsManager.reachRightTwo;
			case 6: return GraphicsManager.reachRightFour;
			case 7: return GraphicsManager.reachLeftTwo;
			case 8: return GraphicsManager.reachLeftFour;

			default: return null;
		}
	}
	
	// returns the quantity of a type of item shown in a sub-menu item
	public String getBattleMenuSubItemQuantity(int index){
		if (subMenuIndex != INDEX_SUB_INVENTORY) return null;
		if (combatInventory.get(index).typeIndex != ItemManager.INDEX_MISCELLANEOUS) return null;
		
		return Integer.toString(combatInventory.get(index).quantity);
	}
	
	
	// returns a fade image for spells for which there is insufficient energy
	public BufferedImage getBattleMenuSubItemFade(int index){
		if (subMenuIndex != INDEX_SUB_SPELLBOOK) return null;
		
		if (!hasSufficientEnergy(index)) return GraphicsManager.battleMenuFadeOut2;
		else return null;
	}

	// returns the amount of side pointers available
	public int getSidePointerAmount(boolean sideIsLeft){
		if (Core.team.get(preparationCharacter).move == null) return 0;
		
		int amount = 0;
		int targets;
		if (Core.team.get(preparationCharacter).move.targetIsFoe)
			targets = monsters.size();
		else targets = Core.team.size();
		
		int reach = Core.team.get(preparationCharacter).move.reach;
		
		if (sideIsLeft){
			switch (reach){
				case AbilityManager.INDEX_REACH_SINGLE_TARGET:
				case AbilityManager.INDEX_REACH_RIGHT_TWO:	
				case AbilityManager.INDEX_REACH_RIGHT_FOUR:
					break;
				case AbilityManager.INDEX_REACH_DIMINISHING_THREE:	
				case AbilityManager.INDEX_REACH_FULL_THREE:
					if (battleMenuTarget > 0) amount++;
					break;
				case AbilityManager.INDEX_REACH_DIMINISHING_FIVE:	
				case AbilityManager.INDEX_REACH_FULL_FIVE:
					if (battleMenuTarget > 0) amount++;
					if (battleMenuTarget > 1) amount++;
					break;
				case AbilityManager.INDEX_REACH_LEFT_TWO:
					if (battleMenuTarget > 0) amount++;
					if (battleMenuTarget > 1) amount++;
					break;
				case AbilityManager.INDEX_REACH_LEFT_FOUR:
					if (battleMenuTarget > 0) amount++;
					if (battleMenuTarget > 1) amount++;
					if (battleMenuTarget > 2) amount++;
					if (battleMenuTarget > 3) amount++;
					break;
			}
		} else {
			switch (reach){
				case AbilityManager.INDEX_REACH_SINGLE_TARGET:
				case AbilityManager.INDEX_REACH_LEFT_TWO:	
				case AbilityManager.INDEX_REACH_LEFT_FOUR:
					break;
				case AbilityManager.INDEX_REACH_DIMINISHING_THREE:	
				case AbilityManager.INDEX_REACH_FULL_THREE:
					if (battleMenuTarget < targets-1) amount++;
					break;
				case AbilityManager.INDEX_REACH_DIMINISHING_FIVE:	
				case AbilityManager.INDEX_REACH_FULL_FIVE:
					if (battleMenuTarget < targets-1) amount++;
					if (battleMenuTarget < targets-2) amount++;
					break;
				case AbilityManager.INDEX_REACH_RIGHT_TWO:
					if (battleMenuTarget < targets-1) amount++;
					if (battleMenuTarget < targets-2) amount++;
					break;
				case AbilityManager.INDEX_REACH_RIGHT_FOUR:
					if (battleMenuTarget < targets-1) amount++;
					if (battleMenuTarget < targets-2) amount++;
					if (battleMenuTarget < targets-3) amount++;
					if (battleMenuTarget < targets-4) amount++;
					break;
			}
		}
		
		return amount;
	}
	
	// returns the x coordinate of the side pointer
	public int getSidePointerX(int mainPointerX, int index, boolean sideIsLeft){
		// index starts at 0. must start at 1 for actual math
		index++;
		// offset size should ideally be image_width of target 
		int offsetSize = (int)(32 * Core.multiplier);
		
		// if left side, use negative math to reverse order
		if (sideIsLeft) offsetSize *= -1;
		return mainPointerX + index*offsetSize;
	}
	
	// returns whether the menu is in the targeting phase
	public boolean sidePointersAllowed(){
		return stageProgression == 3;
	}
	
	// returns the title to display above a targeting pointer
	public String getPointerTitle(){
		if (stageProgression != 3) return null;
		PlayerCharacter pc = Core.team.get(preparationCharacter);
		if (pc.move.targetIsFoe)
			return monsters.get(battleMenuTarget).name;
		else return Core.team.get(battleMenuTarget).name;
	}
	
	// returns stage index
	public int getStageIndex(){
		return stageIndex;
	}
	
	// returns stage state
	public int getStageProgression(){
		return stageProgression;
	}
	
	// returns x coordinate for displaying specified monster
	public int getMonsterX(int index){
		int foeAreaLeft = (int)(64 * Core.multiplier);
		int foeAreaSize = (int)(160 * Core.multiplier);
		
		int x;
		if (monsters.size() % 2 == 0)
			x = foeAreaLeft + foeAreaSize/2 - (int)((32 * monsters.size()/2 *Core.multiplier));
		else 
			x = foeAreaLeft + foeAreaSize/2 - (int)(32/2*Core.multiplier) - (int)((32 * (monsters.size()-1)/2 *Core.multiplier));

		x += (int)(index * 32 * Core.multiplier);
		return x;
	}
	
	// returns y coordinate for displaying specified monster
	public int getMonsterY(int index){
		
		int y = (int)(48 * Core.multiplier);
		y += (int)(index * 1 * Core.multiplier);
		
		return y;
	}
	
	// returns x coordinate for displaying specified team member
	public int getTeamX(int index){
		int teamAreaLeft = 0;
		int teamAreaSize = (int)(128 * Core.multiplier);
		
		int x;
		if (Core.team.size() % 2 == 0)
			x = teamAreaLeft + teamAreaSize/2 - (int)((32 * Core.team.size()/2 *Core.multiplier));
		else 
			x = teamAreaLeft + teamAreaSize/2 - (int)(32/2*Core.multiplier) - (int)((32 * (Core.team.size()-1)/2 *Core.multiplier));

		x += (int)(index * 32 * Core.multiplier);
		return x;
	}
	
	// returns y coordinate for displaying specified team member
	public int getTeamY(int index){
		
		int y = (int)(85 * Core.multiplier);
		y += (int)(index * 1 * Core.multiplier);

		return y;
	}
	
	// returns x coordinate for displaying the selection sprite
	public int getSelectionX(){
		int x;
		if (stageProgression != 2){
			
			x = (int)(62 * Core.multiplier);
			x += battleMenuItem * BATTLE_MENU_ITEM_SIZE * Core.multiplier;
			
		} else {
			x = (int)(66 * Core.multiplier);
		}
		
		return x;
	}
	
	//returns y coordinate for displaying the selection sprite
	public int getSelectionY(){
		int y;
		if (stageProgression != 2){
			y = (int)(158 * Core.multiplier);
		} else {
			y = (int)(73 * Core.multiplier);
			y += battleSubItem * BATTLE_SUB_ITEM_HEIGHT * Core.multiplier;
		}
		
		return y;
	}
	
	// returns a certain sprite for animating
	public BufferedImage getSelectionSprite(){
		if (stageProgression == 3) return null;
		if (cursorAnimator == null) return null;
		
		BufferedImage selector;
		
		if (stageProgression == 0 || stageProgression == 1)
			selector = GraphicsManager.battleMenuSelection1;
		else selector = GraphicsManager.battleMenuSelection2;

		switch(cursorAnimator.getAnimationIndex()){
			case 0: return selector;
			case 1: return null;
			// case 2: return enlarged ( selection1 ); ??
			default: return null;
		}
	}
	
	// returns x coordinate for displaying the pointer sprite
	public int getPointerX(){
		int x = 0;
		
		switch (stageProgression) {
			case 1:
			case 2:
				x = (int)((20 + preparationCharacter * 46) * Core.multiplier);
				break;
			case 3:
				
				if (Core.team.get(preparationCharacter).move.targetIsFoe){
					x = getMonsterX(battleMenuTarget);
					// monster image size - pointer size / 2 |TODO if big monster?
					x += (32 - 3) * Core.multiplier;
				} else {
					x = getTeamX(battleMenuTarget);
					// team member image size - pointer size / 2
					x += (32 - 3) * Core.multiplier;
				}
				break;
		}
		
		return x;
	}
	
	// returns y coordinate for displaying the pointer sprite
	public int getPointerY(){
		int y = 0;
		
		switch (stageProgression) {
			case 1:
			case 2:
				y = (int)(30 * Core.multiplier);
				break;
			case 3:
				if (Core.team.get(preparationCharacter).move.targetIsFoe){
					y = getMonsterY(battleMenuTarget);
				} else {
					y = getTeamY(battleMenuTarget);
				}
				break;
		}
		
		return y;
	}
	
	// returns an index for who or what is being pointed at
	public int getPointerTargetIndex(){
		return battleMenuTarget;
	}
	
	// returns a certain sprite for animating
	public BufferedImage getPointerSprite(){
		if (stageIndex != INDEX_STAGE_PREPARATION) return null;
		if (stageProgression == 0) return null;
		if (pointerAnimator == null) return null;
		
		BufferedImage pointer1, pointer2;
		
		if (stageProgression == 1 || stageProgression == 2) {
			pointer1 = GraphicsManager.battleMenuPointer1;
			pointer2 = GraphicsManager.battleMenuPointer2;
		} else {
			pointer1 = GraphicsManager.battleMenuPointer3;
			pointer2 = GraphicsManager.battleMenuPointer4;			
		}

		switch(pointerAnimator.getAnimationIndex()){
			case 0: return pointer1;
			case 1: return pointer2;
			// case 2: return enlarged ( selection1 ); ??
			default: return null;
		}
	}
	
	// creates animation threads and starts them
	public void startAnimation(){
		if (cursorAnimator == null){
			cursorAnimator = new Animator(AMOUNT_OF_BROWSE_ANIMATIONS, MENU_ANIMATION_INTERVAL);
			cursorAnimator.thread = new Thread(cursorAnimator);
			cursorAnimator.thread.start();
		}
		if (pointerAnimator == null){
			pointerAnimator = new Animator(AMOUNT_OF_BROWSE_ANIMATIONS, POINTER_ANIMATION_INTERVAL);
			pointerAnimator.thread = new Thread(pointerAnimator);
			pointerAnimator.thread.start();
		}
	}
	
	// destroys all animation threads
	public void stopAnimation(){
		stopSelectionAnimation();
		stopPointerAnimation();
	}
	
	//
	private void stopSelectionAnimation(){
		if (cursorAnimator != null){
			cursorAnimator.thread.interrupt();
			cursorAnimator = null;
		}
	}
	
	//
	private void stopPointerAnimation(){
		if (pointerAnimator != null){
			pointerAnimator.thread.interrupt();
			pointerAnimator = null;
		}
	}
}
