package ca.mcgill.cs.comp303.rummy.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.mcgill.cs.comp303.rummy.model.Card;
import ca.mcgill.cs.comp303.rummy.model.GUIObserver;
import ca.mcgill.cs.comp303.rummy.model.GameModel;


public class DeckPanel extends JPanel implements GUIObserver
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5320041283305814871L;
	protected static Map<String, ImageIcon> aCard;
	protected static JLabel aLabel;
	
	private static final String IMAGE_LOCATION = "./resources/";
	private static final String IMAGE_SUFFIX = ".gif";
	private static final String[] RANK_CODES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
	private static final String[] SUIT_CODES = {"c", "d", "h", "s"};
	
	
	//contructor should just call a guibuilder
	public DeckPanel() 
	{
		aCard = new HashMap<>();
		aLabel = new JLabel();
		aLabel.setIcon(getBack());
		JLabel text = new JLabel("Deck:");
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
		ImageIcon lIcon = (ImageIcon) aCard.get( pCode );
		if( lIcon == null )
		{
			lIcon = new ImageIcon(CardImages.class.getClassLoader().getResource( IMAGE_LOCATION + pCode + IMAGE_SUFFIX ));
			aCard.put( pCode, lIcon );
		}
		return lIcon;
	}

	@Override
	public void stateChanged(GameModel pModel)
	{
		boolean empty = pModel.isDeckEmpty();
		if (empty)
		{
			aLabel.setIcon(null);
		}
		
	}
}
