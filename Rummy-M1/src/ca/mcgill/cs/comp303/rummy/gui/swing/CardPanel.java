package ca.mcgill.cs.comp303.rummy.gui.swing;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import ca.mcgill.cs.comp303.rummy.model.Card;
import ca.mcgill.cs.comp303.rummy.model.GUIObserver;
import ca.mcgill.cs.comp303.rummy.model.GameEngine;
import ca.mcgill.cs.comp303.rummy.model.GameModel;
import ca.mcgill.cs.comp303.rummy.model.Hand;

public class CardPanel extends JPanel implements GUIObserver
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4812842226139949746L;

	protected Map<String, ImageIcon> CardImages;
	
	private JLabel[] aLabels;
	private int aPlayer;
	
	private static final String IMAGE_LOCATION = "./resources/";
	private static final String IMAGE_SUFFIX = ".gif";
	private static final String[] RANK_CODES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
	private static final String[] SUIT_CODES = {"c", "d", "h", "s"};
	
	GameModel ge;

	public CardPanel(GameModel pModel)
	{
		super();
		ge = pModel;
		CardImages =  new HashMap<>();
		aLabels = new JLabel[10];
		
		int i = 0;
		for (JLabel jl : aLabels)
		{
			jl = new JLabel();
			jl.setIcon(getBack());
			CardImages.put("b", getBack());
			aLabels[i] = jl;
			i++;
			add(jl);
		}
		
		setSize(100, 30);
		setVisible(true);
	}
	//note MUST refactor into separate class
	private static String getCode( Card pCard )
	{
		return RANK_CODES[ pCard.getRank().ordinal() ] + SUIT_CODES[ pCard.getSuit().ordinal() ];		
	}

	public ImageIcon getBack()
	{
		return getCard( "b" );
	}
	
	private ImageIcon getCard( String pCode )
	{
		ImageIcon lIcon = (ImageIcon) CardImages.get( pCode );
		if( lIcon == null )
		{
			lIcon = new ImageIcon(CardImages.class.getClassLoader().getResource( IMAGE_LOCATION + pCode + IMAGE_SUFFIX ));
		}
		return lIcon;
	}
	
	@Override
	public void stateChanged(GameModel pModel)
	{
		Hand h = pModel.getHand(aPlayer);
		HashMap<String, ImageIcon> hs = new HashMap<>();
		for (Card c : h.flatten())
		{
			hs.put(getCode(c), getCard(getCode(c)));
		}
		
		for (String s : hs.keySet())
		{
			//will repaint if there's an extra card or if there's a missing card
			if (!CardImages.containsKey(s) || hs.size() < CardImages.size())
			{
				CardImages = hs;
				removeAll();
				for (ImageIcon ic : CardImages.values())
				{
					JLabel lLabel = new JLabel(ic);
					String name = imageToString(ic);
					lLabel.setName(name);
					mouseAdd(lLabel);
					add(lLabel);
				}
				validate();
				repaint();
				break;
			}
		}
	}
	
	private String imageToString(ImageIcon ic)
	{
		String name = "";
		for (String names : CardImages.keySet())
		{
			if (CardImages.get(names) == ic)
			{
				name += names;
				break;
			}
		}
		return name;
	}
	private void mouseAdd(JLabel pLabel)
	{
		pLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				Component clicked = e.getComponent();
				String name = clicked.getName();
				Card c = makeCard(name);
				ge.playerDiscardedCard(c);
				
			}

			private Card makeCard(String name)
			{
				char rankLetter = name.charAt(0);
				char suitLetter = name.charAt(1);
				
				int i = 0;
				for (String s : RANK_CODES) 
				{
					if (rankLetter == s.charAt(0))
					{
						break;
					}
					i++;
				}
				
				int j = 0;
				for (String s : SUIT_CODES) 
				{
					if (suitLetter == s.charAt(0))
					{
						break;
					}
					j++;
				}
				
				Card c = new Card(i,j);
				return c;
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	public void setPlayer(int pPlayer)
	{
		aPlayer = pPlayer;
	}
	
}
