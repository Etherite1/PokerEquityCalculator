package com.example.backend;

import java.util.ArrayList;
import java.util.Collections;

public class HandType
{
    // an object containing the type of hand and relevant tiebreaker info
    private ArrayList<Card> sevenCardHand;
    private int rank;
    private ArrayList<Integer> tiebreakInfo = new ArrayList<>();
    private int[] values = new int[13];
    private int[] suits = new int[4];
    public HandType(ArrayList<Card> sevenCardHand)
    {
        this.sevenCardHand = sevenCardHand;
        for(Card card : sevenCardHand)
        {
            values[card.getValue()]++;
            suits[card.getSuit()]++;
        }
        if(this.straightFlush()) return;
        else if(this.fourOfAKind()) return;
        else if(this.fullHouse()) return;
        else if(this.flush()) return;
        else if(this.straight()) return;
        else if(this.threeOfAKind()) return;
        else if(this.twoPair()) return;
        else if(this.pair()) return;
        else this.highCard();
    }

    public int getRank()
    {
        return this.rank;
    }

    public ArrayList<Integer> getTiebreakInfo()
    {
        return this.tiebreakInfo;
    }

    public boolean straightFlush() // rank 8
    {
        // return tiebreak info as (straight flush high card)
        int flush_suit = -1;
        for(int suit = 0; suit < 4; suit++)
        {
            if(this.suits[suit] >= 5)
            {
                flush_suit = suit;
                break;
            }
        }
        if(flush_suit == -1) return false;

        ArrayList<Integer> flush_values = new ArrayList<>();
        for(Card card : this.sevenCardHand)
        {
            if(card.getSuit() == flush_suit) flush_values.add(card.getValue());
        }

        Collections.sort(flush_values);
        Collections.reverse(flush_values);

        int straight_high = -1;
        if(flush_values.get(0) == 12) // checking if Ace can be lowest
        {
            boolean five_high_flush = true;
            int value_needed = 3;
            for(int i = flush_values.size() - 4; i < flush_values.size(); i++)
            {
                if(flush_values.get(i) == value_needed) value_needed--;
                else
                {
                    five_high_flush = false;
                    break;
                }
            }
            if(five_high_flush) straight_high = 3;
        }

        for(int i = 0; i < flush_values.size() - 4; i++)
        {
            int value_needed = flush_values.get(i);
            boolean hit_straight = true;
            for(int j = i; j < i + 5; j++)
            {
                if(flush_values.get(j) == value_needed) value_needed--;
                else
                {
                    hit_straight = false;
                    break;
                }
            }
            if(hit_straight)
            {
                straight_high = flush_values.get(i);
                break;
            }
        }

        if(straight_high == -1) return false;
        else
        {
            this.rank = 8;
            this.tiebreakInfo.add(straight_high);
            return true;
        }
    }

