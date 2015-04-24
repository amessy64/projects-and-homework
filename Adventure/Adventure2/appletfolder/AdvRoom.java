/*
 * File: AdvRoom.java
 * ------------------
 * This file defines a class that models a single room in the
 * Adventure game.
 */

import java.io.*;
import java.util.*;

/**
 * This class defines a single room in the Adventure game.  A room
 * is characterized by the following properties:
 *
 * <ul>
 * <li>A room number, which must be greater than zero
 * <li>Its name, which is a one-line string identifying the room
 * <li>Its description, which is a line array describing the room
 * <li>A list of objects contained in the room
 * <li>A flag indicating whether the room has been visited
 * <li>A motion table specifying the exits and where they lead
 * </ul>
 *
 */

public class AdvRoom {

	private int roomNumber;
	private String roomName;
	private String roomDescription;
	private ArrayList<AdvObject> objects = new ArrayList<AdvObject>();
	boolean visited;
	private ArrayList<AdvMotionTableEntry> connections = new ArrayList<AdvMotionTableEntry>();
	
	public int getRoomNumber() {
		return roomNumber;
	}
	
	public void setRoomNumber(int i) {
		roomNumber = i;;
	}

	public String getName() {
		return roomName;
	}
	
	public void setName(String s) {
		roomName = s;
	}

	public void addObject(AdvObject obj) {
		objects.add(obj);
	}

	public void removeObject(AdvObject obj) {
		objects.remove(obj);
	}

	public boolean containsObject(AdvObject obj) {
		if (objects.contains(obj))
			return true;
		return false;
	}

	public int getObjectCount() {
		return objects.size();
	}
	
	public AdvObject getObject(int index) {
		return objects.get(index);
	}

	public String getDescription() {
		return roomDescription;
	}
	
	public void setDescription(String s) {
		roomDescription = s;
	}

	public void setVisited(boolean flag) {
		visited = flag;
	}

	public boolean hasBeenVisited() {
		return visited;
	}

	public ArrayList<AdvMotionTableEntry> getMotionTable() {
		return connections;
	}
	
	public void addTableEntry(AdvMotionTableEntry connection) {
		this.connections.add(connection);
	}
	
	public String toString() {
		return roomNumber+" "+roomName+" "+roomDescription+" "+objects+" "+connections;
	}

/**
 * Creates a new room by reading its data from the specified
 * reader.  If no data is left in the reader, this method returns
 * <code>null</code> instead of an <code>AdvRoom</code> value.
 * Note that this is a static method, which means that you need
 * to call
 *
 *<pre><code>
 *     AdvRoom.readRoom(br)
 *</code></pre>
 *
 * @param br The reader from which the room data is read 
 */
	public static AdvRoom readRoom(BufferedReader br) {
		try {
			String line = br.readLine();
			if (line != null) {
				AdvRoom thisRoom = new AdvRoom();
				
				int num = Integer.parseInt(line);
				thisRoom.setRoomNumber(num);
				
				String name = br.readLine();
				thisRoom.setName(name);
		
				String description = "";
				String stop = "-----";
				while (true) {
					line = br.readLine();
					if (line.equals(stop)) 
						break;
					description += line;
				}
				thisRoom.setDescription(description);
		
				while (true) {
					line = br.readLine();
					if (line == null || line.length() == 0)
						break;
					parseConnectionLine(thisRoom, line);
				}

				return thisRoom;
			} else {
				return null;
			}
		} catch (IOException e) {
			System.out.println("File I/O error!");
			return null;
		}
	}
	
	private static void parseConnectionLine(AdvRoom room, String s) {
		String key = null;
		String[] strings1 = new String[2];
		
		boolean flag = false;
		for (int i = 0;i < s.length(); i++) {
			if (s.charAt(i) == '/') {
				flag = true;
			}
		}
		//needs specific 5 spaces per motion and number to work
		AdvMotionTableEntry connection;
		if (flag) {
			strings1 = s.split("/");
			key = strings1[1];
			String[] strings2 = strings1[0].split("\\s+");
			connection = new AdvMotionTableEntry(strings2[0],Integer.parseInt(strings2[1]),key);
		} else {
			strings1 = s.split("\\s+");
			connection = new AdvMotionTableEntry(strings1[0],Integer.parseInt(strings1[1]),key);
		}
		room.addTableEntry(connection);
	}
	
	public static void main(String args[]) {
		File file = new File("CrowtherRooms.txt");
		
		try {
			BufferedReader scan = new BufferedReader(new FileReader(file));
			System.out.println("File Open Succesfull");
			
			ArrayList<AdvRoom> rooms = new ArrayList<AdvRoom>();
			AdvRoom room = new AdvRoom();
			while(true) {
				room = AdvRoom.readRoom(scan);
				if (room != null) {
					rooms.add(room);
				} else {
					break;
				}
			}
						
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	

}
