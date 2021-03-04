package areas;

public class TileEvent {

	
	public static final int INDEX_TRANSITION = 0;
	
	public String tempS = "hello";
	
	public int typeIndex;
	
	public Area transitionArea;
	
	
	TileEvent(int type){
		this.tempS = "hi";
		this.typeIndex = type;
	}
	
	TileEvent(int type, Area newArea){
		this.tempS = "hi";
		this.typeIndex = type;
		this.transitionArea = newArea;
	}
	
	// transition event
	static TileEvent transition(Area area){
		return new TileEvent(0, area);
	}
}
