package com.example.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class preflop_statistics
{
    static final String[] values = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    static final String[] suits = new String[]{"h", "d", "c", "s"};
    static final int epochs = (int) 1e5;

    public static String get_statistics(String str1, String str2)
    {
        String h1c1v = str1.substring(0, 1), h1c1s = str1.substring(1, 2);
        String h1c2v = str1.substring(3, 4), h1c2s = str1.substring(4, 5);
        String h2c1v = str2.substring(0, 1), h2c1s = str2.substring(1, 2);
        String h2c2v = str2.substring(3, 4), h2c2s = str2.substring(4, 5);

        Hand hand1 = new Hand(new Card(h1c1v, h1c1s), new Card(h1c2v, h1c2s));
        Hand hand2 = new Hand(new Card(h2c1v, h2c1s), new Card(h2c2v, h2c2s));

        Set<Integer> to_excl = new HashSet<>();
        to_excl.add(Card.map_suit.get(h1c1s) * 13 + Card.map_val.get(h1c1v));
        to_excl.add(Card.map_suit.get(h1c2s) * 13 + Card.map_val.get(h1c2v));
        to_excl.add(Card.map_suit.get(h2c1s) * 13 + Card.map_val.get(h2c1v));
        to_excl.add(Card.map_suit.get(h2c2s) * 13 + Card.map_val.get(h2c2v));

        ArrayList<Integer> not_excl = new ArrayList<>();
        for(int i = 0; i < 52; i++)
        {
            if(!to_excl.contains(i)) not_excl.add(i);
        }

        ArrayList<Hand> bothHands = new ArrayList<>();
        bothHands.add(hand1);
        bothHands.add(hand2);

        int[] cntWDL = new int[2];

        for(int rep = 0; rep < epochs; rep++)
        {
            ArrayList<Card> board_cards = getBoard(not_excl);
            HandRanker ranker = new HandRanker(bothHands, board_cards);
            ArrayList<Integer> winners = ranker.rank();
            for(int i : winners) cntWDL[i] += 1;
//            System.out.println(winners);
        }

        double equity1 = (1.0 * cntWDL[0]) / (cntWDL[0] + cntWDL[1]);
        double equity2 = (1.0 * cntWDL[1]) / (cntWDL[0] + cntWDL[1]);

        String ret = "";
        ret += (str1 + " has " + equity1 * 100 + "% equity\n");
        ret += (str2 + " has " + equity2 * 100 + "% equity");
        return ret;
    }

    public static ArrayList<Card> getBoard(ArrayList<Integer> not_excl)
    {
        Collections.shuffle(not_excl);
        ArrayList<Card> board = new ArrayList<>();
        for(int i = 0; i < 5; i++)
        {
            board.add(new Card(values[not_excl.get(i) % 13], suits[not_excl.get(i) / 13]));
        }
        return board;
    }

    public Card getCard(int index)
    {
        return new Card(values[index % 13], suits[index / 13]);
    }
}
