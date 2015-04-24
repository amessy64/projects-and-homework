package ca.mcgill.cs.comp303.rummy.model;

public interface GameModel
{
	public Card getCardDeck();
	public Card getCardDiscard();
	public Hand getHand(int player);
	public boolean isDeckEmpty();
	public String getDiscardCode();
	public void playerDiscardedCard(Card c);
	public void discardChosen();
	public int getTurn();
	public int getHumanScore();
	public String getPlay();
}
