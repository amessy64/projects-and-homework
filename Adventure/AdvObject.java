/*
 * File: AdvObject.java
 * --------------------
 * This file defines a class that models an object in the
 * Adventure game.
 */

import java.io.*;
import java.util.*;

/**
 * This class defines an object in the Adventure game.  An object is
 * characterized by the following properties:
 *
 * <ul>
 * <li>Its name, which is the noun used to refer to the object
 * <li>Its description, which is a string giving a short description
 * <li>The room number in which the object initially lives
 * </ul>
 *
 * The external format of the objects file is described in the
 * assignment handout.
 */

public class AdvObject {

	private String objName;
	private String objDescription;
	private int initialRoom;

	public String getName() {
		return objName;
	}
	
	private void setName(String s) {
		objName = s;
	}
	
	public String getDescription() {
		return objDescription;
	}

	public void setDescription(String s) {
		objDescription = s;
	}
	
	public int getInitialLocation() {
		return initialRoom;
	}
	
	public void setInitialLocation(int i) {
		initialRoom = i;
	}

/**
 * Creates a new object by reading its data from the specified
 * reader.  If no data is left in the reader, this method returns
 * <code>null</code> instead of an <code>AdvObject</code> value.
 * Note that this is a static method, which means that you need
 * to call
 *
 *<pre><code>
 *     AdvObject.readObject(rd)
 *</code></pre>
 *
 * @param rd The reader from which the object data is read 
 */
	public static AdvObject readObject(BufferedReader br) {
		try {
			String line = br.readLine();
			if (line != null) {
				AdvObject obj = new AdvObject();
			
				String objname = line;
				obj.setName(objname);
				
				String objDescript = br.readLine();
				obj.setDescription(objDescript);
				
				int init = Integer.parseInt(br.readLine());
				obj.setInitialLocation(init);
				
				br.readLine();
				return obj;
			} else {
				return null;
			}
		} catch (IOException e) {
			System.out.println("File I/O error!");
			return null;
		}
	}
	
	public String toString() {
		return objName+" "+objDescription+" "+initialRoom;
	}
	/*
	public static void main(String args[]) {
		File file = new File("SmallObjects.txt");
		
		try {
			BufferedReader scan = new BufferedReader(new FileReader(file));
			System.out.println("File Open Succesfull");
			
			ArrayList<AdvObject> objects = new ArrayList<AdvObject>();
			AdvObject obje = new AdvObject();
			while(true) {
				obje = AdvObject.readObject(scan);
				if (obje != null) {
					objects.add(obje);
				} else {
					break;
				}
			}
			
			System.out.println(objects.get(2).getName());
			System.out.println(objects.get(2).getDescription());
			System.out.println(objects.get(2).getInitialLocation());
			
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	*/
}
