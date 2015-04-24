package ca.mcgill.cs.comp303.rummy.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import ca.mcgill.cs.comp303.rummy.model.Card;
import ca.mcgill.cs.comp303.rummy.model.GUIObserver;
import ca.mcgill.cs.comp303.rummy.model.GameModel;

public class DiscardPanel extends JPanel implements GUIObserver
{
	protected static Map<String, ImageIcon> aCards;
	protected static JLabel aLabel;
	
	private static final String IMAGE_LOCATION = "./resources/";
	private static final String IMAGE_SUFFIX = ".gif";
	private static final String[] RANK_CODES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
	private static final String[] SUIT_CODES = {"c", "d", "h", "s"};
	//contructor should just call a guibuilder
	public DiscardPanel() 
	{
		aCards = new HashMap<>();
		aLabel = new JLabel();
		JLabel text = new JLabel("Discard:");
		add(text);
		add(aLabel);
		setSize(30, 30);
		setVisible(true);
		validate();
		repaint();
	}
	
	private static String getCode( Card pCard )
	{
		return RANK_CODES[ pCard.getRank().ordinal() ] + SUIT_CODES[ pCard.getSuit().ordinal() ];		
	}

	public static ImageIcon getBack()
	{
		return getCard( "b" );
	}
	
	private static ImageIcon getCard( String pCode )
	{
		ImageIcon lIcon = (ImageIcon) aCards.get( pCode );
		if( lIcon == null )
		{
			lIcon = new ImageIcon(CardImages.class.getClassLoader().getResource( IMAGE_LOCATION + pCode + IMAGE_SUFFIX ));
			aCards.put( pCode, lIcon );
		}
		return lIcon;
	}

	@Override
	public void stateChanged(GameModel pModel)
	{
		//when turn == 0, it is the computer's turn
		//this is a really unclear way of doing this lol
		if (pModel.getTurn() == 0)
			discardBlocked(pModel);
		else {
		final String discardCode = pModel.getDiscardCode();
		if (discardCode == null)
		{
			removeAll();
			aCards.clear();
			validate();
			repaint();
		}
		else
		{
			//aCards.clear();
			if (!aCards.containsKey(discardCode))
				aCards.put(discardCode, getCard(discardCode));
			removeAll();
			JLabel lLabel = new JLabel( getCard(discardCode));
			add(lLabel);	
			validate();
			repaint(); 	
		}
		}
	}

	private void discardBlocked(final GameModel pModel)
	{
		new Timer(500, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeAll();
				JLabel lLabel = new JLabel(getCard(pModel.getDiscardCode()));
				add(lLabel);	
				validate();
				repaint(); 		
				((Timer)e.getSource()).stop();
			}
		}).start();
	}
}
