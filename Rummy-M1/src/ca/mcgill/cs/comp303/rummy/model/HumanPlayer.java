package ca.mcgill.cs.comp303.rummy.model;
//has no functionality beyond tracking score and hand
public class HumanPlayer extends Player
{

	public HumanPlayer(String s)
	{
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean knock()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Card decide(Card cardDiscard, GameModel pModel)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
