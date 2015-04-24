package ca.mcgill.cs.comp303.rummy.gui.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.mcgill.cs.comp303.rummy.model.GUIObserver;
import ca.mcgill.cs.comp303.rummy.model.GameModel;

public class ScorePanel extends JPanel implements GUIObserver
{
	private JLabel aScore;
	private GameModel aModel;
	
	
	public ScorePanel(GameModel pModel)
	{
		super();
		aModel = pModel;
		aScore = new JLabel("0");
		add(new JLabel("Score: "));
		add(aScore);

		setVisible(true);
		validate();
		repaint();
	}


	@Override
	public void stateChanged(GameModel pModel)
	{
		aScore.setText(""+aModel.getHumanScore());
	}
}
