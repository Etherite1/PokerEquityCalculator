package com.example.backend;

import java.util.ArrayList;
import java.util.Collections;

public class HandRanker
{
    ArrayList<Hand> hands;
    ArrayList<Card> board;
    public HandRanker(ArrayList<Hand> hands, ArrayList<Card> board)
    {
        this.hands = hands;
        this.board = board;
    }

    public ArrayList<Integer> rank()
    {
        ArrayList<HandTypePair> classified_hands = new ArrayList<>();

        for(int index = 0; index < this.hands.size(); index++)
        {
            Hand hand = this.hands.get(index);
            if(hand == null) continue;

            ArrayList<Card> sevenCardHand = new ArrayList<>();
            sevenCardHand.add(hand.getCard1());
            sevenCardHand.add(hand.getCard2());
            for(Card c : this.board) sevenCardHand.add(c);

            HandType classify = new HandType(sevenCardHand);
            classified_hands.add(new HandTypePair(classify, index));
        }
        Collections.sort(classified_hands);
        ArrayList<Integer> best_hands = new ArrayList<>();
        for(HandTypePair hand : classified_hands)
        {
            if(hand.compareTo(classified_hands.get(0)) == 0) best_hands.add(hand.index);
            else break;
        }
//        for(Card c: this.board) System.out.print(c + " ");
//        System.out.println();
//        for(HandTypePair i : classified_hands) System.out.println(i.hand.getRank() + " " + i.hand.getTiebreakInfo());
        return best_hands;
    }
}

class HandTypePair implements Comparable<HandTypePair>
{
    HandType hand;
    int index;

    public HandTypePair(HandType hand, int index)
    {
        this.hand = hand;
        this.index = index;
    }

    public int compareTo(HandTypePair o)
    {
        if(this.hand.getRank() == o.hand.getRank())
        {
            ArrayList<Integer> tiebreak1 = this.hand.getTiebreakInfo();
            ArrayList<Integer> tiebreak2 = o.hand.getTiebreakInfo();
            if(tiebreak1.equals(tiebreak2)) return 0;
            else
            {
                int ret = 0;
                for(int i = 0; i < tiebreak1.size(); i++)
                {
                    if(tiebreak1.get(i) > tiebreak2.get(i))
                    {
                        ret = -1;
                        break;
                    }
                    else if(tiebreak1.get(i) < tiebreak2.get(i))
                    {
                        ret = 1;
                        break;
                    }
                }
                return ret;
            }
        }
        else if(this.hand.getRank() > o.hand.getRank()) return -1;
        else return 1;
    }
}