package ca.mcgill.cs.comp303.rummy.model;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.cs.comp303.rummy.model.Card.Rank;
import ca.mcgill.cs.comp303.rummy.model.Card.Suit;


public class TestAutoMatch
{
	@Test
	public static void testcase1() 
	{
		Hand testHand = new Hand();
		testHand.add(new Card(Rank.ACE, Suit.CLUBS));
		testHand.add(new Card(Rank.ACE, Suit.DIAMONDS));
		testHand.add(new Card(Rank.ACE, Suit.HEARTS));
		testHand.add(new Card(Rank.ACE, Suit.SPADES));
		testHand.add(new Card(Rank.TWO, Suit.CLUBS));
		testHand.add(new Card(Rank.TWO, Suit.DIAMONDS));
		testHand.add(new Card(Rank.TWO, Suit.HEARTS));
		testHand.add(new Card(Rank.TWO, Suit.SPADES));
		testHand.add(new Card(Rank.THREE, Suit.CLUBS));
		testHand.add(new Card(Rank.THREE, Suit.DIAMONDS));
		
		testHand.autoMatch();
		
		assertEquals(2, testHand.getUnmatchedCards().size());
	}
	
	@Test
	public static void testcase2() 
	{
		Hand testHand = new Hand();
		testHand.add(new Card(Rank.ACE, Suit.CLUBS));
		testHand.add(new Card(Rank.TWO, Suit.CLUBS));
		testHand.add(new Card(Rank.THREE, Suit.CLUBS));
		testHand.add(new Card(Rank.FOUR, Suit.CLUBS));
		testHand.add(new Card(Rank.FIVE, Suit.CLUBS));
		testHand.add(new Card(Rank.SIX, Suit.CLUBS));
		testHand.add(new Card(Rank.SEVEN, Suit.CLUBS));
		testHand.add(new Card(Rank.EIGHT, Suit.CLUBS));
		testHand.add(new Card(Rank.NINE, Suit.CLUBS));
		testHand.add(new Card(Rank.TEN, Suit.CLUBS));
		
		testHand.autoMatch();
		
		assertEquals(0, testHand.getUnmatchedCards().size());
	}
	
	@Test
	public static void testcase3() 
	{
		Hand testHand = new Hand();
		testHand.add(new Card(Rank.ACE, Suit.CLUBS));
		testHand.add(new Card(Rank.TWO, Suit.CLUBS));
		testHand.add(new Card(Rank.FIVE, Suit.CLUBS));
		testHand.add(new Card(Rank.SIX, Suit.CLUBS));
		testHand.add(new Card(Rank.JACK, Suit.HEARTS));
		testHand.add(new Card(Rank.FOUR, Suit.HEARTS));
		testHand.add(new Card(Rank.THREE, Suit.DIAMONDS));
		testHand.add(new Card(Rank.THREE, Suit.SPADES));
		testHand.add(new Card(Rank.TWO, Suit.SPADES));
		testHand.add(new Card(Rank.TWO, Suit.DIAMONDS));
		
		testHand.autoMatch();
		
		assertEquals(7, testHand.getUnmatchedCards().size());
	}
	
	
}
