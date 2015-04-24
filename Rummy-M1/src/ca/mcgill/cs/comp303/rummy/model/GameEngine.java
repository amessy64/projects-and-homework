package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JPanel;

import ca.mcgill.cs.comp303.rummy.gui.swing.CardPanel;

/**
 * @author Alexander
 *
 */
/**
 * @author Alexander
 *
 */

//has functionality for command line stuff, and GUI stuff
public class GameEngine implements GameModel
{
	private ArrayList<Player> aPlayers;
	private Player human;
	//for the text based implementation, a textarea is still possible
	private ArrayList<Logger> aLoggers;
	//for the gui based implementation
	private ArrayList<GUIObserver> aObservers;
	private Deck aStock;
	private Stack<Card> aDiscard;
	private int[] aScore;
	//simply tracks whose turn it is 0 = p1, 1 = p2
	private int aTurn;
	private Stack<String> plays;
	
	private static final String[] RANK_CODES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
	private static final String[] SUIT_CODES = {"c", "d", "h", "s"};

	public GameEngine() 
    {
		aPlayers = new ArrayList<>();
		aLoggers = new ArrayList<>();
		aObservers = new ArrayList<>();
		
		aStock = new Deck();
		aDiscard = new Stack<>();
		
		aScore = new int[2];
		plays = new Stack<>();
		setTurn(0);
    }
	
	public Card getCardDeck() 
	{
		Card deckDraw = aStock.draw();
		//notifyLoggers(""+deckDraw+" was drawn by "+aPlayers.get(aTurn).getName());
		return deckDraw;
	}

	//returns null if stock is empty
	public Card getCardDiscard() 
	{
		if (aDiscard.isEmpty())
		{
			return null;
		}
		else
		{
			Card discardDraw = aDiscard.peek();
			return discardDraw;
		}
	}
	
	public void addPlayer(Player p) 
	{
		getPlayers().add(p);
	}
	
	public void addLogger(Logger l) 
	{
		aLoggers.add(l);
	}
	
	public void newGame() 
	{

		for (Player p: getPlayers())
		{
		    for (int i = 0; i < 10; i++)
		    {
		    	Card c = aStock.draw();
		    	p.addToHand(c);
		    }
		}
			
		aDiscard.push(aStock.draw());
			
		notifyObservers();
	}

	public void play()
	{
		for (Player p : aPlayers)
		{
			//tells loggers the content of a player's hand
			printHand(p.getName(), handToString(p.getHand()));
		}
		//a match will end when one player's points >= 100
		while(aScore[0] <= 100 && aScore[1] <= 100)
		{
			//knocking will end a round
			while(!hasKnocked(getTurn()))
			{
				if (aStock.size() == 0)
				{
					refreshTurn(getTurn());
					break;
				}
				playTurn();
				notifyObservers();
				printHand(aPlayers.get(getTurn()).getName(), handToString(aPlayers.get(getTurn()).getHand()));
			}
			score();
			clearScores();
			reDeal();
		}
		
		
	}
	
	public String handToString(Hand pHand)
	{
		String handContents = "[";
		for (CardSet cs : pHand.getMatchedSets())
		{
			for (Card c : cs)
			{
				handContents += ""+RANK_CODES[c.getRank().ordinal()]+SUIT_CODES[c.getSuit().ordinal()]+" ";
			}
		}
		for (Card c : pHand.getUnmatchedCards())
		{
			handContents += ""+RANK_CODES[c.getRank().ordinal()]+SUIT_CODES[c.getSuit().ordinal()]+" ";
		}
		handContents += "]";
		return handContents;
	}

	
	private void printHand(String pName, String pCards)
	{
		notifyLoggers(pName+": "+pCards);
	}

	private void playTurn()
	{
		setTurn((getTurn()+1)%2);
		Card c = getPlayers().get(getTurn()).playTurn(this);
	
		aDiscard.push(c);
		notifyLoggers("Player: "+ getPlayers().get(getTurn()).getName()+" discarded Card: "+c);
	}

	private void refreshTurn(int turn) {
		notifyLoggers("No one knocked!");
		score();
		clearScores();
		reDeal();
	}

	private void reDeal()
	{
		for (Player p : getPlayers())
		{
			p.clearHand();
			p.resetSize();
		}
		aStock.shuffle();
		newGame();
	}

	private void clearScores()
	{
		for (Player p : getPlayers())
		{
			p.resetScore();
		}
		
	}

	private boolean hasKnocked(int pTurn)
	{
		return getPlayers().get(pTurn).knock();
	}
	//need  to implement layOff()!!!!!!
	private void score()
	{
		//the other player
		int pKnocker = getTurn();
		int other = (pKnocker+1)%2;
		//scoring does not go past 100 in this version of the game
		if (!getPlayers().get(pKnocker).hasGin())
		{
			int otherScore = layoff(getPlayerMatched(pKnocker), getPlayers().get(other));
			if (getPlayers().get(pKnocker).getScore() < otherScore)
			{
				aScore[pKnocker] += getPlayers().get(other).getScore() - getPlayers().get(pKnocker).getScore();
			} 
			else
			{
				aScore[other] += 10 + getPlayers().get(pKnocker).getScore() - getPlayers().get(other).getScore();
			}
		}
		else
		{
			aScore[pKnocker] += 20 + getPlayers().get(other).getScore();
		}
		notifyLoggers("The score for Player1 is: "+aScore[0]+" The Score for Player2 is: "+aScore[1]);
	}

