package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;

import ca.mcgill.cs.comp303.rummy.model.Card.Rank;
import ca.mcgill.cs.comp303.rummy.model.Card.Suit;

/**
 * Models a hand of 10 cards. The hand is not sorted. Not threadsafe.
 * The hand is a set: adding the same card twice will not add duplicates
 * of the card.
 * @inv size() > 0
 * @inv size() <= HAND_SIZE
 */
public class Hand
{
	private HashSet<CardSet> aMyHand;
	private CardSet aUnMatched;
	private int aSize;
	
	private final int aHandSize = 10;
	private final int aFaceScore = 10;
	private final int aSpotRank = 9;
	/**
	 * Constructor.
	 */
	public Hand()
	{
		aMyHand = new HashSet<>();
		aUnMatched = new CardSet();
		aSize = 0;
	}
	
	/**
	 * Adds pCard to the list of unmatched cards.
	 * If the card is already in the hand, it is not added.
	 * @param pCard The card to add.
	 * @throws HandException if the hand is complete.
	 * @throws HandException if the card is already in the hand.
	 * @pre pCard != null
	 */
	public void add( Card pCard )
	{
		aUnMatched.add(pCard);
		aSize++;
	}
	
	/**
	 * Remove pCard from the hand and break any matched set
	 * that the card is part of. Does nothing if
	 * pCard is not in the hand.
	 * @param pCard The card to remove.
	 * @Pre pCard != null
	 */
	public void remove( Card pCard )
	{
		assert pCard != null;
		
		for (CardSet cs : aMyHand)
		{
			if (cs.contains(pCard)) 
			{
				cs.remove(pCard);
				
				for (Card c: cs)
				{
					aUnMatched.add(c);
					cs.remove(c);
				}
				aMyHand.remove(cs);
			}
		}
		if (aUnMatched.contains(pCard))
		{
			aUnMatched.remove(pCard);
		}
		aSize--;
	}
	
	/**
	 * @return True if the hand is complete.
	 */
	public boolean isComplete()
	{
		return aSize == aHandSize; //does complete mean 10 cards in hand?
	}
	
	/**
	 * Removes all the cards from the hand.
	 */
	public void clear()
	{
		aMyHand.clear();
		aUnMatched = new CardSet();
	}
	
	/**
	 * @return A copy of the set of matched sets
	 */
	public HashSet<CardSet> getMatchedSets()
	{
		//returning references to the same cards
		HashSet<CardSet> matchedSets = new HashSet<>();
		for (CardSet cs : aMyHand)
		{
			if (!cs.equals(aUnMatched))
			{
				matchedSets.add((CardSet) cs.shallowCopy());
			}
		}
		return matchedSets;
	}
	
	/**
	 * @return A copy of the set of unmatched cards.
	 */
	//@SuppressWarnings("unchecked")
	public CardSet getUnmatchedCards()
	{
		return aUnMatched;
	}
	
	/**
	 * @return The number of cards in the hand.
	 */
	public int size()
	{
		return aSize;
	}
	
