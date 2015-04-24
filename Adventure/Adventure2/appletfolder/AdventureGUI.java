import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AdventureGUI extends JPanel implements ActionListener {

//GUI core
	protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
	
	//Adventure variable
	Adventure2 adv;
	String instructions = "This is an adventure game! To play, all you must do is write in commands and hopefully nothing will blow up! Write QUIT to leave the game. Write HELP to get this message again. Write INVENTORY to see what you have in your inventory. Write LOOK to get the room description.\n";
	
	public AdventureGUI() 
	{
		
		super(new GridBagLayout());
 
        textField = new JTextField(20);
        textField.addActionListener(this);
 
        textArea = new JTextArea(10, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
		
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
		
		textArea.append("Welcome to the Crowther Adventure!"+"\n"+"\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		
		textArea.append(instructions+"\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		
		adv = new Adventure2();
		adv.game("", textArea);
	}
	
	public void actionPerformed(ActionEvent event) {
        String text = textField.getText().toUpperCase();
		textArea.append(text+"\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		adv.game(text, textArea);
		textArea.setCaretPosition(textArea.getDocument().getLength());
        textField.selectAll();
 
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new AdventureGUI());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	
}