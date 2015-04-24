/*
 * File: Adventure.java
 * Name: Alex M.
 */

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class is the main program class for the Adventure game.
 * In the final version, <code>Adventure</code> should extend
 * <code>ConsoleProgram</code> directly.
 */

public class Adventure2  {
	
	//Pertaining to State
	private File roomFile;
	private File objectFile;
	private File synonymFile;
	private ArrayList<AdvRoom> rooms;
	private ArrayList<AdvObject> inventory;
	private HashMap<String, String> synonyms;
	private AdvRoom currentRoom;
	
	String instructions = "This is an adventure game! To play, all you must do is write in commands and hopefully nothing will blow up! Write QUIT to leave the game. Write HELP to get this message again. Write INVENTORY to see what you have in your inventory. Write LOOK to get the room description.\n";
	
	public Adventure2() {
		//files
		File roomFile = new File("CrowtherRooms.txt");
		File objectFile = new File("CrowtherObjects.txt");
		File synonymFile = new File("CrowtherSynonyms.txt");
		
		//a lot of hard work went here, loads all the files into the program. Something quite devious will have
		//to be done about saving!!
		rooms = new ArrayList<AdvRoom>();
		inventory = new ArrayList<AdvObject>();
		synonyms = new HashMap<String, String>();
		
		loadRooms(rooms, roomFile);
		loadObjects(rooms, objectFile);
		loadSynonyms(synonyms, synonymFile);
		
		currentRoom = rooms.get(0);
	}

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

	public void game(String s, JTextArea textArea) {
		
		//still have not implemented keys or synonym checking
			ArrayList<AdvMotionTableEntry> connections = currentRoom.getMotionTable();
			
			//maybe make this its own method, it's relatively simple
			if (!currentRoom.hasBeenVisited()) {
				textArea.append(currentRoom.getDescription()+"\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				currentRoom.setVisited(true);
				
				if (currentRoom.getObjectCount() != 0) {
					for (int i = 0; i < currentRoom.getObjectCount(); i++)
					{
						textArea.append(currentRoom.getObject(i).getName()+" "+currentRoom.getObject(i).getDescription()+"\n");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
				}
			}
			
			//synonyms are not implemented as of yet
			
			//probably wont work for three words, will give an exception if the user inputs 3
			//maybe add a try/catch block if feeling adventurous
			if (s.contains(" ")) {
				String[] strings = new String[2];
				strings = s.split("\\s+");
				String verb = strings[0];
				String obj = strings[1];
				
				if (verb.equals("TAKE")) {
					for (int i = 0; i < currentRoom.getObjectCount(); i++){
						AdvObject object = currentRoom.getObject(i);
						if (object.getName().equals(obj)) {
							inventory.add(object);
							//might be causing trouble, look out for objects
							currentRoom.removeObject(object);
							textArea.append(obj+" Taken!"+"\n");
							textArea.setCaretPosition(textArea.getDocument().getLength());
						}
					}
				}
				if (verb.equals("DROP")) {
					for (int i = 0; i < inventory.size(); i++) {
						AdvObject object = inventory.get(i);
						if (object.getName().equals(obj)) {
							currentRoom.addObject(object);
							inventory.remove(object);
							textArea.append(obj+" Dropped!"+"\n");
							textArea.setCaretPosition(textArea.getDocument().getLength());
						}
					}
				}
			} else {
				//commands that do not involve any object interactions
				if (s.equals("QUIT")) {
					textArea.append("Closing!"+"\n");
					System.exit(0);
				}
				if (s.equals("HELP")) {
					textArea.append(instructions+"\n");
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				if (s.equals("LOOK")) {
					textArea.append(currentRoom.getDescription()+"\n");
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				if (s.equals("INVENTORY")) {
					for (int i = 0; i < inventory.size(); i++)
						textArea.append(inventory.get(i).getName()+inventory.get(i).getDescription()+"\n");
						textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				/*
				-general movement flow control
				-if user inputs a direction, program execution will go here and change currentRoom
				-checks is there is a forced direction, in which case the current room immediately goes to the forced destination
				*/
				if (s.equals("NORTH") || s.equals("SOUTH") || s.equals("EAST") || s.equals("WEST") || s.equals("UP") || s.equals("DOWN") || s.equals("IN") || s.equals("OUT") || s.equals("XYZZY") || s.equals("PLUGH") || s.equals("WAVE") || s.equals("WATER")) {
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
						if (connections.get(i).getDirection().equals(s)) {
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
							return;
					}
					
					for (int i =0; i < connections.size(); i++) {
						if (connections.get(i).getDirection().equals(s)) {
							currentRoom = rooms.get(connections.get(i).getDestinationRoom()-1);
						}
					}
				}
				
			}
			
		textArea.append(currentRoom.getName()+"\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		
		
		
	}
}