	/**
	 * Determines if pCard is already in the hand, either as an
	 * unmatched card or as part of a set.
	 * @param pCard The card to check.
	 * @return true if the card is already in the hand.
	 * @pre pCard != null
	 */
	public boolean contains( Card pCard )
	{
		for (CardSet cs: aMyHand)
		{
			if (cs.contains(pCard)) 
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return The total point value of the unmatched cards in this hand.
	 */
	public int score()
	{
		int points = 0;
		for (Card c: aUnMatched) 
		{
			if (c.getSuit().ordinal() < aSpotRank)
			{
				points += c.getSuit().ordinal();
			}
			else
			{
				points += aFaceScore;
			}
		}
		return points;
	}
	
	/**
	 * Creates a group of cards of the same rank.
	 * @param pCards The cards to groups
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	
	public void createGroup( CardSet pCards )
	{
		assert pCards != null;
		
		if (pCards.isGroup() && pCards.containsUnmatched(aUnMatched)) 
		{
			//need to remove chosen cards from unmatched
			CardSet newGroup = (CardSet) pCards.shallowCopy();
			aMyHand.add(newGroup);
			removeFromUnMatched(newGroup);
		}
		else
		{
			throw new HandException("Group not created!");
		}
	}
	
	private void removeFromUnMatched(CardSet pCards)
	{
		for (Card c : pCards)
		{
			if (aUnMatched.contains(c))
			{
				aUnMatched.remove(c);
			}
		}
		
	}

	//generateGroups, almost certain it doesnt work
	private static HashSet<CardSet> generateGroups( CardSet pCards ) 
	{
		Iterator<Card> itr = pCards.iterator();
		HashMap<Rank, ArrayList<Card>> ranks = new HashMap<>();
		
		//generate the hashmap
		while(itr.hasNext()) 
		{
			Card curr = itr.next();
			if (!ranks.containsKey(curr.getRank()))
			{
				ArrayList<Card> cards = new ArrayList<>();
				cards.add(curr);
				ranks.put(curr.getRank(), cards);
			}
			else
			{
				ranks.get(curr.getRank()).add(curr);
			}
		}
		
		HashSet<CardSet> groups = new HashSet<>();
		for (Rank r : ranks.keySet())
		{
			if (ranks.get(r).size() > 2)
			{
				CardSet cs = new CardSet();
				for (Card c : ranks.get(r)) 
				{
					cs.add(c);
				}
				groups.add(cs);
			}
		}
		
		return groups;
	}
	
	/**
	 * Creates a run of cards of the same suit.
	 * @param pCards The cards to group in a run
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public void createRun( CardSet pCards )
	{
		assert pCards != null;
		
		if (pCards.isRun() && pCards.containsUnmatched(aUnMatched)) 
		{
			//need to remove cards from unmatched
			CardSet newGroup = (CardSet) pCards.shallowCopy();
			aMyHand.add(newGroup);
			removeFromUnMatched(newGroup);
		}
		else
		{
			throw new HandException("Run not created!");
		}
	}
	
	@SuppressWarnings("unused")
	private static HashSet<CardSet> generateRuns( CardSet pCards ) 
	{
		Iterator<Card> itr = pCards.iterator();
		HashMap<Suit, ArrayList<Card>> suits = new HashMap<>();
		//generate the hashmap
		while(itr.hasNext()) 
		{
			Card curr = itr.next();
			if (!suits.containsKey(curr.getSuit()))
			{
				ArrayList<Card> cards = new ArrayList<>();
				cards.add(curr);
				suits.put(curr.getSuit(), cards);
			}
			else
			{
				suits.get(curr.getSuit()).add(curr);
			}
		}
		
		HashSet<CardSet> runs = new HashSet<>();
		for (Suit s : suits.keySet())
		{
			ArrayList<Card> cs = suits.get(s);
			Card[] cards = new Card[13];
			
			itr = cs.iterator();
			while(itr.hasNext()) 
			{
				Card curr = itr.next();
				cards[curr.getRank().ordinal()] = curr;
			}
			
			CardSet candidate = new CardSet();
			for (int i = 0; i < 13; i++) 
			{
				//should refactor;
				if ((cards[(i+1)%13] != null || i != 0 && cards[(i-1)%13] !=null) && cards[i] != null || i+1 > cards.length && cards[i-1] != null) 
				{
					candidate.add(cards[i]);
				}
				else 
				{
					if (candidate.size() >= 3) 
					{
						ArrayList<Card>  lList= candidate.toArrayList();
						Collections.sort(lList);
						CardSet sorted = new CardSet(lList);
						
						runs.add(sorted);
					}
					candidate = new CardSet();
				}
			}
		}
		
		return runs;
	}
	
	/**
	 * Calculates the matching of cards into groups and runs that
	 * results in the lowest amount of points for unmatched cards.
	 * 
	 */
	public void autoMatch()
	{
		//create all possible groups and runs
		//check for collisions, group collisions
		//run a getMin loop for each collision
		HashSet<CardSet> groups = generateGroups(aUnMatched);
		HashSet<CardSet> runs = generateRuns(aUnMatched);
		ArrayList<ArrayList<CardSet>> intersections = new ArrayList<>();
		
		for (CardSet c : groups) 
		{
			if (!c.shared(runs)) 
			{
				createGroup(c);
			}
			else
			{
				ArrayList<CardSet> coll = new ArrayList<>();
				coll.add(c);
				for (CardSet r : runs) 
				{
					if (r.collides(c)) 
					{
						coll.add(r);
					}
				}
				intersections.add(coll);
			}
		}
		
		for (CardSet c : runs) 
		{
			if (!c.shared(groups)) 
			{
				createRun(c);
			}
		}
		
		for (ArrayList<CardSet> al : intersections)
		{
			chooseMin(al);
		}	
		
	}

	private void chooseMin(ArrayList<CardSet> list)
	{
		int score = 100;
		int position = 0;
		
		for (CardSet cs : list)
		{
			Hand h = this.deepCopy();
			if (cs.isGroup() && cs.containsUnmatched(aUnMatched)) 
			{
				h.createGroup(cs);
			} 
			else if (cs.containsUnmatched(aUnMatched))
			{
				h.createRun(cs);
			}
			if (h.score() < score) 
			{
				score = h.score();
				position = list.indexOf(cs);
			}
		}
		
		if (list.get(position).isGroup()) 
		{
			this.createGroup(list.get(position));
		} 
		else 
		{
			this.createRun(list.get(position));
		}
	}

	private Hand deepCopy()
	{
		Hand h = new Hand();
		for (Card c : this.getUnmatchedCards())
			h.add(c);
		for (CardSet cs : this.getMatchedSets())
		{
			CardSet s = new CardSet();
			for (Card c : cs)
			{
				s.add(c);
			}
			h.aMyHand.add(s);
		}
		return h;
	}

	public Card getFirstCard()
	{
		aSize--;
		return aUnMatched.getFirstCard();
	}

	public void setSize(int i)
	{
		aSize = 0;
	}

	public ArrayList<Card> flatten()
	{
		ArrayList<Card> cards = new ArrayList<>();
		int i = 0;
		
		for(CardSet cs : aMyHand)
		{
			for (Card c : cs)
			{
				cards.add(c);
			}
		}
		for (Card c : aUnMatched)
		{
			cards.add(c);
		}
		
		return cards;
	}
}
