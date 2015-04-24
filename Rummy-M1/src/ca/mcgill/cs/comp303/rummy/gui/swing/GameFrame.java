package ca.mcgill.cs.comp303.rummy.gui.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ca.mcgill.cs.comp303.rummy.model.GameEngine;
//first version, should have basic cardpanels(north south) and deck(west), discard(center), information(east)
public class GameFrame extends JFrame
{

/**
	 * 
	 */
	private static final long serialVersionUID = -9214625504534766520L;
	private DeckPanel deck;
	private DiscardPanel discard;
	private TextPanel textArea;
	private static GameEngine ge;
	private static CardPanel player;
	private static CardPanel opponent;

	//will need to call and add contructors for two card panels, deck, discard, score
//and messages
	public GameFrame() 
	{
		initUI();
	}

	public final void initUI() 
	{
		setTitle("Gin Rummy!");
		setLayout(new BorderLayout());
		
		ge = new GameEngine();
		//CardPanels extend JPanel, implement ActionListener
		//cards are painted in a row, so flow layout, is default and works fine here
		
		//make separate class someday
		JPanel PlayerPanel = new JPanel();
		PlayerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JButton knock = new JButton("knock");
		knock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ge.humanKnocked();
			}
			
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		PlayerPanel.add(knock, c);
		
		ScorePanel humanScore = new ScorePanel(ge);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		PlayerPanel.add(humanScore, c);
		
		
		player = new CardPanel(ge);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		PlayerPanel.add(player, c);
		
		opponent = new CardPanel(ge);
		//shows the back of a card, and nothing when deck is empty
		deck = new DeckPanel();
		deck.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				ge.deckPlayerDraw();
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
		discard = new DiscardPanel();
		discard.addMouseListener(new MouseListener() 
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				ge.discardPlayerDraw();
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
		textArea = new TextPanel();
		
		//adding components to frame
		add(PlayerPanel, BorderLayout.SOUTH);
		add(deck, BorderLayout.WEST);
		add(discard, BorderLayout.CENTER);
		add(textArea, BorderLayout.EAST);
		add(opponent, BorderLayout.NORTH);
		
		//adding observers to ge
		ge.addObserver(opponent);
		ge.addObserver(player);
		ge.addObserver(deck);
		ge.addObserver(discard);
		ge.addObserver(humanScore);
		ge.addObserver(textArea);
		
		
		ge.initializeGame(opponent, player);
		setSize(900,500);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	//no args
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				GameFrame gF = new GameFrame();
				
				//ge.play();
			}
		});
	}

}