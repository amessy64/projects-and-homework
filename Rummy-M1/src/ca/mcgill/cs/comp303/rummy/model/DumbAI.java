package ca.mcgill.cs.comp303.rummy.model;

public class DumbAI extends Player
{

	public DumbAI(String s)
	{
		super(s);
	}
	
	public boolean knock() 
	{
		return aHand.score() < 10;
	}
	
	public Card decide(Card cardDiscard,GameModel pModel) 
	{
		aHand.add(pModel.getCardDeck());
		
		return aHand.getFirstCard();
	}

}
