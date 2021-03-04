package sorting;

import java.util.Comparator;

import battle.Combatant;
import foundation.Core;

public class SpeedComparator implements Comparator<Combatant>{
	@Override
	public int compare(final Combatant c1, final Combatant c2){
		int comparatorValue = c1.getSpeed().compareTo(c2.getSpeed());
    	if (comparatorValue == 0) {
    		int ran = Core.random.nextInt(2);
    		if (ran == 1) return 1;
    		else return -1;
    	}
    	else return comparatorValue;
	}
}