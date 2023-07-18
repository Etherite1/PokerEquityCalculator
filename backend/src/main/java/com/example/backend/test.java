package com.example.backend;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class test
{
    public static void main(String[] args)
    {
        Card[] sevenCardHand = new Card[]
        {
                new Card("Q", "s"),
                new Card("5", "c"),
                new Card("J", "d"),
                new Card("A", "s"),
                new Card("8", "s"),
                new Card("T", "d"),
                new Card("K", "s")
        };
        ArrayList<Card> c = new ArrayList<>();
        for(Card card : sevenCardHand) c.add(card);
        HandType classify = new HandType(c);
        System.out.println(classify.getRank());
        System.out.println(classify.getTiebreakInfo());
    }
}
