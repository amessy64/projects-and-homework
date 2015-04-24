package ca.mcgill.cs.comp303.rummy.gui.swing;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

public class RummyApplet extends JApplet {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4112983415055993483L;

	//Called when this applet is loaded into the browser.
    public void init() {
        //Execute a job on the event-dispatching thread; creating this applet's GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    JFrame jf = new GameFrame();
                    add(jf);
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }
}