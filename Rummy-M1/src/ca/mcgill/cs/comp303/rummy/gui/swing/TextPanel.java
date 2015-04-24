package ca.mcgill.cs.comp303.rummy.gui.swing;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import ca.mcgill.cs.comp303.rummy.model.GUIObserver;
import ca.mcgill.cs.comp303.rummy.model.GameModel;

public class TextPanel extends JPanel implements GUIObserver
{
	private JTextArea textArea;
	String curr;
	
	public TextPanel() 
	{
		textArea = new JTextArea(14, 30);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textArea);
		add(scrollPane);
		curr = "";
		setVisible(true);
	}

	@Override
	public void stateChanged(GameModel pModel)
	{
		String play = pModel.getPlay();
		if (play == null)
		{}
		else if (!curr.equals(play))
		{
			curr = play;
			textArea.append(play+"\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		
	}

}