    public boolean fourOfAKind() // rank 7
    {
        // returns tiebreak info as [quadvalue, kickervalue]
        boolean foundQuads = false;
        int quadsValue = -1, kickerValue = -1;
        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] == 4)
            {
                quadsValue = value;
                foundQuads = true;
            }
            else if(this.values[value] >= 1) kickerValue = value;
        }

        if(foundQuads)
        {
            this.rank = 7;
            this.tiebreakInfo.add(quadsValue);
            this.tiebreakInfo.add(kickerValue);
        }
        return foundQuads;
    }

    public boolean fullHouse() // rank 6
    {
        // returns tiebreakInfo as [tripsvalue, pairvalue]
        int trips_value = -1, pair_value = -1;
        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] == 3) trips_value = value;
        }
        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] >= 2 && value != trips_value) pair_value = value;
        }

        if(trips_value == -1 || pair_value == -1) return false;
        else
        {
            this.rank = 6;
            this.tiebreakInfo.add(trips_value);
            this.tiebreakInfo.add(pair_value);
            return true;
        }
    }

    public boolean flush() // rank 5
    {
        // returns tiebreak info as list of values of cards, length 5
        int flush_suit = -1;
        for(int suit = 0; suit < 4; suit++)
        {
            if(this.suits[suit] >= 5)
            {
                flush_suit = suit;
                break;
            }
        }

        if(flush_suit == -1) return false;
        else
        {
            this.rank = 5;
            ArrayList<Integer> flush_values = new ArrayList<>();
            for (Card card : this.sevenCardHand) {
                if (card.getSuit() == flush_suit) flush_values.add(card.getValue());
            }
            Collections.sort(flush_values);
            Collections.reverse(flush_values);
            for (int i = 0; i < 5; i++) this.tiebreakInfo.add(flush_values.get(i));
            return true;
        }
    }
    public boolean straight() // rank 4
    {
        // returns tiebreak info as [straight high card]
        int straight_high = -1;
        boolean hit_5_high = true;
        for(int value : new int[]{3, 2, 1, 0, 12})
        {
            if(this.values[value] == 0)
            {
                hit_5_high = false;
                break;
            }
        }
        if(hit_5_high) straight_high = 3;

        for(int value = 12; value > 3; value--)
        {
            if(this.values[value] >= 1)
            {
                boolean hitStraight = true;
                for(int i = value; i > value - 5; i--)
                {
                    if(this.values[i] == 0)
                    {
                        hitStraight = false;
                        break;
                    }
                }
                if(hitStraight)
                {
                    straight_high = value;
                    break;
                }
            }
        }

        if(straight_high == -1) return false;
        else
        {
            this.rank = 4;
            this.tiebreakInfo.add(straight_high);
            return true;
        }
    }

    public boolean threeOfAKind() // rank 3
    {
        // returns tiebreak info as [tripsvalue, kicker1, kicker2]
        int trips_value = -1, kicker1 = -1, kicker2 = -1;

        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] == 3) trips_value = value;
            else if(this.values[value] == 1)
            {
                kicker2 = kicker1;
                kicker1 = value;
            }
        }

        if(trips_value == -1) return false;
        else
        {
            this.rank = 3;
            this.tiebreakInfo.add(trips_value);
            this.tiebreakInfo.add(kicker1);
            this.tiebreakInfo.add(kicker2);
            return true;
        }
    }

    public boolean twoPair() // rank 2
    {
        // returns tiebreak info as [pairhigh, pairlow, kicker]
        int pair1 = -1, pair2 = -1, kicker = -1;
        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] == 2)
            {
                pair2 = pair1;
                pair1 = value;
            }
        }

        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] >= 1 && value != pair1 && value != pair2)
                kicker = value;
        }

        if(pair1 == -1 || pair2 == -1) return false;
        else
        {
            this.rank = 2;
            this.tiebreakInfo.add(pair1);
            this.tiebreakInfo.add(pair2);
            this.tiebreakInfo.add(kicker);
            return true;
        }
    }

    public boolean pair() // rank 1
    {
        // returns tiebreak info as [pair, kicker1, kicker2, kicker3]
        int pair = -1;
        int[] kickers = {-1, -1, -1};
        for(int value = 0; value < 13; value++)
        {
            if(this.values[value] == 2) pair = value;
            else if(this.values[value] == 1)
            {
                kickers[2] = kickers[1];
                kickers[1] = kickers[0];
                kickers[0] = value;
            }
        }
        if(pair == -1) return false;
        else
        {
            this.rank = 1;
            this.tiebreakInfo.add(pair);
            for(int kicker : kickers) this.tiebreakInfo.add(kicker);
            return true;
        }
    }

    public void highCard() // rank 0
    {
        // returns tiebreakinfo as [list of high cards in order]
        this.rank = 0;
        for(int value = 12; value >= 0; value--)
        {
            if(this.values[value] == 1 && this.tiebreakInfo.size() < 5)
                this.tiebreakInfo.add(value);
        }
    }
}