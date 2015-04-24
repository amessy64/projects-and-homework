/**
 * 
 */
package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashSet;
/**
 * @author Alexander
 * yeeeeeeeeeeeeeee
 */
public class CardSet implements ICardSet
{
	private HashSet<Card> aSet;
	
	public CardSet() {
		aSet = new HashSet<>();
	}
	
	public CardSet( Card pCard ) {
		aSet = new HashSet<>();
		aSet.add(pCard);
	}
	
	public CardSet( CardSet pSet ) {
		aSet = pSet.toHashSet();
	}
	
	public CardSet(ArrayList<Card> pList)
	{
		aSet = new HashSet<>();
		for (Card c : pList)
			aSet.add(c);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Card> iterator()
	{
		return aSet.iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	
	public void add(Card pCard)
	{
		aSet.add(pCard);
	}
	
	public void remove(Card pCard)
	{
		aSet.remove(pCard);
	}
	
	
	public CardSet shallowCopy()
	{
		CardSet cs = new CardSet();
		for (Card c : this)
		{
			cs.add(c);
		}
		return cs;
	}
	
	public CardSet deepCopy()
	{
		CardSet copy = new CardSet();
		for (Card c : aSet)
		{
			copy.add(new Card(c.getRank(), c.getSuit()));
		}
		return copy;
	}
	
	//helper method
	private HashSet<Card> toHashSet() {
		
		Iterator<Card> itr = iterator();
		HashSet<Card> hS = new HashSet<>();
		
		while(itr.hasNext()) 
		{
			hS.add(itr.next());
		}
		
		return hS;
	}
	//pCards must be unmatched for this method to mean anything, maybe put it in Hand?
	public boolean containsUnmatched(CardSet pCards)
	{
		for (Card c: this)
		{
			if (!pCards.contains(c)) 
			{
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<Card> toArrayList() 
	{
		Iterator<Card> itr = iterator();
		ArrayList<Card> cards = new ArrayList<>();
		
		while(itr.hasNext()) 
		{
			cards.add(itr.next());
		}
		
		return cards;
	}

	/* (non-Javadoc)
	 * @see ca.mcgill.cs.comp303.rummy.model.ICardSet#contains(ca.mcgill.cs.comp303.rummy.model.Card)
	 */
	@Override
	public boolean contains(Card pCard)
	{
		return aSet.contains(pCard);
		/*alternative method follows
		 * 
		 * Iterator<Card> itr = aSet.iterator();
		 * while(itr.hasNext()) {
		 * 	if (itr.next() == pCard) 
		 *    return true
		 *  }
		 * }
		 * return false
		 * */
	}

	/* (non-Javadoc)
	 * @see ca.mcgill.cs.comp303.rummy.model.ICardSet#size()
	 */
	@Override
	public int size()
	{
		//alternative is iterate over set, count++ and return result
		return aSet.size();
	}

	/* (non-Javadoc)
	 * @see ca.mcgill.cs.comp303.rummy.model.ICardSet#isGroup()
	 */
	@Override
	public boolean isGroup()
	{
		Iterator<Card> itr = aSet.iterator();
		Card c = itr.next();
		while (itr.hasNext())
		{
			if (c.getRank().ordinal() != itr.next().getRank().ordinal())
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.mcgill.cs.comp303.rummy.model.ICardSet#isRun()
	 */
	@Override
	public boolean isRun()
	{
		ArrayList<Card> cl = this.toArrayList();
		Collections.sort(cl);
		Iterator<Card> itr = cl.iterator();
		Card c = itr.next();
		while (itr.hasNext())
		{
			Card nextCard = itr.next();
			if (c.getRank().ordinal()+1 != nextCard.getRank().ordinal())
			{
				return false;
			}
			c = nextCard;
			
		}
		return true;
	}

	public boolean shared(HashSet<CardSet> runs)
	{
		Iterator<CardSet> itr = runs.iterator();
		while(itr.hasNext())
		{
			Iterator<Card> citr = itr.next().iterator();
			while (citr.hasNext()) 
			{
				if (contains(citr.next()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean collides(CardSet cs)
	{
		for (Card c : cs)
		{
			if (contains(c))
				return true;
		}
		return false;
	}

	public Card getFirstCard()
	{
		Card first = null;
		for (Card c : aSet)
		{
			first = c;
			aSet.remove(c);
			break;
		}
		
		assert first != null;
		return first;
	}

}
