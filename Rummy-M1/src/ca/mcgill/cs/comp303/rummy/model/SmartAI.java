package ca.mcgill.cs.comp303.rummy.model;

public class SmartAI extends Player
{

	public SmartAI(String s)
	{
		super(s);
	}

	@Override
	public boolean knock()
	{
		return aHand.score() < 5;
	}

	@Override
	public Card decide(Card pCard, GameModel pModel)
	{
		if (pCard == null)
		{
			aHand.add(pModel.getCardDeck());
			return aHand.getFirstCard();
		}
		else
		{
			aHand.autoMatch();
			for (CardSet cs : aHand.getMatchedSets()) 
			{
				cs.add(pCard);
				if (cs.isGroup() || cs.isRun())
				{
					return aHand.getFirstCard();
				}
				cs.remove(pCard);
			}
		
			if (pCard.getRank().ordinal() < 6)
			{
				aHand.add(pCard);
				pModel.discardChosen();
			}
			else
			{
				aHand.add(pModel.getCardDeck());
			}
		
			return aHand.getFirstCard();
		}
	}

}
