/*
 * File: AdvMotionTableEntry.java
 * ------------------------------
 * This class keeps track of an entry in the motion table.
 */

public class AdvMotionTableEntry {

	private String direction;
	private int destinationRoom;
	private String keyName;
 
 
	public AdvMotionTableEntry(String dir, int room, String key) {
		direction = dir.toUpperCase();
		destinationRoom = room;
		if (key == null)
			keyName = null;
		else
			keyName = key;
	}
   
	public String getDirection() {
		return direction;
	}

	public int getDestinationRoom() {
		return destinationRoom;
	}

	public String getKeyName() {
		return keyName;
	}
	
	public String toString() {
		return direction+" "+destinationRoom+" "+keyName;
	}

}
