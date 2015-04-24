/*
 * File: Adventure.java
 * Name: Alex M.
 */

import java.io.*;
import java.util.*;

/**
 * This class is the main program class for the Adventure game.
 * In the final version, <code>Adventure</code> should extend
 * <code>ConsoleProgram</code> directly.
 */

public class Adventure {
	//readRoom() was really tricky
	public static void loadRooms(ArrayList<AdvRoom> rooms, File roomFile) {
		try {
			BufferedReader roomScan = new BufferedReader(new FileReader(roomFile));
			//System.out.println("File Open Succesfull");
			
			AdvRoom room = new AdvRoom();
			while(true) {
				room = AdvRoom.readRoom(roomScan);
				if (room != null) {
					room.setVisited(false);
					rooms.add(room);
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	
	}
	//readObject() was easier
	public static void loadObjects(ArrayList<AdvRoom> rooms, File objectFile) {
		if (objectFile.exists()) {
			try {
				BufferedReader objectScan = new BufferedReader(new FileReader(objectFile));
				//System.out.println("File Open Succesfull");
				
				AdvObject obj = new AdvObject();
				while(true) {
					obj = AdvObject.readObject(objectScan);
					if (obj != null) {
						rooms.get(obj.getInitialLocation()-1).addObject(obj);
					} else {
						break;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	}
	//making a synonyms table using hashmap, this is the first time i use hashmaps!
	public static void loadSynonyms(HashMap<String, String> synonyms, File synonymFile) {
		try {
			BufferedReader synonymScan = new BufferedReader(new FileReader(synonymFile));
			//System.out.println("File Open Succesfull");
			
			String line;
			while((line = synonymScan.readLine()) != null) {
				String[] strings = new String[2];
				strings = line.split("=");
				synonyms.put(strings[0],strings[1]);
			}
		} catch (IOException e) {
			System.out.println("File I/O error!");
		}
	}

	public static void main(String[] args) {
		System.out.println("Enter the Game you wish to play: Crowther, Small, Tiny");
		
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.nextLine();
		
		//files
		File roomFile = new File(input+"Rooms.txt");
		File objectFile = new File(input+"Objects.txt");
		File synonymFile = new File("CrowtherSynonyms.txt");
		
		//a lot of hard work went here, loads all the files into the program. Something quite devious will have
		//to be done about saving!!
		ArrayList<AdvRoom> rooms = new ArrayList<AdvRoom>();
		ArrayList<AdvObject> inventory = new ArrayList<AdvObject>();
		HashMap<String, String> synonyms = new HashMap<String, String>();
		loadRooms(rooms, roomFile);
		loadObjects(rooms, objectFile);
		loadSynonyms(synonyms, synonymFile);
		
		//classic stuff
		String instructions = "This is an adventure game! To play, all you must do is write in commands and hopefully nothing will blow up! Write QUIT to leave the game. Write HELP to get this message again. Write INVENTORY to see what you have in your inventory. Write LOOK to get the room description.\n";
		System.out.println(instructions);
		
		System.out.println("Welcome to the "+input.toUpperCase()+" Adventure!\n");
		AdvRoom currentRoom = rooms.get(0);
		//still have not implemented keys or synonym checking
		boolean play = true;
		while(play) {
			ArrayList<AdvMotionTableEntry> connections = currentRoom.getMotionTable();
			//maybe make this its own method, it's relatively simple
			System.out.println(currentRoom.getName()+"\n");
			if (!currentRoom.hasBeenVisited()) {
				System.out.println(currentRoom.getDescription()+"\n");
				currentRoom.setVisited(true);
			}
			
			if (currentRoom.getObjectCount() != 0) {
				for (int i = 0; i < currentRoom.getObjectCount(); i++)
					System.out.println(currentRoom.getObject(i).getName()+" "+currentRoom.getObject(i).getDescription());
			}
			//prints the prompt, takes the user input and converts it to uppercase
			//synonyms are not implemented as of yet
			System.out.print(">");
			String userInput = keyboard.nextLine().toUpperCase();
			System.out.println("");
			//probably wont work for three words, will give an exception if the user inputs 3
			//maybe add a try/catch block if feeling adventurous
			if (userInput.contains(" ")) {
				String[] strings = new String[2];
				strings = userInput.split("\\s+");
				String verb = strings[0];
				String obj = strings[1];
				
				if (verb.equals("TAKE")) {
					for (int i = 0; i < currentRoom.getObjectCount(); i++){
						AdvObject object = currentRoom.getObject(i);
						if (object.getName().equals(obj)) {
							inventory.add(object);
							//might be causing trouble, look out for objects
							currentRoom.removeObject(object);
							System.out.println(obj+" Taken!");
						}
					}
				}
				if (verb.equals("DROP")) {
					for (int i = 0; i < inventory.size(); i++) {
						AdvObject object = inventory.get(i);
						if (object.getName().equals(obj)) {
							currentRoom.addObject(object);
							inventory.remove(object);
							System.out.println(obj+" Dropped!");
						}
					}
				}
			} else {
				//commands that do not involve any object interactions
				if (userInput.equals("QUIT")) {
					play = false;
				}
				if (userInput.equals("HELP")) {
					System.out.println(instructions);
				}
				if (userInput.equals("LOOK")) {
					System.out.println(currentRoom.getDescription()+"\n");
				}
				if (userInput.equals("INVENTORY")) {
					for (int i = 0; i < inventory.size(); i++)
						System.out.println(inventory.get(i).getName()+inventory.get(i).getDescription()+"\n");
				}
				/*
				-general movement flow control
				-if user inputs a direction, program execution will go here and change currentRoom
				-checks is there is a forced direction, in which case the current room immediately goes to the forced destination
				*/
				if (userInput.equals("NORTH") || userInput.equals("SOUTH") || userInput.equals("EAST") || userInput.equals("WEST") || userInput.equals("UP") || userInput.equals("DOWN") || userInput.equals("IN") || userInput.equals("OUT") || userInput.equals("XYZZY") || userInput.equals("PLUGH") || userInput.equals("WAVE") || userInput.equals("WATER")) {
					for (int i = 0; i < connections.size(); i++) {
						if (connections.get(i).getDirection().equals("FORCED")) {
							currentRoom = rooms.get(connections.get(i).getDestinationRoom()-1);
							continue;
						}
					}
					//now checking for keys
					boolean hasKey = false;
					int newDestination = 0;
					for (int i =0; i < connections.size(); i++) {
						if (connections.get(i).getDirection().equals(userInput)) {
							for (int j = 0; j < inventory.size(); j++) {
								if (inventory.get(j).getName().equals(connections.get(i).getKeyName())) {
									hasKey = true;
									newDestination = i;
								}
							}
						}
					}
					
					if (hasKey) {
							currentRoom = rooms.get(connections.get(newDestination).getDestinationRoom()-1);
							continue;
					}
					
					for (int i =0; i < connections.size(); i++) {
						if (connections.get(i).getDirection().equals(userInput)) {
							currentRoom = rooms.get(connections.get(i).getDestinationRoom()-1);
						}
					}
				}
				
			}
			
		}
		
		
		
	}
}
