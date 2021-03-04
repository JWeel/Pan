package battle;

import foundation.Core;
import repository.AbilityManager;
import repository.MonsterManager;

public class AI {

	public static boolean useAdvancedAI;
	
	// returns a battle move for a monster to perform
	public static Ability getMove(int ID){
		Ability move;
		
		// if advancedAI setting is on, tries to return advanced AI move
		if (useAdvancedAI) {
			move = getAdvancedMove(ID);
			if (move != null) return move;
		}
		
		// otherwise randomly selects from move-set
		int ran = Core.random.nextInt(64);
		switch (ID){
		
			case MonsterManager.ID_MINION1:
				if (ran < 8) move = new Ability(AbilityManager.INDEX_DEFEND);
				else move = new Ability(AbilityManager.INDEX_ATTACK);
				break;
				
			case MonsterManager.ID_STEM1:
				if (ran < 24) move = new Ability(AbilityManager.INDEX_DEFEND);
				else move = new Ability(AbilityManager.INDEX_ATTACK);
				break;
				
			case MonsterManager.ID_MOLLUSCA1:
				move = new Ability(AbilityManager.INDEX_ATTACK);
				break;
		
		
			default: return new Ability(AbilityManager.INDEX_DEFEND);
		}
		
		// randomly chooses target, making sure it is alive
		if (move.ID != AbilityManager.INDEX_DEFEND){
			if (move.targetIsFoe)
				do {
				move.target = Core.random.nextInt(Core.team.size());
				} while (Core.team.get(move.target).HP == 0);
			else
				do {
				move.target = Core.random.nextInt(Core.currentBattle.monsters.size());
				} while (Core.currentBattle.monsters.get(move.target).HP == 0);
				// ^ Loophole if no allies are alive is fixed because can target self
		}
		
		return move;
	}
	
	// returns a battle move selected with advanced intelligence
	private static Ability getAdvancedMove(int ID){
		
		Ability advancedMove = null;
		
		// ANOTHER EXAMPLE :
		// a monster that can revive others
		// revive must be done with advancedAI
		// if it is allowed in regular AI then monster may just randomly
		// cast revive on alive targets which is not really cool
		
		switch(ID){
		
			//case MonsterManager.SuperSmartOptionalBoss:
				// if (someone in team.hp < 25%)
				// int target = idOf(thatSomeone)
				// advancedMove = new Ability(AbilityManager.INDEX_SUPER_STRIKE, target);
		
		
		
		}
		
		
		return advancedMove;
	}
}
