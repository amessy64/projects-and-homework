package ca.mcgill.cs.comp303.rummy.model;

import java.util.HashSet;

public abstract class Player
{
	String aName;
	Hand aHand;
	int aMatchScore;
	
	public Player(String s) 
	{
		aName = s;
		aHand = new Hand();
		resetScore(); //sets aMatchScore to 0
	}
	

	public abstract boolean knock();

	public final Card playTurn(GameModel pModel)
	{
		return decide(pModel.getCardDiscard(), pModel);
	}

	public abstract Card decide(Card cardDiscard,GameModel pModel);


	public String getName()
	{
		return aName;
	}

	public void addToHand(Card pDraw)
	{
		aHand.add(pDraw);
	}

	public int getScore()
	{
		aHand.autoMatch();
		return aHand.score();
	}


	public void resetScore()
	{
		aMatchScore = 0;		
	}


	public boolean hasGin()
	{
		return aHand.getUnmatchedCards().size() == 0;
	}


	public void clearHand()
	{
		aHand.clear();
	}


	public void resetSize()
	{
		aHand.setSize(0);		
	}


	public HashSet<CardSet> getMatchedCards()
	{
		return aHand.getMatchedSets();
	}


	public CardSet getUnMatchedCards()
	{
		return aHand.getUnmatchedCards();
	}
	
	public Hand getHand() 
	{
		return aHand;
	}


	public void removeCard(Card c)
	{
		aHand.remove(c);
	}
}
