package com.example.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EquityCalc
{
    static final String[] values = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    static final String[] suits = new String[]{"h", "d", "c", "s"};
    static final int epochs = (int) 1e5;

    public static void main(String[] args)
    {
        System.out.println(getEquities("Tc3h", "-1", "Td9s", "2cAd", "As6c", "-1", "7d4d", "Ah7c", "8s8c", "6d Ts 2h -1 -1"));
    }

    public static String getEquities(String str0, String str1, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String strBoard)
    {
        String[] stringHands = new String[] {str0, str1, str2, str3, str4, str5, str6, str7, str8};
        ArrayList<Hand> hands = new ArrayList<>();

        for(int i = 0; i < 9; i++)
        {
            if(stringHands[i].equals("-1"))
            {
                hands.add(null);
                continue;
            }
            Card card1 = new Card(stringHands[i].substring(0, 1), stringHands[i].substring(1, 2));
            Card card2 = new Card(stringHands[i].substring(2, 3), stringHands[i].substring(3, 4));
            hands.add(new Hand(card1, card2));
        }

        String[] arrBoard = strBoard.split(" ");
        Card[] board = new Card[5];
        for(int i = 0; i < 5; i++)
        {
            if(arrBoard[i].equals("-1")) break;
            board[i] = new Card(arrBoard[i].substring(0, 1), arrBoard[i].substring(1, 2));
        }

        Set<Integer> to_excl = new HashSet<>();
        for(Hand hand : hands)
        {
            if(hand == null) continue;
            to_excl.add(hand.getCard1().getSuit() * 13 + hand.getCard1().getValue());
            to_excl.add(hand.getCard2().getSuit() * 13 + hand.getCard2().getValue());
        }

        for(Card c : board)
        {
            if(c == null) break;
            to_excl.add(c.getSuit() * 13 + c.getValue());
        }

        ArrayList<Integer> not_excl = new ArrayList<>();
        for(int i = 0; i < 52; i++)
        {
            if(!to_excl.contains(i)) not_excl.add(i);
        }

        double[] cntWDL = new double[9];

        for(int rep = 0; rep < epochs; rep++)
        {
            ArrayList<Card> board_cards = getBoard(board, not_excl);
            HandRanker ranker = new HandRanker(hands, board_cards);
            ArrayList<Integer> winners = ranker.rank();
            if(winners.size() == 1) cntWDL[winners.get(0)]++;
            else
            {
                for(int win : winners) cntWDL[win] += 0.5;
            }
        }

        String ret = "";
        for(double i : cntWDL)
        {
            String equity = String.format("%.2f", i / epochs * 100);
            ret += equity + " ";
        }
        return ret;
    }

    public static ArrayList<Card> getBoard(Card[] board, ArrayList<Integer> not_excl)
    {
        Collections.shuffle(not_excl);
        ArrayList<Card> genBoard = new ArrayList<>();
        for(Card c : board)
            if(c != null) genBoard.add(c);
        for(int i = genBoard.size(); i < 5; i++)
        {
            genBoard.add(new Card(values[not_excl.get(i) % 13], suits[not_excl.get(i) / 13]));
        }
        return genBoard;
    }

    public Card getCard(int index)
    {
        return new Card(values[index % 13], suits[index / 13]);
    }
}