	//assumes no mistake on part of players (best layoff?)
	//not yet tested
	private int layoff(HashSet<CardSet> pLayoff, Player pPlayer)
	{
		int score = pPlayer.getScore();
		for (CardSet cs : pLayoff)
		{
			for (Card c : pPlayer.getUnMatchedCards())
			{
				cs.add(c);
				if (cs.isRun() || cs.isGroup())
				{
					if (c.getSuit().ordinal() < 9)
					{
						score -= c.getSuit().ordinal();
					}
					else
					{
						score -= 10;
					}
				} 
				else
				{
					cs.remove(c);
				}
			}
		}
		return score;
	}

	private HashSet<CardSet> getPlayerMatched(int pPlayer)
	{
		return aPlayers.get(pPlayer).getMatchedCards();
	}

	private void notifyLoggers(String string)
	{
		for (Logger l : aLoggers)
		{
			l.printStatement(string);
		}
	}

	public ArrayList<Player> getPlayers()
	{
		return aPlayers;
	}

	public int getTurn()
	{
		return aTurn;
	}

	public void setTurn(int pTurn)
	{
		this.aTurn = pTurn;
	}

	public void addObserver(GUIObserver go)
	{
		aObservers.add(go);
	}
	
	public void notifyObservers()
	{
		for (GUIObserver go : aObservers)
		{
			go.stateChanged(this);
		}
	}

	@Override
	public Hand getHand(int player)
	{
		return aPlayers.get(player).getHand();
	}

	//dummy method, will integrate GUI to launch from frame later!
	public void initializeGame(CardPanel pPlayer1, CardPanel pPlayer2)
	{
		human = new HumanPlayer("human");
		addPlayer(human);
		pPlayer2.setPlayer(0);
		
		addPlayer(new SmartAI("bob"));
		pPlayer1.setPlayer(1);
		
		//creating a new game should change the card panels
		newGame();
		
	}

	@Override
	public boolean isDeckEmpty()
	{
		return aStock.size() == 0;
	}

	@Override
	public String getDiscardCode()
	{
		if (aDiscard.isEmpty())
			return null;
		
		Card d = aDiscard.peek();
		return RANK_CODES[d.getRank().ordinal()]+SUIT_CODES[d.getSuit().ordinal()];
	}

	/*
	 * 
	 * 
	 * THE GUI METHODS START HEREISH
	 * 
	 * 
	 * */
	
	
	//should not use magic number, but whatever
	public void deckPlayerDraw()
	{
		if (aTurn == 0)
		{
			human.addToHand(getCardDeck());
			plays.push("Human drew from the deck!");
			aTurn = 1;
			notifyObservers();
		}
		
	}

	public void discardPlayerDraw()
	{
		if (aTurn == 0)
		{
			Card discard = drawDiscard();
			plays.push("Human drew : "+discard.getRank()+" of "+discard.getSuit());
			human.addToHand(discard);
			aTurn = 1;
			notifyObservers();
		}
	}

	private Card drawDiscard()
	{
		return aDiscard.pop();
	}

	private void computerTurn()
	{
		Card c = getPlayers().get(1).playTurn(this);
		plays.push("Computer played : "+c.getRank()+" of "+c.getSuit());
		System.out.println(getPlayers().get(1).getHand().size());
		aTurn = 0;
		aDiscard.push(c);
		notifyObservers();
		checkScores();
	}
	//need to get textArea working for this
	private void checkScores()
	{
		if (aScore[0] > 100 || aScore[1] > 100)
		{
			System.exit(0);
		}
		if (aStock.size() == 0)
		{
			score();
			clearScores();
			reDeal();
		}
	}

	//this should only be possible after player draws, it currently is not :(
	@Override
	public void playerDiscardedCard(Card c)
	{
		if (human.getHand().size() == 11)
		{
			plays.push("Human discarded : "+c.getRank()+" of "+c.getSuit());
			aDiscard.push(c);
			human.removeCard(c);
			System.out.println(human.getHand().size());
			notifyObservers();
			computerTurn();
		}
		
	}

	@Override
	public void discardChosen()
	{
		aDiscard.pop();
	}

	public void humanKnocked()
	{
		// TODO Auto-generated method stub
		plays.push("Human knocked!");
		score();
		clearScores();
		reDeal();
		checkScores();
		notifyObservers();
	}

	@Override
	public int getHumanScore()
	{
		return aScore[0];
	}

	@Override
	public String getPlay()
	{
		if (plays.isEmpty())
			return null;
		return plays.peek();
	}
	
}
