package sorting;

import java.util.Comparator;

import character.PlayerCharacter;

public class PositionComparator implements Comparator<PlayerCharacter>{

	// sorts the player characters based upon their battle position
	@Override
	public int compare(PlayerCharacter pc1, PlayerCharacter po2) {
		return Integer.compare(pc1.battlePosition, po2.battlePosition);
	}
}
