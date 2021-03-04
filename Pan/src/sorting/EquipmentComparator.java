package sorting;

import java.util.Comparator;

import character.Item;

public class EquipmentComparator implements Comparator<Item>{
	
	// returns comparator value based on equip-status (equipped items come first)
	@Override
	public int compare(Item i1, Item i2) {
		if (i1.isEquipped && !i2.isEquipped) return -1;
		else if (!i1.isEquipped && i2.isEquipped) return 1;
		else return Integer.compare(i1.ID, i2.ID);
	}
}